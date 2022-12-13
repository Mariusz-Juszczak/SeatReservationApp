import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IFloor, NewFloor } from '../floor.model';
import {DimensionsFormGroup, DimensionsFormService} from "../../dimensions/update/dimensions-form.service";

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IFloor for edit and NewFloorFormGroupInput for create.
 */
type FloorFormGroupInput = IFloor | PartialWithRequiredKeyOf<NewFloor>;

type FloorFormDefaults = Pick<NewFloor, 'id'>;

type FloorFormGroupContent = {
  id: FormControl<IFloor['id'] | NewFloor['id']>;
  name: FormControl<IFloor['name']>;
  number: FormControl<IFloor['number']>;
  map: FormControl<IFloor['map']>;
  mapContentType: FormControl<IFloor['mapContentType']>;
  dimensions: DimensionsFormGroup;
  building: FormControl<IFloor['building']>;
};


export type FloorFormGroup = FormGroup<FloorFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class FloorFormService {

  constructor(
    protected dimensionsFormService: DimensionsFormService,
  ) {}

  createFloorFormGroup(floor: FloorFormGroupInput = { id: null }): FloorFormGroup {
    const floorRawValue = {
      ...this.getFormDefaults(),
      ...floor,
    };
    return new FormGroup<FloorFormGroupContent>({
      id: new FormControl(
        { value: floorRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      name: new FormControl(floorRawValue.name, {
        validators: [Validators.required],
      }),
      number: new FormControl(floorRawValue.number, {
        validators: [Validators.required],
      }),
      map: new FormControl(floorRawValue.map),
      mapContentType: new FormControl(floorRawValue.mapContentType),
      dimensions: this.dimensionsFormService.createDimensionsFormGroup(),
      building: new FormControl(floorRawValue.building),
    });
  }

  getFloor(form: FloorFormGroup): IFloor | NewFloor {
    return form.getRawValue() as IFloor | NewFloor;
  }

  resetForm(form: FloorFormGroup, floor: FloorFormGroupInput): void {
    if (floor.dimensions == null) {
      floor.dimensions = this.dimensionsFormService.getFormDefaults();
    }
    const floorRawValue = { ...this.getFormDefaults(), ...floor };
    form.reset(
      {
        ...floorRawValue,
        id: { value: floorRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): FloorFormDefaults {
    return {
      id: null,
    };
  }
}
