import { AvailabilityStatus } from 'app/entities/enumerations/availability-status.model';

import { ISeat, NewSeat } from './seat.model';

export const sampleWithRequiredData: ISeat = {
  id: 43988,
  seatNumber: 94068,
  name: 'Buckinghamshire',
  status: AvailabilityStatus['FREE'],
};

export const sampleWithPartialData: ISeat = {
  id: 79692,
  seatNumber: 80908,
  name: 'Assistant haptic pink',
  status: AvailabilityStatus['FREE'],
};

export const sampleWithFullData: ISeat = {
  id: 56108,
  seatNumber: 49967,
  name: 'Home Developer',
  status: AvailabilityStatus['OCCUPIED'],
};

export const sampleWithNewData: NewSeat = {
  seatNumber: 6086,
  name: 'ivory connect',
  status: AvailabilityStatus['OCCUPIED'],
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
