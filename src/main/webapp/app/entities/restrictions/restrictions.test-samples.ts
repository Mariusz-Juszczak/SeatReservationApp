import dayjs from 'dayjs/esm';

import { IRestrictions, NewRestrictions } from './restrictions.model';

export const sampleWithRequiredData: IRestrictions = {
  id: 16243,
  name: 'Account Security transmit',
  fromDate: dayjs('2022-09-05T07:59'),
};

export const sampleWithPartialData: IRestrictions = {
  id: 59624,
  name: 'Officer Rustic',
  fromDate: dayjs('2022-09-04T18:11'),
};

export const sampleWithFullData: IRestrictions = {
  id: 32511,
  name: 'maroon Sports Mountains',
  fromDate: dayjs('2022-09-04T21:12'),
  toDate: dayjs('2022-09-04T13:40'),
};

export const sampleWithNewData: NewRestrictions = {
  name: 'Liaison primary Savings',
  fromDate: dayjs('2022-09-04T19:10'),
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
