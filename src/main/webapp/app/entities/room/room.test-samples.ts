import { AvailabilityStatus } from 'app/entities/enumerations/availability-status.model';
import { RoomType } from 'app/entities/enumerations/room-type.model';

import { IRoom, NewRoom } from './room.model';

export const sampleWithRequiredData: IRoom = {
  id: 302,
  name: 'repurpose RSS',
  status: AvailabilityStatus['FREE'],
  roomType: RoomType['SHARED'],
};

export const sampleWithPartialData: IRoom = {
  id: 11964,
  name: 'Account Future Salad',
  status: AvailabilityStatus['UNAVAILABLE'],
  roomType: RoomType['CONFERENCE'],
};

export const sampleWithFullData: IRoom = {
  id: 25559,
  name: 'Triple-buffered Barbados Metical',
  map: '../fake-data/blob/hipster.png',
  mapContentType: 'unknown',
  status: AvailabilityStatus['FREE'],
  roomType: RoomType['CONFERENCE'],
};

export const sampleWithNewData: NewRoom = {
  name: 'Practical',
  status: AvailabilityStatus['RESTRICTED'],
  roomType: RoomType['SHARED'],
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
