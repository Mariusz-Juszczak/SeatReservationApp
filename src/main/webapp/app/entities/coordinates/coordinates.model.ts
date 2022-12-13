export interface ICoordinates {
  id: number;
  fromTop?: number | null;
  fromLeft?: number | null;
  angle?: number | null;
}

export type NewCoordinates = Omit<ICoordinates, 'id'> & { id: null };
