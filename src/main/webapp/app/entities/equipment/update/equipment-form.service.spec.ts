import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../equipment.test-samples';

import { EquipmentFormService } from './equipment-form.service';

describe('Equipment Form Service', () => {
  let service: EquipmentFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(EquipmentFormService);
  });

  describe('Service methods', () => {
    describe('createEquipmentFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createEquipmentFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            name: expect.any(Object),
            type: expect.any(Object),
            description: expect.any(Object),
            seat: expect.any(Object),
          })
        );
      });

      it('passing IEquipment should create a new form with FormGroup', () => {
        const formGroup = service.createEquipmentFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            name: expect.any(Object),
            type: expect.any(Object),
            description: expect.any(Object),
            seat: expect.any(Object),
          })
        );
      });
    });

    describe('getEquipment', () => {
      it('should return NewEquipment for default Equipment initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createEquipmentFormGroup(sampleWithNewData);

        const equipment = service.getEquipment(formGroup) as any;

        expect(equipment).toMatchObject(sampleWithNewData);
      });

      it('should return NewEquipment for empty Equipment initial value', () => {
        const formGroup = service.createEquipmentFormGroup();

        const equipment = service.getEquipment(formGroup) as any;

        expect(equipment).toMatchObject({});
      });

      it('should return IEquipment', () => {
        const formGroup = service.createEquipmentFormGroup(sampleWithRequiredData);

        const equipment = service.getEquipment(formGroup) as any;

        expect(equipment).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IEquipment should not enable id FormControl', () => {
        const formGroup = service.createEquipmentFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewEquipment should disable id FormControl', () => {
        const formGroup = service.createEquipmentFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
