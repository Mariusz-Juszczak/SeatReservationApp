import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IRestrictions, NewRestrictions } from '../restrictions.model';

export type PartialUpdateRestrictions = Partial<IRestrictions> & Pick<IRestrictions, 'id'>;

type RestOf<T extends IRestrictions | NewRestrictions> = Omit<T, 'fromDate' | 'toDate'> & {
  fromDate?: string | null;
  toDate?: string | null;
};

export type RestRestrictions = RestOf<IRestrictions>;

export type NewRestRestrictions = RestOf<NewRestrictions>;

export type PartialUpdateRestRestrictions = RestOf<PartialUpdateRestrictions>;

export type EntityResponseType = HttpResponse<IRestrictions>;
export type EntityArrayResponseType = HttpResponse<IRestrictions[]>;

@Injectable({ providedIn: 'root' })
export class RestrictionsService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/restrictions');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(restrictions: NewRestrictions): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(restrictions);
    return this.http
      .post<RestRestrictions>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(restrictions: IRestrictions): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(restrictions);
    return this.http
      .put<RestRestrictions>(`${this.resourceUrl}/${this.getRestrictionsIdentifier(restrictions)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestRestrictions>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  findCurrent(): Observable<EntityResponseType> {
    return this.http
      .get<RestRestrictions>(`${this.resourceUrl}/current`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestRestrictions[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getRestrictionsIdentifier(restrictions: Pick<IRestrictions, 'id'>): number {
    return restrictions.id;
  }

  compareRestrictions(o1: Pick<IRestrictions, 'id'> | null, o2: Pick<IRestrictions, 'id'> | null): boolean {
    return o1 && o2 ? this.getRestrictionsIdentifier(o1) === this.getRestrictionsIdentifier(o2) : o1 === o2;
  }

  addRestrictionsToCollectionIfMissing<Type extends Pick<IRestrictions, 'id'>>(
    restrictionsCollection: Type[],
    ...restrictionsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const restrictions: Type[] = restrictionsToCheck.filter(isPresent);
    if (restrictions.length > 0) {
      const restrictionsCollectionIdentifiers = restrictionsCollection.map(
        restrictionsItem => this.getRestrictionsIdentifier(restrictionsItem)!
      );
      const restrictionsToAdd = restrictions.filter(restrictionsItem => {
        const restrictionsIdentifier = this.getRestrictionsIdentifier(restrictionsItem);
        if (restrictionsCollectionIdentifiers.includes(restrictionsIdentifier)) {
          return false;
        }
        restrictionsCollectionIdentifiers.push(restrictionsIdentifier);
        return true;
      });
      return [...restrictionsToAdd, ...restrictionsCollection];
    }
    return restrictionsCollection;
  }

  protected convertDateFromClient<T extends IRestrictions | NewRestrictions | PartialUpdateRestrictions>(restrictions: T): RestOf<T> {
    return {
      ...restrictions,
      fromDate: restrictions.fromDate?.toJSON() ?? null,
      toDate: restrictions.toDate?.toJSON() ?? null,
    };
  }

  protected convertDateFromServer(restRestrictions: RestRestrictions): IRestrictions {
    return {
      ...restRestrictions,
      fromDate: restRestrictions.fromDate ? dayjs(restRestrictions.fromDate) : undefined,
      toDate: restRestrictions.toDate ? dayjs(restRestrictions.toDate) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestRestrictions>): HttpResponse<IRestrictions> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestRestrictions[]>): HttpResponse<IRestrictions[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
