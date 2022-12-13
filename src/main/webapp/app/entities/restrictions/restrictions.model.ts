import dayjs from 'dayjs/esm';
import { RestrictionType } from 'app/entities/enumerations/restriction-type.model';

export interface IRestrictions {
  id: number;
  name?: string | null;
  fromDate?: dayjs.Dayjs | null;
  toDate?: dayjs.Dayjs | null;
  restrictionType?: RestrictionType | null;
  restrictionValue?: number | null;
}

export type NewRestrictions = Omit<IRestrictions, 'id'> & { id: null };
