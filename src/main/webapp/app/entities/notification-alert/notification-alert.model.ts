import dayjs from 'dayjs/esm';
import { NotificationPriority } from 'app/entities/enumerations/notification-priority.model';
import { NotificationState } from 'app/entities/enumerations/notification-state.model';

export interface INotificationAlert {
  id: number;
  title?: string | null;
  message?: string | null;
  notificationPriority?: NotificationPriority | null;
  notificationState?: NotificationState | null;
  createdAt?: dayjs.Dayjs | null;
  userId?: number | null;
}

export type NewNotificationAlert = Omit<INotificationAlert, 'id'> & { id: null };
