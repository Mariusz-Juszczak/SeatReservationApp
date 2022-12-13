import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { BuildingFormService, BuildingFormGroup } from './building-form.service';
import { IBuilding } from '../building.model';
import { BuildingService } from '../service/building.service';
import { ILocation } from 'app/entities/location/location.model';
import { LocationService } from 'app/entities/location/service/location.service';

@Component({
  selector: 'app-building-update',
  templateUrl: './building-update.component.html',
})
export class BuildingUpdateComponent implements OnInit {
  isSaving = false;
  building: IBuilding | null = null;
  locationsSharedCollection: ILocation[] = [];

  editForm: BuildingFormGroup = this.buildingFormService.createBuildingFormGroup();

  constructor(
    protected buildingService: BuildingService,
    protected buildingFormService: BuildingFormService,
    protected locationService: LocationService,
    protected activatedRoute: ActivatedRoute
  ) {}

  compareLocation = (o1: ILocation | null, o2: ILocation | null): boolean => this.locationService.compareLocation(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ building }) => {
      this.building = building;
      if (building) {
        this.updateForm(building);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const building = this.buildingFormService.getBuilding(this.editForm);
    if (building.id !== null) {
      this.subscribeToSaveResponse(this.buildingService.update(building));
    } else {
      this.subscribeToSaveResponse(this.buildingService.create(building));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IBuilding>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(building: IBuilding): void {
    this.building = building;
    this.buildingFormService.resetForm(this.editForm, building);

    this.locationsSharedCollection = this.locationService.addLocationToCollectionIfMissing<ILocation>(
      this.locationsSharedCollection,
      building.location
    );
  }

  protected loadRelationshipsOptions(): void {
    this.locationService
      .query()
      .pipe(map((res: HttpResponse<ILocation[]>) => res.body ?? []))
      .pipe(
        map((locations: ILocation[]) => this.locationService.addLocationToCollectionIfMissing<ILocation>(locations, this.building?.location) )
      )
      .subscribe((locations: ILocation[]) => (this.locationsSharedCollection = locations));
  }
}
