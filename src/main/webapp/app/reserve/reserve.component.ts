import {Component, OnInit} from '@angular/core';
import {ActivatedRoute, Router} from '@angular/router';
import {NgbModal} from '@ng-bootstrap/ng-bootstrap';

import {ILocation} from '../entities/location/location.model';
import {IBuilding} from '../entities/building/building.model';
import {IFloor} from '../entities/floor/floor.model';

import {FormControl, FormGroup, Validators} from "@angular/forms";
import {EntityArrayResponseType, SeatService} from "../entities/seat/service/seat.service";
import {ISeat} from "../entities/seat/seat.model";
import {IRoom} from "../entities/room/room.model";
import {BuildingService} from "../entities/building/service/building.service";
import {FloorService} from "../entities/floor/service/floor.service";
import dayjs from "dayjs/esm";
import {ASC, DESC} from "../config/navigation.constants";
import {NewSeatReserved} from "../entities/seat-reserved/seat-reserved.model";
import {SeatReservedService} from "../entities/seat-reserved/service/seat-reserved.service";
import {DATE_TIME_FORMAT} from "../config/input.constants";
import {DataUtils} from "../core/util/data-util.service";
import {LocationService} from "../entities/location/service/location.service";
import {EquipmentType} from "../entities/enumerations/equipment-type.model";
import {Observable} from "rxjs";
import {HttpResponse} from "@angular/common/http";

@Component({
  selector: 'app-reserve',
  templateUrl: './reserve.component.html',
  styleUrls: ['./reserve.component.scss']
})
export class ReserveComponent implements OnInit {
  locations?: ILocation[];
  selectedLocation?: ILocation;
  selectedBuilding?: IBuilding;
  selectedFloor?: IFloor;
  selectedFromDate?: dayjs.Dayjs;
  selectedToDate?: dayjs.Dayjs;
  buildings?: IBuilding[];
  rooms?: IRoom[];
  floors?: IFloor[];
  seats?: ISeat[];
  rawSeats?: ISeat[];
  floor?: IFloor;
  isLoading = false;

  predicate = 'status';
  ascending = true;

  equipmentTypes: string[] = Object.keys(EquipmentType).sort();
  selectedEquipmentTypes: string[] = [];

  locationBuildingParams = {
    sort: this.getSortQueryParam('name', true),
  }

  floorParams = {
    sort: this.getSortQueryParam('number', true),
  }
  reserveForm = new FormGroup({
    location: new FormControl('', {nonNullable: true, validators: [Validators.required]}),
    building: new FormControl('', {nonNullable: true, validators: [Validators.required]}),
    floor: new FormControl('', {nonNullable: true, validators: [Validators.required]}),
    fromDate: new FormControl('', {nonNullable: true, validators: [Validators.required]}),
    toDate: new FormControl('', {nonNullable: true, validators: [Validators.required]}),
    equipment: new FormControl(''),
  });

  constructor(
    protected activatedRoute: ActivatedRoute,
    protected seatReservedService: SeatReservedService,
    protected seatService: SeatService,
    protected locationService: LocationService,
    protected buildingService: BuildingService,
    protected floorService: FloorService,
    protected router: Router,
    protected modalService: NgbModal,
    protected dataUtils: DataUtils
  ) {
  }

  trackId = (_index: number, item: ISeat): number => this.seatService.getSeatIdentifier(item);

  ngOnInit(): void {
    this.loadLocations();
  }

  loadLocations(): void {
    this.isLoading = true;
    this.locationService.query(this.locationBuildingParams).subscribe(res => {
      this.locations = res.body ?? [];
      this.isLoading = false;
    });
  }

  loadBuildings(): void {
    this.cleanUpBuildings();
    const locationId = this.selectedLocation?.id;
    if (locationId != null) {
      this.isLoading = true;
      this.buildingService.findByLocationId(locationId, this.locationBuildingParams).subscribe(res => {
        this.buildings = res.body ?? [];
        this.isLoading = false;
      });
    }
  }

  loadFloors(): void {
    this.cleanUpFloors();
    const buildingId = this.selectedBuilding?.id;
    if (buildingId != null) {
      this.isLoading = true;
      this.floorService.findByBuildingId(buildingId, this.floorParams).subscribe(res => {
        this.floors = res.body ?? [];
        this.isLoading = false;
      });
    }
  }

  loadSeats(): void {
    this.cleanUpSeats();
    const floorId = this.selectedFloor?.id;
    if (floorId != null && this.selectedFromDate && this.selectedToDate) {
      this.isLoading = true;
      this.seatService.findByFloorIdAndPeriod(floorId, this.getPageableParameters(), this.selectedFromDate, this.selectedToDate)
        .subscribe({
          next: (res) => this.successSeatsLoading(res),
          error: () => this.isLoading = false,
        });
    }
  }

  sortAndFilter(): void {
    this.seats = this.rawSeats?.filter(seat => {
      const seatEquipments = seat.equipments?.map(equipment => equipment.type as string)?? [];
      return this.selectedEquipmentTypes.every(s => seatEquipments.includes(s));
    });
    if (this.seats) {
      this.seats = this.seats.sort((a: ISeat, b: ISeat) => {
        const acsValue = this.ascending ? 1 : -1;
        if (this.predicate === 'status') {
          return acsValue * (a.availabilityStatus ?? '').localeCompare(b.availabilityStatus ?? '', 'en')
        }
        if (this.predicate === 'roomName') {
          return acsValue * (a.room?.name ?? '').localeCompare(b.room?.name ?? '', 'en')
        }
        if (this.predicate === 'seatName') {
          return acsValue * (a.name ?? '').localeCompare(b.name ?? '', 'en')
        }
        return 0;
      });
    }
  }

  reserve(seat: ISeat): void {
    if (this.selectedFromDate && this.selectedToDate) {
      const seatReserved: NewSeatReserved = {
        id: null,
        fromDate: dayjs(this.selectedFromDate, DATE_TIME_FORMAT),
        toDate: dayjs(this.selectedToDate, DATE_TIME_FORMAT),
        seatId: seat.id
      };
      this.isLoading = false;
      this.seatReservedService.create(seatReserved)
        .subscribe({
          next: () => this.loadSeats(),
          error: () => this.isLoading = false,
        });
    }
  }

  showMap(): void {
    if (this.selectedFloor?.map) {
      this.openFile(this.selectedFloor.map, this.selectedFloor.mapContentType)
    }
  }

  openFile(base64String: string, contentType: string | null | undefined): void {
    return this.dataUtils.openFile(base64String, contentType);
  }

  protected successSeatsLoading(res: any): void {
    this.seats = res.body ?? [];
    this.rawSeats = this.seats;
    this.sortAndFilter();
    this.isLoading = false;
  }

  protected getPageableParameters(): any {
    this.isLoading = true;
    return {
      sort: this.getSortQueryParam(this.predicate, this.ascending),
    };
  }

  protected getSortQueryParam(predicate = this.predicate, ascending = this.ascending): string[] {
    const ascendingQueryParam = ascending ? ASC : DESC;
    if (predicate === '') {
      return [];
    } else {
      return [predicate + ',' + ascendingQueryParam];
    }
  }

  protected cleanUpBuildings(): void {
    this.buildings = [];
    this.selectedBuilding = undefined;
    this.cleanUpFloors();
  }

  protected cleanUpFloors(): void {
    this.floors = [];
    this.selectedFloor = undefined;
    this.cleanUpSeats();
  }

  protected cleanUpSeats(): void {
    this.seats = [];
  }


}
