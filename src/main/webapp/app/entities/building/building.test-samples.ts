import { IBuilding, NewBuilding } from './building.model';

export const sampleWithRequiredData: IBuilding = {
  id: 15365,
  name: 'Multi-layered firmware Run',
};

export const sampleWithPartialData: IBuilding = {
  id: 32315,
  name: 'Summit PCI',
};

export const sampleWithFullData: IBuilding = {
  id: 81596,
  name: 'AI',
};

export const sampleWithNewData: NewBuilding = {
  name: 'applications web-enabled',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
