import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IAddress, NewAddress } from '../address.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IAddress for edit and NewAddressFormGroupInput for create.
 */
type AddressFormGroupInput = IAddress | PartialWithRequiredKeyOf<NewAddress>;

type AddressFormDefaults = Pick<NewAddress, 'id'>;

type AddressFormGroupContent = {
  id: FormControl<IAddress['id'] | NewAddress['id']>;
  country: FormControl<IAddress['country']>;
  street: FormControl<IAddress['street']>;
  postalCode: FormControl<IAddress['postalCode']>;
  city: FormControl<IAddress['city']>;
  stateProvince: FormControl<IAddress['stateProvince']>;
};

export type AddressFormGroup = FormGroup<AddressFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class AddressFormService {

  createAddressFormGroup(address: AddressFormGroupInput = { id: null }): AddressFormGroup {
    const addressRawValue = {
      ...this.getFormDefaults(),
      ...address,
    };
    return new FormGroup<AddressFormGroupContent>({
      id: new FormControl(
        { value: addressRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      country: new FormControl(addressRawValue.country, {
        validators: [Validators.required],
      }),
      street: new FormControl(addressRawValue.street, {
        validators: [Validators.required],
      }),
      postalCode: new FormControl(addressRawValue.postalCode, {
        validators: [Validators.required],
      }),
      city: new FormControl(addressRawValue.city, {
        validators: [Validators.required],
      }),
      stateProvince: new FormControl(addressRawValue.stateProvince, {
        validators: [Validators.required],
      }),
    });
  }

  public getFormDefaults(): AddressFormDefaults {
    return {
      id: null,
    };
  }
}
