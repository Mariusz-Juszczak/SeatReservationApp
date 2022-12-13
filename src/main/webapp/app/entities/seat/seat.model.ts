import { ICoordinates, NewCoordinates } from 'app/entities/coordinates/coordinates.model';
import { IDimensions, NewDimensions } from 'app/entities/dimensions/dimensions.model';
import { IRoom } from 'app/entities/room/room.model';
import { AvailabilityStatus } from 'app/entities/enumerations/availability-status.model';
import {SeatStatus} from "../enumerations/seat-status.model";
import {IEquipment} from "../equipment/equipment.model";

export interface ISeat {
  id: number;
  seatNumber?: number | null;
  name?: string | null;
  availabilityStatus?: AvailabilityStatus | null;
  status?: SeatStatus | null;
  coordinates?: ICoordinates | NewCoordinates;
  dimensions?: IDimensions | NewDimensions;
  room?: IRoom | null;
  equipments?: IEquipment[] | null;
}

export type NewSeat = Omit<ISeat, 'id'> & { id: null };
