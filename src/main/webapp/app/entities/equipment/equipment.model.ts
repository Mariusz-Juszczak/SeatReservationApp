import { ISeat } from 'app/entities/seat/seat.model';
import { EquipmentType } from 'app/entities/enumerations/equipment-type.model';

export interface IEquipment {
  id: number;
  name?: string | null;
  type?: EquipmentType | null;
  description?: string | null;
  seat?: Pick<ISeat, 'id'> | null;
}

export type NewEquipment = Omit<IEquipment, 'id'> & { id: null };
