import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { ISeatReserved, NewSeatReserved } from '../seat-reserved.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts ISeatReserved for edit and NewSeatReservedFormGroupInput for create.
 */
type SeatReservedFormGroupInput = ISeatReserved | PartialWithRequiredKeyOf<NewSeatReserved>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends ISeatReserved | NewSeatReserved> = Omit<T, 'fromDate' | 'toDate' | 'createdDate'> & {
  fromDate?: string | null;
  toDate?: string | null;
  createdDate?: string | null;
};

type SeatReservedFormRawValue = FormValueOf<ISeatReserved>;

type NewSeatReservedFormRawValue = FormValueOf<NewSeatReserved>;

type SeatReservedFormDefaults = Pick<NewSeatReserved, 'id' | 'fromDate' | 'toDate' | 'createdDate'>;

type SeatReservedFormGroupContent = {
  id: FormControl<SeatReservedFormRawValue['id'] | NewSeatReserved['id']>;
  name: FormControl<SeatReservedFormRawValue['name']>;
  fromDate: FormControl<SeatReservedFormRawValue['fromDate']>;
  toDate: FormControl<SeatReservedFormRawValue['toDate']>;
  createdDate: FormControl<SeatReservedFormRawValue['createdDate']>;
  reservationStatus: FormControl<SeatReservedFormRawValue['reservationStatus']>;
  user: FormControl<SeatReservedFormRawValue['user']>;
};

export type SeatReservedFormGroup = FormGroup<SeatReservedFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class SeatReservedFormService {
  createSeatReservedFormGroup(seatReserved: SeatReservedFormGroupInput = { id: null }): SeatReservedFormGroup {
    const seatReservedRawValue = this.convertSeatReservedToSeatReservedRawValue({
      ...this.getFormDefaults(),
      ...seatReserved,
    });
    return new FormGroup<SeatReservedFormGroupContent>({
      id: new FormControl(
        { value: seatReservedRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      name: new FormControl(seatReservedRawValue.name, {
        validators: [Validators.required],
      }),
      fromDate: new FormControl(seatReservedRawValue.fromDate, {
        validators: [Validators.required],
      }),
      toDate: new FormControl(seatReservedRawValue.toDate),
      createdDate: new FormControl(seatReservedRawValue.createdDate, {
        validators: [Validators.required],
      }),
      reservationStatus: new FormControl(seatReservedRawValue.reservationStatus, {
        validators: [Validators.required],
      }),
      user: new FormControl(seatReservedRawValue.user),
    });
  }

  getSeatReserved(form: SeatReservedFormGroup): ISeatReserved | NewSeatReserved {
    return this.convertSeatReservedRawValueToSeatReserved(form.getRawValue() as SeatReservedFormRawValue | NewSeatReservedFormRawValue);
  }

  resetForm(form: SeatReservedFormGroup, seatReserved: SeatReservedFormGroupInput): void {
    const seatReservedRawValue = this.convertSeatReservedToSeatReservedRawValue({ ...this.getFormDefaults(), ...seatReserved });
    form.reset(
      {
        ...seatReservedRawValue,
        id: { value: seatReservedRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): SeatReservedFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      fromDate: currentTime,
      toDate: currentTime,
      createdDate: currentTime,
    };
  }

  private convertSeatReservedRawValueToSeatReserved(
    rawSeatReserved: SeatReservedFormRawValue | NewSeatReservedFormRawValue
  ): ISeatReserved | NewSeatReserved {
    return {
      ...rawSeatReserved,
      fromDate: dayjs(rawSeatReserved.fromDate, DATE_TIME_FORMAT),
      toDate: dayjs(rawSeatReserved.toDate, DATE_TIME_FORMAT),
      createdDate: dayjs(rawSeatReserved.createdDate, DATE_TIME_FORMAT),
    };
  }

  private convertSeatReservedToSeatReservedRawValue(
    seatReserved: ISeatReserved | (Partial<NewSeatReserved> & SeatReservedFormDefaults)
  ): SeatReservedFormRawValue | PartialWithRequiredKeyOf<NewSeatReservedFormRawValue> {
    return {
      ...seatReserved,
      fromDate: seatReserved.fromDate ? seatReserved.fromDate.format(DATE_TIME_FORMAT) : undefined,
      toDate: seatReserved.toDate ? seatReserved.toDate.format(DATE_TIME_FORMAT) : undefined,
      createdDate: seatReserved.createdDate ? seatReserved.createdDate.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
