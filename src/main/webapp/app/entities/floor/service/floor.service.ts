import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IFloor, NewFloor } from '../floor.model';
import {IBuilding} from "../../building/building.model";

export type PartialUpdateFloor = Partial<IFloor> & Pick<IFloor, 'id'>;

export type EntityResponseType = HttpResponse<IFloor>;
export type EntityArrayResponseType = HttpResponse<IFloor[]>;

@Injectable({ providedIn: 'root' })
export class FloorService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/floors');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(floor: NewFloor): Observable<EntityResponseType> {
    return this.http.post<IFloor>(this.resourceUrl, floor, { observe: 'response' });
  }

  update(floor: IFloor): Observable<EntityResponseType> {
    return this.http.put<IFloor>(`${this.resourceUrl}/${this.getFloorIdentifier(floor)}`, floor, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IFloor>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  findByBuildingId(buildingId: number, page?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(page);
    return this.http.get<IFloor[]>(`${this.resourceUrl}/buildings/${buildingId}`, { params: options, observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IFloor[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getFloorIdentifier(floor: Pick<IFloor, 'id'>): number {
    return floor.id;
  }

  compareFloor(o1: Pick<IFloor, 'id'> | null, o2: Pick<IFloor, 'id'> | null): boolean {
    return o1 && o2 ? this.getFloorIdentifier(o1) === this.getFloorIdentifier(o2) : o1 === o2;
  }

  addFloorToCollectionIfMissing<Type extends Pick<IFloor, 'id'>>(
    floorCollection: Type[],
    ...floorsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const floors: Type[] = floorsToCheck.filter(isPresent);
    if (floors.length > 0) {
      const floorCollectionIdentifiers = floorCollection.map(floorItem => this.getFloorIdentifier(floorItem)!);
      const floorsToAdd = floors.filter(floorItem => {
        const floorIdentifier = this.getFloorIdentifier(floorItem);
        if (floorCollectionIdentifiers.includes(floorIdentifier)) {
          return false;
        }
        floorCollectionIdentifiers.push(floorIdentifier);
        return true;
      });
      return [...floorsToAdd, ...floorCollection];
    }
    return floorCollection;
  }
}
