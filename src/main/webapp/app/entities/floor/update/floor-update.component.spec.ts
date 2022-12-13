import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { FloorFormService } from './floor-form.service';
import { FloorService } from '../service/floor.service';
import { IFloor } from '../floor.model';
import { IDimensions } from 'app/entities/dimensions/dimensions.model';
import { DimensionsService } from 'app/entities/dimensions/service/dimensions.service';
import { IBuilding } from 'app/entities/building/building.model';
import { BuildingService } from 'app/entities/building/service/building.service';

import { FloorUpdateComponent } from './floor-update.component';

describe('Floor Management Update Component', () => {
  let comp: FloorUpdateComponent;
  let fixture: ComponentFixture<FloorUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let floorFormService: FloorFormService;
  let floorService: FloorService;
  let dimensionsService: DimensionsService;
  let buildingService: BuildingService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [FloorUpdateComponent],
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
      .overrideTemplate(FloorUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(FloorUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    floorFormService = TestBed.inject(FloorFormService);
    floorService = TestBed.inject(FloorService);
    dimensionsService = TestBed.inject(DimensionsService);
    buildingService = TestBed.inject(BuildingService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call dimensions query and add missing value', () => {
      const floor: IFloor = { id: 456 };
      const dimensions: IDimensions = { id: 45173 };
      floor.dimensions = dimensions;

      const dimensionsCollection: IDimensions[] = [{ id: 97167 }];
      jest.spyOn(dimensionsService, 'query').mockReturnValue(of(new HttpResponse({ body: dimensionsCollection })));
      const expectedCollection: IDimensions[] = [dimensions, ...dimensionsCollection];
      jest.spyOn(dimensionsService, 'addDimensionsToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ floor });
      comp.ngOnInit();

      expect(dimensionsService.query).toHaveBeenCalled();
      expect(dimensionsService.addDimensionsToCollectionIfMissing).toHaveBeenCalledWith(dimensionsCollection, dimensions);
      expect(comp.dimensionsCollection).toEqual(expectedCollection);
    });

    it('Should call Building query and add missing value', () => {
      const floor: IFloor = { id: 456 };
      const building: IBuilding = { id: 25094 };
      floor.building = building;

      const buildingCollection: IBuilding[] = [{ id: 41339 }];
      jest.spyOn(buildingService, 'query').mockReturnValue(of(new HttpResponse({ body: buildingCollection })));
      const additionalBuildings = [building];
      const expectedCollection: IBuilding[] = [...additionalBuildings, ...buildingCollection];
      jest.spyOn(buildingService, 'addBuildingToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ floor });
      comp.ngOnInit();

      expect(buildingService.query).toHaveBeenCalled();
      expect(buildingService.addBuildingToCollectionIfMissing).toHaveBeenCalledWith(
        buildingCollection,
        ...additionalBuildings.map(expect.objectContaining)
      );
      expect(comp.buildingsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const floor: IFloor = { id: 456 };
      const dimensions: IDimensions = { id: 39643 };
      floor.dimensions = dimensions;
      const building: IBuilding = { id: 55295 };
      floor.building = building;

      activatedRoute.data = of({ floor });
      comp.ngOnInit();

      expect(comp.dimensionsCollection).toContain(dimensions);
      expect(comp.buildingsSharedCollection).toContain(building);
      expect(comp.floor).toEqual(floor);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IFloor>>();
      const floor = { id: 123 };
      jest.spyOn(floorFormService, 'getFloor').mockReturnValue(floor);
      jest.spyOn(floorService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ floor });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: floor }));
      saveSubject.complete();

      // THEN
      expect(floorFormService.getFloor).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(floorService.update).toHaveBeenCalledWith(expect.objectContaining(floor));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IFloor>>();
      const floor = { id: 123 };
      jest.spyOn(floorFormService, 'getFloor').mockReturnValue({ id: null });
      jest.spyOn(floorService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ floor: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: floor }));
      saveSubject.complete();

      // THEN
      expect(floorFormService.getFloor).toHaveBeenCalled();
      expect(floorService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IFloor>>();
      const floor = { id: 123 };
      jest.spyOn(floorService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ floor });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(floorService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareDimensions', () => {
      it('Should forward to dimensionsService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(dimensionsService, 'compareDimensions');
        comp.compareDimensions(entity, entity2);
        expect(dimensionsService.compareDimensions).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareBuilding', () => {
      it('Should forward to buildingService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(buildingService, 'compareBuilding');
        comp.compareBuilding(entity, entity2);
        expect(buildingService.compareBuilding).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
