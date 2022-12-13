import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { SeatFormService } from './seat-form.service';
import { SeatService } from '../service/seat.service';
import { ISeat } from '../seat.model';
import { ICoordinates } from 'app/entities/coordinates/coordinates.model';
import { CoordinatesService } from 'app/entities/coordinates/service/coordinates.service';
import { IDimensions } from 'app/entities/dimensions/dimensions.model';
import { DimensionsService } from 'app/entities/dimensions/service/dimensions.service';
import { ISeatReserved } from 'app/entities/seat-reserved/seat-reserved.model';
import { SeatReservedService } from 'app/entities/seat-reserved/service/seat-reserved.service';
import { IRoom } from 'app/entities/room/room.model';
import { RoomService } from 'app/entities/room/service/room.service';

import { SeatUpdateComponent } from './seat-update.component';

describe('Seat Management Update Component', () => {
  let comp: SeatUpdateComponent;
  let fixture: ComponentFixture<SeatUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let seatFormService: SeatFormService;
  let seatService: SeatService;
  let coordinatesService: CoordinatesService;
  let dimensionsService: DimensionsService;
  let seatReservedService: SeatReservedService;
  let roomService: RoomService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [SeatUpdateComponent],
      providers: [
        FormBuilder,
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    })
      .overrideTemplate(SeatUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(SeatUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    seatFormService = TestBed.inject(SeatFormService);
    seatService = TestBed.inject(SeatService);
    coordinatesService = TestBed.inject(CoordinatesService);
    dimensionsService = TestBed.inject(DimensionsService);
    seatReservedService = TestBed.inject(SeatReservedService);
    roomService = TestBed.inject(RoomService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call coordinates query and add missing value', () => {
      const seat: ISeat = { id: 456 };
      const coordinates: ICoordinates = { id: 89427 };
      seat.coordinates = coordinates;

      const coordinatesCollection: ICoordinates[] = [{ id: 33912 }];
      jest.spyOn(coordinatesService, 'query').mockReturnValue(of(new HttpResponse({ body: coordinatesCollection })));
      const expectedCollection: ICoordinates[] = [coordinates, ...coordinatesCollection];
      jest.spyOn(coordinatesService, 'addCoordinatesToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ seat });
      comp.ngOnInit();

      expect(coordinatesService.query).toHaveBeenCalled();
      expect(coordinatesService.addCoordinatesToCollectionIfMissing).toHaveBeenCalledWith(coordinatesCollection, coordinates);
      expect(comp.coordinatesCollection).toEqual(expectedCollection);
    });

    it('Should call dimensions query and add missing value', () => {
      const seat: ISeat = { id: 456 };
      const dimensions: IDimensions = { id: 27322 };
      seat.dimensions = dimensions;

      const dimensionsCollection: IDimensions[] = [{ id: 59968 }];
      jest.spyOn(dimensionsService, 'query').mockReturnValue(of(new HttpResponse({ body: dimensionsCollection })));
      const expectedCollection: IDimensions[] = [dimensions, ...dimensionsCollection];
      jest.spyOn(dimensionsService, 'addDimensionsToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ seat });
      comp.ngOnInit();

      expect(dimensionsService.query).toHaveBeenCalled();
      expect(dimensionsService.addDimensionsToCollectionIfMissing).toHaveBeenCalledWith(dimensionsCollection, dimensions);
      expect(comp.dimensionsCollection).toEqual(expectedCollection);
    });

    it('Should call seatReserved query and add missing value', () => {
      const seat: ISeat = { id: 456 };
      const seatReserved: ISeatReserved = { id: 98458 };
      seat.seatReserved = seatReserved;

      const seatReservedCollection: ISeatReserved[] = [{ id: 81248 }];
      jest.spyOn(seatReservedService, 'query').mockReturnValue(of(new HttpResponse({ body: seatReservedCollection })));
      const expectedCollection: ISeatReserved[] = [seatReserved, ...seatReservedCollection];
      jest.spyOn(seatReservedService, 'addSeatReservedToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ seat });
      comp.ngOnInit();

      expect(seatReservedService.query).toHaveBeenCalled();
      expect(seatReservedService.addSeatReservedToCollectionIfMissing).toHaveBeenCalledWith(seatReservedCollection, seatReserved);
      expect(comp.seatReservedsCollection).toEqual(expectedCollection);
    });

    it('Should call Room query and add missing value', () => {
      const seat: ISeat = { id: 456 };
      const room: IRoom = { id: 11994 };
      seat.room = room;

      const roomCollection: IRoom[] = [{ id: 23235 }];
      jest.spyOn(roomService, 'query').mockReturnValue(of(new HttpResponse({ body: roomCollection })));
      const additionalRooms = [room];
      const expectedCollection: IRoom[] = [...additionalRooms, ...roomCollection];
      jest.spyOn(roomService, 'addRoomToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ seat });
      comp.ngOnInit();

      expect(roomService.query).toHaveBeenCalled();
      expect(roomService.addRoomToCollectionIfMissing).toHaveBeenCalledWith(
        roomCollection,
        ...additionalRooms.map(expect.objectContaining)
      );
      expect(comp.roomsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const seat: ISeat = { id: 456 };
      const coordinates: ICoordinates = { id: 98127 };
      seat.coordinates = coordinates;
      const dimensions: IDimensions = { id: 65569 };
      seat.dimensions = dimensions;
      const seatReserved: ISeatReserved = { id: 89353 };
      seat.seatReserved = seatReserved;
      const room: IRoom = { id: 8633 };
      seat.room = room;

      activatedRoute.data = of({ seat });
      comp.ngOnInit();

      expect(comp.coordinatesCollection).toContain(coordinates);
      expect(comp.dimensionsCollection).toContain(dimensions);
      expect(comp.seatReservedsCollection).toContain(seatReserved);
      expect(comp.roomsSharedCollection).toContain(room);
      expect(comp.seat).toEqual(seat);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ISeat>>();
      const seat = { id: 123 };
      jest.spyOn(seatFormService, 'getSeat').mockReturnValue(seat);
      jest.spyOn(seatService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ seat });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: seat }));
      saveSubject.complete();

      // THEN
      expect(seatFormService.getSeat).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(seatService.update).toHaveBeenCalledWith(expect.objectContaining(seat));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ISeat>>();
      const seat = { id: 123 };
      jest.spyOn(seatFormService, 'getSeat').mockReturnValue({ id: null });
      jest.spyOn(seatService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ seat: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: seat }));
      saveSubject.complete();

      // THEN
      expect(seatFormService.getSeat).toHaveBeenCalled();
      expect(seatService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ISeat>>();
      const seat = { id: 123 };
      jest.spyOn(seatService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ seat });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(seatService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareCoordinates', () => {
      it('Should forward to coordinatesService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(coordinatesService, 'compareCoordinates');
        comp.compareCoordinates(entity, entity2);
        expect(coordinatesService.compareCoordinates).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareDimensions', () => {
      it('Should forward to dimensionsService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(dimensionsService, 'compareDimensions');
        comp.compareDimensions(entity, entity2);
        expect(dimensionsService.compareDimensions).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareSeatReserved', () => {
      it('Should forward to seatReservedService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(seatReservedService, 'compareSeatReserved');
        comp.compareSeatReserved(entity, entity2);
        expect(seatReservedService.compareSeatReserved).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareRoom', () => {
      it('Should forward to roomService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(roomService, 'compareRoom');
        comp.compareRoom(entity, entity2);
        expect(roomService.compareRoom).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
