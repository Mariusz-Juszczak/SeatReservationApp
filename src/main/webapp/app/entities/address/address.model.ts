export interface IAddress {
  id: number;
  country?: string | null;
  street?: string | null;
  postalCode?: string | null;
  city?: string | null;
  stateProvince?: string | null;
}

export type NewAddress = Omit<IAddress, 'id'> & { id: null };
