import dayjs from 'dayjs/esm';
import { IUser } from 'app/entities/user/user.model';
import { ReservationStatus } from 'app/entities/enumerations/reservation-status.model';
import {IEquipment} from "../equipment/equipment.model";

export interface ISeatReserved {
  id: number;
  name?: string | null;
  fromDate?: dayjs.Dayjs | null;
  toDate?: dayjs.Dayjs | null;
  createdDate?: dayjs.Dayjs | null;
  reservationStatus?: ReservationStatus | null;
  equipments?: IEquipment[] | null;
  user?: IUser | null;
  locationId?: number,
  buildingId?: number,
  floorId?: number,
  roomId?: number,
  seatId?: number,
  locationName?: string,
  buildingName?: string,
  floorName?: string,
  roomName?: string,
  seatName?: string,
  buildingAddress?: string
}

export type NewSeatReserved = Omit<ISeatReserved, 'id'> & { id: null };
