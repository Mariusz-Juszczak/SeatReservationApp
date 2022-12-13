import dayjs from 'dayjs/esm';

import { NotificationPriority } from 'app/entities/enumerations/notification-priority.model';
import { NotificationState } from 'app/entities/enumerations/notification-state.model';

import { INotificationAlert, NewNotificationAlert } from './notification-alert.model';

export const sampleWithRequiredData: INotificationAlert = {
  id: 79118,
};

export const sampleWithPartialData: INotificationAlert = {
  id: 97987,
  title: 'Sausages Tennessee Cambridgeshire',
  message: 'product Liberia virtual',
  notificationState: NotificationState['NEW'],
};

export const sampleWithFullData: INotificationAlert = {
  id: 48903,
  title: 'redundant',
  message: 'schemas disintermediate',
  notificationPriority: NotificationPriority['HIGH'],
  notificationState: NotificationState['READ'],
  createdAt: dayjs('2022-09-07T13:14'),
  userId: 4008,
};

export const sampleWithNewData: NewNotificationAlert = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
