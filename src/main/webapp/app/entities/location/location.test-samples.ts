import { ILocation, NewLocation } from './location.model';

export const sampleWithRequiredData: ILocation = {
  id: 91847,
  name: 'parsing',
};

export const sampleWithPartialData: ILocation = {
  id: 93501,
  name: 'payment',
};

export const sampleWithFullData: ILocation = {
  id: 128,
  name: 'monetize indigo',
};

export const sampleWithNewData: NewLocation = {
  name: 'International Computer',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
