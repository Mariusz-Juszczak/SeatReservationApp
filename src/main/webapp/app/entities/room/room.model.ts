import { ICoordinates, NewCoordinates } from 'app/entities/coordinates/coordinates.model';
import { IDimensions, NewDimensions } from 'app/entities/dimensions/dimensions.model';
import { IFloor } from 'app/entities/floor/floor.model';
import { AvailabilityStatus } from 'app/entities/enumerations/availability-status.model';
import { RoomType } from 'app/entities/enumerations/room-type.model';

export interface IRoom {
  id: number;
  name?: string | null;
  map?: string | null;
  mapContentType?: string | null;
  status?: AvailabilityStatus | null;
  roomType?: RoomType | null;
  coordinates?: ICoordinates | NewCoordinates;
  dimensions?: IDimensions | NewDimensions;
  floor?: IFloor | null;
}

export type NewRoom = Omit<IRoom, 'id'> & { id: null };
