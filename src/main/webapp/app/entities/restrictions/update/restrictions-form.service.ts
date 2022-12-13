import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IRestrictions, NewRestrictions } from '../restrictions.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IRestrictions for edit and NewRestrictionsFormGroupInput for create.
 */
type RestrictionsFormGroupInput = IRestrictions | PartialWithRequiredKeyOf<NewRestrictions>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IRestrictions | NewRestrictions> = Omit<T, 'fromDate' | 'toDate'> & {
  fromDate?: string | null;
  toDate?: string | null;
};

type RestrictionsFormRawValue = FormValueOf<IRestrictions>;

type NewRestrictionsFormRawValue = FormValueOf<NewRestrictions>;

type RestrictionsFormDefaults = Pick<NewRestrictions, 'id' | 'fromDate' | 'toDate'>;

type RestrictionsFormGroupContent = {
  id: FormControl<RestrictionsFormRawValue['id'] | NewRestrictions['id']>;
  name: FormControl<RestrictionsFormRawValue['name']>;
  fromDate: FormControl<RestrictionsFormRawValue['fromDate']>;
  toDate: FormControl<RestrictionsFormRawValue['toDate']>;
  restrictionType: FormControl<RestrictionsFormRawValue['restrictionType']>;
  restrictionValue: FormControl<RestrictionsFormRawValue['restrictionValue']>;
};

export type RestrictionsFormGroup = FormGroup<RestrictionsFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class RestrictionsFormService {
  createRestrictionsFormGroup(restrictions: RestrictionsFormGroupInput = { id: null }): RestrictionsFormGroup {
    const restrictionsRawValue = this.convertRestrictionsToRestrictionsRawValue({
      ...this.getFormDefaults(),
      ...restrictions,
    });
    return new FormGroup<RestrictionsFormGroupContent>({
      id: new FormControl(
        { value: restrictionsRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      name: new FormControl(restrictionsRawValue.name, {
        validators: [Validators.required],
      }),
      fromDate: new FormControl(restrictionsRawValue.fromDate, {
        validators: [Validators.required],
      }),
      toDate: new FormControl(restrictionsRawValue.toDate),
      restrictionType: new FormControl(restrictionsRawValue.restrictionType, {
        validators: [Validators.required],
      }),
      restrictionValue: new FormControl(restrictionsRawValue.restrictionValue, {
        validators: [Validators.required, Validators.min(0), Validators.max(100)],
      }),
    });
  }

  getRestrictions(form: RestrictionsFormGroup): IRestrictions | NewRestrictions {
    return this.convertRestrictionsRawValueToRestrictions(form.getRawValue() as RestrictionsFormRawValue | NewRestrictionsFormRawValue);
  }

  resetForm(form: RestrictionsFormGroup, restrictions: RestrictionsFormGroupInput): void {
    const restrictionsRawValue = this.convertRestrictionsToRestrictionsRawValue({ ...this.getFormDefaults(), ...restrictions });
    form.reset(
      {
        ...restrictionsRawValue,
        id: { value: restrictionsRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): RestrictionsFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      fromDate: currentTime,
      toDate: currentTime,
    };
  }

  private convertRestrictionsRawValueToRestrictions(
    rawRestrictions: RestrictionsFormRawValue | NewRestrictionsFormRawValue
  ): IRestrictions | NewRestrictions {
    return {
      ...rawRestrictions,
      fromDate: dayjs(rawRestrictions.fromDate, DATE_TIME_FORMAT),
      toDate: dayjs(rawRestrictions.toDate, DATE_TIME_FORMAT),
    };
  }

  private convertRestrictionsToRestrictionsRawValue(
    restrictions: IRestrictions | (Partial<NewRestrictions> & RestrictionsFormDefaults)
  ): RestrictionsFormRawValue | PartialWithRequiredKeyOf<NewRestrictionsFormRawValue> {
    return {
      ...restrictions,
      fromDate: restrictions.fromDate ? restrictions.fromDate.format(DATE_TIME_FORMAT) : undefined,
      toDate: restrictions.toDate ? restrictions.toDate.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
