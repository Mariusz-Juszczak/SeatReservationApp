import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { ISeat, NewSeat } from '../seat.model';
import {DimensionsFormGroup, DimensionsFormService} from "../../dimensions/update/dimensions-form.service";
import {CoordinatesFormGroup, CoordinatesFormService} from "../../coordinates/update/coordinates-form.service";

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts ISeat for edit and NewSeatFormGroupInput for create.
 */
type SeatFormGroupInput = ISeat | PartialWithRequiredKeyOf<NewSeat>;

type SeatFormDefaults = Pick<NewSeat, 'id'>;

type SeatFormGroupContent = {
  id: FormControl<ISeat['id'] | NewSeat['id']>;
  seatNumber: FormControl<ISeat['seatNumber']>;
  name: FormControl<ISeat['name']>;
  status: FormControl<ISeat['status']>;
  coordinates: CoordinatesFormGroup;
  dimensions: DimensionsFormGroup;
  room: FormControl<ISeat['room']>;
};

export type SeatFormGroup = FormGroup<SeatFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class SeatFormService {

  constructor(
    protected dimensionsFormService: DimensionsFormService,
    protected coordinatesFormService: CoordinatesFormService,
  ) {}

  createSeatFormGroup(seat: SeatFormGroupInput = { id: null }): SeatFormGroup {
    const seatRawValue = {
      ...this.getFormDefaults(),
      ...seat,
    };
    return new FormGroup<SeatFormGroupContent>({
      id: new FormControl(
        { value: seatRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      seatNumber: new FormControl(seatRawValue.seatNumber, {
        validators: [Validators.required],
      }),
      name: new FormControl(seatRawValue.name, {
        validators: [Validators.required],
      }),
      status: new FormControl(seatRawValue.status, {
        validators: [Validators.required],
      }),
      coordinates: this.coordinatesFormService.createCoordinatesFormGroup(),
      dimensions: this.dimensionsFormService.createDimensionsFormGroup(),
      room: new FormControl(seatRawValue.room),
    });
  }

  getSeat(form: SeatFormGroup): ISeat | NewSeat {
    return form.getRawValue() as ISeat | NewSeat;
  }

  resetForm(form: SeatFormGroup, seat: SeatFormGroupInput): void {
    if (seat.dimensions == null) {
      seat.dimensions = this.dimensionsFormService.getFormDefaults();
    }
    if (seat.coordinates == null) {
      seat.coordinates = this.coordinatesFormService.getFormDefaults();
    }
    const seatRawValue = { ...this.getFormDefaults(), ...seat };
    form.reset(
      {
        ...seatRawValue,
        id: { value: seatRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): SeatFormDefaults {
    return {
      id: null,
    };
  }
}
