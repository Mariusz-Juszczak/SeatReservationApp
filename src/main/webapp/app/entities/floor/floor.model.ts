import { IDimensions, NewDimensions } from 'app/entities/dimensions/dimensions.model';
import { IBuilding } from 'app/entities/building/building.model';

export interface IFloor {
  id: number;
  name?: string | null;
  number?: number | null;
  map?: string | null;
  mapContentType?: string | null;
  dimensions?: IDimensions | NewDimensions;
  building?: IBuilding | null;
}

export type NewFloor = Omit<IFloor, 'id'> & { id: null };
