import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IDimensions, NewDimensions } from '../dimensions.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IDimensions for edit and NewDimensionsFormGroupInput for create.
 */
type DimensionsFormGroupInput = IDimensions | PartialWithRequiredKeyOf<NewDimensions>;

type DimensionsFormDefaults = Pick<NewDimensions, 'id'>;

type DimensionsFormGroupContent = {
  id: FormControl<IDimensions['id'] | NewDimensions['id']>;
  height: FormControl<IDimensions['height']>;
  width: FormControl<IDimensions['width']>;
};

export type DimensionsFormGroup = FormGroup<DimensionsFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class DimensionsFormService {
  createDimensionsFormGroup(dimensions: DimensionsFormGroupInput = { id: null }): DimensionsFormGroup {
    const dimensionsRawValue = {
      ...this.getFormDefaults(),
      ...dimensions,
    };
    return new FormGroup<DimensionsFormGroupContent>({
      id: new FormControl(
        { value: dimensionsRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      height: new FormControl(dimensionsRawValue.height, {
        validators: [Validators.required],
      }),
      width: new FormControl(dimensionsRawValue.width, {
        validators: [Validators.required],
      }),
    });
  }

  getFormDefaults(): DimensionsFormDefaults {
    return {
      id: null,
    };
  }
}
