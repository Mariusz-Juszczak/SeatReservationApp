import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IEquipment, NewEquipment } from '../equipment.model';

export type PartialUpdateEquipment = Partial<IEquipment> & Pick<IEquipment, 'id'>;

export type EntityResponseType = HttpResponse<IEquipment>;
export type EntityArrayResponseType = HttpResponse<IEquipment[]>;

@Injectable({ providedIn: 'root' })
export class EquipmentService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/equipment');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(equipment: NewEquipment): Observable<EntityResponseType> {
    return this.http.post<IEquipment>(this.resourceUrl, equipment, { observe: 'response' });
  }

  update(equipment: IEquipment): Observable<EntityResponseType> {
    return this.http.put<IEquipment>(`${this.resourceUrl}/${this.getEquipmentIdentifier(equipment)}`, equipment, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IEquipment>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IEquipment[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getEquipmentIdentifier(equipment: Pick<IEquipment, 'id'>): number {
    return equipment.id;
  }

  compareEquipment(o1: Pick<IEquipment, 'id'> | null, o2: Pick<IEquipment, 'id'> | null): boolean {
    return o1 && o2 ? this.getEquipmentIdentifier(o1) === this.getEquipmentIdentifier(o2) : o1 === o2;
  }

  addEquipmentToCollectionIfMissing<Type extends Pick<IEquipment, 'id'>>(
    equipmentCollection: Type[],
    ...equipmentToCheck: (Type | null | undefined)[]
  ): Type[] {
    const equipment: Type[] = equipmentToCheck.filter(isPresent);
    if (equipment.length > 0) {
      const equipmentCollectionIdentifiers = equipmentCollection.map(equipmentItem => this.getEquipmentIdentifier(equipmentItem)!);
      const equipmentToAdd = equipment.filter(equipmentItem => {
        const equipmentIdentifier = this.getEquipmentIdentifier(equipmentItem);
        if (equipmentCollectionIdentifiers.includes(equipmentIdentifier)) {
          return false;
        }
        equipmentCollectionIdentifiers.push(equipmentIdentifier);
        return true;
      });
      return [...equipmentToAdd, ...equipmentCollection];
    }
    return equipmentCollection;
  }
}
