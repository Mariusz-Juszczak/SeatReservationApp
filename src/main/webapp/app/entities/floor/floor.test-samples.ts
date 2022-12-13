import { IFloor, NewFloor } from './floor.model';

export const sampleWithRequiredData: IFloor = {
  id: 27504,
  name: 'firewall',
  number: 22519,
};

export const sampleWithPartialData: IFloor = {
  id: 49385,
  name: 'iterate Cheese cross-media',
  number: 81768,
  map: '../fake-data/blob/hipster.png',
  mapContentType: 'unknown',
};

export const sampleWithFullData: IFloor = {
  id: 1661,
  name: 'systematic strategy array',
  number: 56655,
  map: '../fake-data/blob/hipster.png',
  mapContentType: 'unknown',
};

export const sampleWithNewData: NewFloor = {
  name: 'SDD Synergistic Designer',
  number: 45774,
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
