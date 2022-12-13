import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { EquipmentFormService } from './equipment-form.service';
import { EquipmentService } from '../service/equipment.service';
import { IEquipment } from '../equipment.model';
import { ISeat } from 'app/entities/seat/seat.model';
import { SeatService } from 'app/entities/seat/service/seat.service';

import { EquipmentUpdateComponent } from './equipment-update.component';

describe('Equipment Management Update Component', () => {
  let comp: EquipmentUpdateComponent;
  let fixture: ComponentFixture<EquipmentUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let equipmentFormService: EquipmentFormService;
  let equipmentService: EquipmentService;
  let seatService: SeatService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [EquipmentUpdateComponent],
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
      .overrideTemplate(EquipmentUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(EquipmentUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    equipmentFormService = TestBed.inject(EquipmentFormService);
    equipmentService = TestBed.inject(EquipmentService);
    seatService = TestBed.inject(SeatService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Seat query and add missing value', () => {
      const equipment: IEquipment = { id: 456 };
      const seat: ISeat = { id: 9127 };
      equipment.seat = seat;

      const seatCollection: ISeat[] = [{ id: 51094 }];
      jest.spyOn(seatService, 'query').mockReturnValue(of(new HttpResponse({ body: seatCollection })));
      const additionalSeats = [seat];
      const expectedCollection: ISeat[] = [...additionalSeats, ...seatCollection];
      jest.spyOn(seatService, 'addSeatToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ equipment });
      comp.ngOnInit();

      expect(seatService.query).toHaveBeenCalled();
      expect(seatService.addSeatToCollectionIfMissing).toHaveBeenCalledWith(
        seatCollection,
        ...additionalSeats.map(expect.objectContaining)
      );
      expect(comp.seatsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const equipment: IEquipment = { id: 456 };
      const seat: ISeat = { id: 86774 };
      equipment.seat = seat;

      activatedRoute.data = of({ equipment });
      comp.ngOnInit();

      expect(comp.seatsSharedCollection).toContain(seat);
      expect(comp.equipment).toEqual(equipment);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IEquipment>>();
      const equipment = { id: 123 };
      jest.spyOn(equipmentFormService, 'getEquipment').mockReturnValue(equipment);
      jest.spyOn(equipmentService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ equipment });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: equipment }));
      saveSubject.complete();

      // THEN
      expect(equipmentFormService.getEquipment).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(equipmentService.update).toHaveBeenCalledWith(expect.objectContaining(equipment));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IEquipment>>();
      const equipment = { id: 123 };
      jest.spyOn(equipmentFormService, 'getEquipment').mockReturnValue({ id: null });
      jest.spyOn(equipmentService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ equipment: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: equipment }));
      saveSubject.complete();

      // THEN
      expect(equipmentFormService.getEquipment).toHaveBeenCalled();
      expect(equipmentService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IEquipment>>();
      const equipment = { id: 123 };
      jest.spyOn(equipmentService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ equipment });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(equipmentService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareSeat', () => {
      it('Should forward to seatService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(seatService, 'compareSeat');
        comp.compareSeat(entity, entity2);
        expect(seatService.compareSeat).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
