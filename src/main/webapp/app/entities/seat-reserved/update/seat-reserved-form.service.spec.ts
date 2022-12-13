import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../seat-reserved.test-samples';

import { SeatReservedFormService } from './seat-reserved-form.service';

describe('SeatReserved Form Service', () => {
  let service: SeatReservedFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(SeatReservedFormService);
  });

  describe('Service methods', () => {
    describe('createSeatReservedFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createSeatReservedFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            name: expect.any(Object),
            fromDate: expect.any(Object),
            toDate: expect.any(Object),
            createdDate: expect.any(Object),
            reservationStatus: expect.any(Object),
            user: expect.any(Object),
          })
        );
      });

      it('passing ISeatReserved should create a new form with FormGroup', () => {
        const formGroup = service.createSeatReservedFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            name: expect.any(Object),
            fromDate: expect.any(Object),
            toDate: expect.any(Object),
            createdDate: expect.any(Object),
            reservationStatus: expect.any(Object),
            user: expect.any(Object),
          })
        );
      });
    });

    describe('getSeatReserved', () => {
      it('should return NewSeatReserved for default SeatReserved initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createSeatReservedFormGroup(sampleWithNewData);

        const seatReserved = service.getSeatReserved(formGroup) as any;

        expect(seatReserved).toMatchObject(sampleWithNewData);
      });

      it('should return NewSeatReserved for empty SeatReserved initial value', () => {
        const formGroup = service.createSeatReservedFormGroup();

        const seatReserved = service.getSeatReserved(formGroup) as any;

        expect(seatReserved).toMatchObject({});
      });

      it('should return ISeatReserved', () => {
        const formGroup = service.createSeatReservedFormGroup(sampleWithRequiredData);

        const seatReserved = service.getSeatReserved(formGroup) as any;

        expect(seatReserved).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing ISeatReserved should not enable id FormControl', () => {
        const formGroup = service.createSeatReservedFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewSeatReserved should disable id FormControl', () => {
        const formGroup = service.createSeatReservedFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
