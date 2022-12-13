import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IRoom, NewRoom } from '../room.model';
import { DimensionsFormGroup, DimensionsFormService } from "../../dimensions/update/dimensions-form.service";
import { CoordinatesFormGroup, CoordinatesFormService } from "../../coordinates/update/coordinates-form.service";

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IRoom for edit and NewRoomFormGroupInput for create.
 */
type RoomFormGroupInput = IRoom | PartialWithRequiredKeyOf<NewRoom>;

type RoomFormDefaults = Pick<NewRoom, 'id'>;

type RoomFormGroupContent = {
  id: FormControl<IRoom['id'] | NewRoom['id']>;
  name: FormControl<IRoom['name']>;
  map: FormControl<IRoom['map']>;
  mapContentType: FormControl<IRoom['mapContentType']>;
  status: FormControl<IRoom['status']>;
  roomType: FormControl<IRoom['roomType']>;
  coordinates: CoordinatesFormGroup;
  dimensions: DimensionsFormGroup;
  floor: FormControl<IRoom['floor']>;
};

export type RoomFormGroup = FormGroup<RoomFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class RoomFormService {

  constructor(
    protected dimensionsFormService: DimensionsFormService,
    protected coordinatesFormService: CoordinatesFormService,
  ) {}

  createRoomFormGroup(room: RoomFormGroupInput = { id: null }): RoomFormGroup {
    const roomRawValue = {
      ...this.getFormDefaults(),
      ...room,
    };
    return new FormGroup<RoomFormGroupContent>({
      id: new FormControl(
        { value: roomRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      name: new FormControl(roomRawValue.name, {
        validators: [Validators.required],
      }),
      map: new FormControl(roomRawValue.map),
      mapContentType: new FormControl(roomRawValue.mapContentType),
      status: new FormControl(roomRawValue.status, {
        validators: [Validators.required],
      }),
      roomType: new FormControl(roomRawValue.roomType, {
        validators: [Validators.required],
      }),
      coordinates: this.coordinatesFormService.createCoordinatesFormGroup(),
      dimensions: this.dimensionsFormService.createDimensionsFormGroup(),
      floor: new FormControl(roomRawValue.floor),
    });
  }

  getRoom(form: RoomFormGroup): IRoom | NewRoom {
    return form.getRawValue() as IRoom | NewRoom;
  }

  resetForm(form: RoomFormGroup, room: RoomFormGroupInput): void {
    if (room.dimensions == null) {
      room.dimensions = this.dimensionsFormService.getFormDefaults();
    }
    if (room.coordinates == null) {
      room.coordinates = this.coordinatesFormService.getFormDefaults();
    }
    const roomRawValue = { ...this.getFormDefaults(), ...room };
    form.reset(
      {
        ...roomRawValue,
        id: { value: roomRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): RoomFormDefaults {
    return {
      id: null,
    };
  }
}
