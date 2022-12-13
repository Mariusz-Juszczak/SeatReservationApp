import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { RoomFormService } from './room-form.service';
import { RoomService } from '../service/room.service';
import { IRoom } from '../room.model';
import { ICoordinates } from 'app/entities/coordinates/coordinates.model';
import { CoordinatesService } from 'app/entities/coordinates/service/coordinates.service';
import { IDimensions } from 'app/entities/dimensions/dimensions.model';
import { DimensionsService } from 'app/entities/dimensions/service/dimensions.service';
import { IFloor } from 'app/entities/floor/floor.model';
import { FloorService } from 'app/entities/floor/service/floor.service';

import { RoomUpdateComponent } from './room-update.component';

describe('Room Management Update Component', () => {
  let comp: RoomUpdateComponent;
  let fixture: ComponentFixture<RoomUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let roomFormService: RoomFormService;
  let roomService: RoomService;
  let coordinatesService: CoordinatesService;
  let dimensionsService: DimensionsService;
  let floorService: FloorService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [RoomUpdateComponent],
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
      .overrideTemplate(RoomUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(RoomUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    roomFormService = TestBed.inject(RoomFormService);
    roomService = TestBed.inject(RoomService);
    coordinatesService = TestBed.inject(CoordinatesService);
    dimensionsService = TestBed.inject(DimensionsService);
    floorService = TestBed.inject(FloorService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call coordinates query and add missing value', () => {
      const room: IRoom = { id: 456 };
      const coordinates: ICoordinates = { id: 75623 };
      room.coordinates = coordinates;

      const coordinatesCollection: ICoordinates[] = [{ id: 93163 }];
      jest.spyOn(coordinatesService, 'query').mockReturnValue(of(new HttpResponse({ body: coordinatesCollection })));
      const expectedCollection: ICoordinates[] = [coordinates, ...coordinatesCollection];
      jest.spyOn(coordinatesService, 'addCoordinatesToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ room });
      comp.ngOnInit();

      expect(coordinatesService.query).toHaveBeenCalled();
      expect(coordinatesService.addCoordinatesToCollectionIfMissing).toHaveBeenCalledWith(coordinatesCollection, coordinates);
      expect(comp.coordinatesCollection).toEqual(expectedCollection);
    });

    it('Should call dimensions query and add missing value', () => {
      const room: IRoom = { id: 456 };
      const dimensions: IDimensions = { id: 56725 };
      room.dimensions = dimensions;

      const dimensionsCollection: IDimensions[] = [{ id: 85924 }];
      jest.spyOn(dimensionsService, 'query').mockReturnValue(of(new HttpResponse({ body: dimensionsCollection })));
      const expectedCollection: IDimensions[] = [dimensions, ...dimensionsCollection];
      jest.spyOn(dimensionsService, 'addDimensionsToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ room });
      comp.ngOnInit();

      expect(dimensionsService.query).toHaveBeenCalled();
      expect(dimensionsService.addDimensionsToCollectionIfMissing).toHaveBeenCalledWith(dimensionsCollection, dimensions);
      expect(comp.dimensionsCollection).toEqual(expectedCollection);
    });

    it('Should call Floor query and add missing value', () => {
      const room: IRoom = { id: 456 };
      const floor: IFloor = { id: 71631 };
      room.floor = floor;

      const floorCollection: IFloor[] = [{ id: 94723 }];
      jest.spyOn(floorService, 'query').mockReturnValue(of(new HttpResponse({ body: floorCollection })));
      const additionalFloors = [floor];
      const expectedCollection: IFloor[] = [...additionalFloors, ...floorCollection];
      jest.spyOn(floorService, 'addFloorToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ room });
      comp.ngOnInit();

      expect(floorService.query).toHaveBeenCalled();
      expect(floorService.addFloorToCollectionIfMissing).toHaveBeenCalledWith(
        floorCollection,
        ...additionalFloors.map(expect.objectContaining)
      );
      expect(comp.floorsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const room: IRoom = { id: 456 };
      const coordinates: ICoordinates = { id: 33183 };
      room.coordinates = coordinates;
      const dimensions: IDimensions = { id: 6957 };
      room.dimensions = dimensions;
      const floor: IFloor = { id: 1994 };
      room.floor = floor;

      activatedRoute.data = of({ room });
      comp.ngOnInit();

      expect(comp.coordinatesCollection).toContain(coordinates);
      expect(comp.dimensionsCollection).toContain(dimensions);
      expect(comp.floorsSharedCollection).toContain(floor);
      expect(comp.room).toEqual(room);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IRoom>>();
      const room = { id: 123 };
      jest.spyOn(roomFormService, 'getRoom').mockReturnValue(room);
      jest.spyOn(roomService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ room });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: room }));
      saveSubject.complete();

      // THEN
      expect(roomFormService.getRoom).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(roomService.update).toHaveBeenCalledWith(expect.objectContaining(room));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IRoom>>();
      const room = { id: 123 };
      jest.spyOn(roomFormService, 'getRoom').mockReturnValue({ id: null });
      jest.spyOn(roomService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ room: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: room }));
      saveSubject.complete();

      // THEN
      expect(roomFormService.getRoom).toHaveBeenCalled();
      expect(roomService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IRoom>>();
      const room = { id: 123 };
      jest.spyOn(roomService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ room });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(roomService.update).toHaveBeenCalled();
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

    describe('compareFloor', () => {
      it('Should forward to floorService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(floorService, 'compareFloor');
        comp.compareFloor(entity, entity2);
        expect(floorService.compareFloor).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
