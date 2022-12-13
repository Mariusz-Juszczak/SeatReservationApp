import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IBuilding, NewBuilding } from '../building.model';

export type PartialUpdateBuilding = Partial<IBuilding> & Pick<IBuilding, 'id'>;

export type EntityResponseType = HttpResponse<IBuilding>;
export type EntityArrayResponseType = HttpResponse<IBuilding[]>;

@Injectable({ providedIn: 'root' })
export class BuildingService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/buildings');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(building: NewBuilding): Observable<EntityResponseType> {
    return this.http.post<IBuilding>(this.resourceUrl, building, { observe: 'response' });
  }

  update(building: IBuilding): Observable<EntityResponseType> {
    return this.http.put<IBuilding>(`${this.resourceUrl}/${this.getBuildingIdentifier(building)}`, building, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IBuilding>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  findByLocationId(locationId: number, page?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(page);
    return this.http.get<IBuilding[]>(`${this.resourceUrl}/locations/${locationId}`, { params: options, observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IBuilding[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getBuildingIdentifier(building: Pick<IBuilding, 'id'>): number {
    return building.id;
  }

  compareBuilding(o1: Pick<IBuilding, 'id'> | null, o2: Pick<IBuilding, 'id'> | null): boolean {
    return o1 && o2 ? this.getBuildingIdentifier(o1) === this.getBuildingIdentifier(o2) : o1 === o2;
  }

  addBuildingToCollectionIfMissing<Type extends Pick<IBuilding, 'id'>>(
    buildingCollection: Type[],
    ...buildingsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const buildings: Type[] = buildingsToCheck.filter(isPresent);
    if (buildings.length > 0) {
      const buildingCollectionIdentifiers = buildingCollection.map(buildingItem => this.getBuildingIdentifier(buildingItem)!);
      const buildingsToAdd = buildings.filter(buildingItem => {
        const buildingIdentifier = this.getBuildingIdentifier(buildingItem);
        if (buildingCollectionIdentifiers.includes(buildingIdentifier)) {
          return false;
        }
        buildingCollectionIdentifiers.push(buildingIdentifier);
        return true;
      });
      return [...buildingsToAdd, ...buildingCollection];
    }
    return buildingCollection;
  }
}
