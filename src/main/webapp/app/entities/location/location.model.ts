import { IUser } from 'app/entities/user/user.model';

export interface ILocation {
  id: number;
  name?: string | null;
  locationAdmins?: IUser[] | null;
}

export type NewLocation = Omit<ILocation, 'id'> & { id: null };
