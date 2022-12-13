import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../restrictions.test-samples';

import { RestrictionsFormService } from './restrictions-form.service';

describe('Restrictions Form Service', () => {
  let service: RestrictionsFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(RestrictionsFormService);
  });

  describe('Service methods', () => {
    describe('createRestrictionsFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createRestrictionsFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            name: expect.any(Object),
            fromDate: expect.any(Object),
            toDate: expect.any(Object),
            availabilityPercentage: expect.any(Object),
            availabilityPerNumberOfSeats: expect.any(Object),
          })
        );
      });

      it('passing IRestrictions should create a new form with FormGroup', () => {
        const formGroup = service.createRestrictionsFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            name: expect.any(Object),
            fromDate: expect.any(Object),
            toDate: expect.any(Object),
            availabilityPercentage: expect.any(Object),
            availabilityPerNumberOfSeats: expect.any(Object),
          })
        );
      });
    });

    describe('getRestrictions', () => {
      it('should return NewRestrictions for default Restrictions initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createRestrictionsFormGroup(sampleWithNewData);

        const restrictions = service.getRestrictions(formGroup) as any;

        expect(restrictions).toMatchObject(sampleWithNewData);
      });

      it('should return NewRestrictions for empty Restrictions initial value', () => {
        const formGroup = service.createRestrictionsFormGroup();

        const restrictions = service.getRestrictions(formGroup) as any;

        expect(restrictions).toMatchObject({});
      });

      it('should return IRestrictions', () => {
        const formGroup = service.createRestrictionsFormGroup(sampleWithRequiredData);

        const restrictions = service.getRestrictions(formGroup) as any;

        expect(restrictions).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IRestrictions should not enable id FormControl', () => {
        const formGroup = service.createRestrictionsFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewRestrictions should disable id FormControl', () => {
        const formGroup = service.createRestrictionsFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
