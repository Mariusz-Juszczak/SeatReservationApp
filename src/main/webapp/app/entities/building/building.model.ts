import {IAddress, NewAddress} from 'app/entities/address/address.model';
import { ILocation } from 'app/entities/location/location.model';

export interface IBuilding {
  id: number;
  name?: string | null;
  address?: IAddress | NewAddress;
  location?: ILocation | null;
}

export type NewBuilding = Omit<IBuilding, 'id'> & { id: null };
