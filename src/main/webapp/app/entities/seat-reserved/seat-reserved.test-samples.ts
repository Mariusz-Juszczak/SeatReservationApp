import dayjs from 'dayjs/esm';

import { ReservationStatus } from 'app/entities/enumerations/reservation-status.model';

import { ISeatReserved, NewSeatReserved } from './seat-reserved.model';

export const sampleWithRequiredData: ISeatReserved = {
  id: 9502,
  name: 'Palladium',
  fromDate: dayjs('2022-09-05T02:27'),
  createdDate: dayjs('2022-09-04T23:46'),
  reservationStatus: ReservationStatus['CANCELLED'],
};

export const sampleWithPartialData: ISeatReserved = {
  id: 44172,
  name: 'infrastructures auxiliary deliverables',
  fromDate: dayjs('2022-09-04T09:02'),
  createdDate: dayjs('2022-09-04T08:50'),
  reservationStatus: ReservationStatus['ACTIVE'],
};

export const sampleWithFullData: ISeatReserved = {
  id: 2757,
  name: 'copying Cheese',
  fromDate: dayjs('2022-09-05T03:00'),
  toDate: dayjs('2022-09-05T01:19'),
  createdDate: dayjs('2022-09-04T11:15'),
  reservationStatus: ReservationStatus['CANCELLED'],
};

export const sampleWithNewData: NewSeatReserved = {
  name: 'Associate Bermuda',
  fromDate: dayjs('2022-09-04T11:18'),
  createdDate: dayjs('2022-09-05T04:49'),
  reservationStatus: ReservationStatus['ACTIVE'],
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
