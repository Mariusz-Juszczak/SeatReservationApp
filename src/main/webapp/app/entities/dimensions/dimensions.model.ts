export interface IDimensions {
  id: number;
  height?: number | null;
  width?: number | null;
}

export type NewDimensions = Omit<IDimensions, 'id'> & { id: null };
