import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IBuilding, NewBuilding } from '../building.model';
import {AddressFormGroup, AddressFormService} from "../../address/update/address-form.service";

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IBuilding for edit and NewBuildingFormGroupInput for create.
 */
type BuildingFormGroupInput = IBuilding | PartialWithRequiredKeyOf<NewBuilding>;

type BuildingFormDefaults = Pick<NewBuilding, 'id'>;

type BuildingFormGroupContent = {
  id: FormControl<IBuilding['id'] | NewBuilding['id']>;
  name: FormControl<IBuilding['name']>;
  address: AddressFormGroup;
  location: FormControl<IBuilding['location']>;
};

export type BuildingFormGroup = FormGroup<BuildingFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class BuildingFormService {

  constructor(
    protected addressFormService: AddressFormService,
  ) {}

  createBuildingFormGroup(building: BuildingFormGroupInput = { id: null }): BuildingFormGroup {
    const buildingRawValue = {
      ...this.getFormDefaults(),
      ...building,
    };
    return new FormGroup<BuildingFormGroupContent>({
      id: new FormControl(
        { value: buildingRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      name: new FormControl(buildingRawValue.name, {
        validators: [Validators.required],
      }),
      address: this.addressFormService.createAddressFormGroup(),
      location: new FormControl(buildingRawValue.location),
    });
  }

  getBuilding(form: BuildingFormGroup): IBuilding | NewBuilding {
    return form.getRawValue() as IBuilding | NewBuilding;
  }

  resetForm(form: BuildingFormGroup, building: BuildingFormGroupInput): void {
    if (building.address == null) {
      building.address = this.addressFormService.getFormDefaults();
    }
    const buildingRawValue = { ...this.getFormDefaults(), ...building };
    form.reset(
      {
        ...buildingRawValue,
        id: { value: buildingRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): BuildingFormDefaults {
    return {
      id: null,
    };
  }
}
