import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { ICoordinates, NewCoordinates } from '../coordinates.model';
import {IDimensions} from "../../dimensions/dimensions.model";

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts ICoordinates for edit and NewCoordinatesFormGroupInput for create.
 */
type CoordinatesFormGroupInput = ICoordinates | PartialWithRequiredKeyOf<NewCoordinates>;

type CoordinatesFormDefaults = Pick<NewCoordinates, 'id'>;

type CoordinatesFormGroupContent = {
  id: FormControl<ICoordinates['id'] | NewCoordinates['id']>;
  fromTop: FormControl<ICoordinates['fromTop']>;
  fromLeft: FormControl<ICoordinates['fromLeft']>;
  angle: FormControl<ICoordinates['angle']>;
};

export type CoordinatesFormGroup = FormGroup<CoordinatesFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class CoordinatesFormService {
  createCoordinatesFormGroup(coordinates: CoordinatesFormGroupInput = { id: null }): CoordinatesFormGroup {
    const coordinatesRawValue = {
      ...this.getFormDefaults(),
      ...coordinates,
    };
    return new FormGroup<CoordinatesFormGroupContent>({
      id: new FormControl(
        { value: coordinatesRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      fromTop: new FormControl(coordinatesRawValue.fromTop, {
        validators: [Validators.required, Validators.min(0), Validators.max(100)],
      }),
      fromLeft: new FormControl(coordinatesRawValue.fromLeft, {
        validators: [Validators.required, Validators.min(0), Validators.max(100)],
      }),
      angle: new FormControl(coordinatesRawValue.angle, {
        validators: [Validators.required, Validators.min(0), Validators.max(360)],
      }),
    });
  }

  getFormDefaults(): CoordinatesFormDefaults {
    return {
      id: null,
    };
  }
}
