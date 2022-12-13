import { EquipmentType } from 'app/entities/enumerations/equipment-type.model';

import { IEquipment, NewEquipment } from './equipment.model';

export const sampleWithRequiredData: IEquipment = {
  id: 1489,
  name: 'workforce',
  type: EquipmentType['NOTEBOOK'],
};

export const sampleWithPartialData: IEquipment = {
  id: 18581,
  name: 'Buckinghamshire e-business',
  type: EquipmentType['NOTEBOOK'],
};

export const sampleWithFullData: IEquipment = {
  id: 31664,
  name: 'attitude Health',
  type: EquipmentType['DOCKING_STATION'],
  description: 'synthesizing Dynamic Berkshire',
};

export const sampleWithNewData: NewEquipment = {
  name: '(EURCO)',
  type: EquipmentType['HEADPHONES'],
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
