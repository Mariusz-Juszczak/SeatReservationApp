import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ISeatReserved, NewSeatReserved } from '../seat-reserved.model';

export type PartialUpdateSeatReserved = Partial<ISeatReserved> & Pick<ISeatReserved, 'id'>;

type RestOf<T extends ISeatReserved | NewSeatReserved> = Omit<T, 'fromDate' | 'toDate' | 'createdDate'> & {
  fromDate?: string | null;
  toDate?: string | null;
  createdDate?: string | null;
};

export type RestSeatReserved = RestOf<ISeatReserved>;

export type NewRestSeatReserved = RestOf<NewSeatReserved>;

export type PartialUpdateRestSeatReserved = RestOf<PartialUpdateSeatReserved>;

export type EntityResponseType = HttpResponse<ISeatReserved>;
export type EntityArrayResponseType = HttpResponse<ISeatReserved[]>;

@Injectable({ providedIn: 'root' })
export class SeatReservedService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/reserved-seats');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(seatReserved: NewSeatReserved): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(seatReserved);
    return this.http
      .post<RestSeatReserved>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(seatReserved: ISeatReserved): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(seatReserved);
    return this.http
      .put<RestSeatReserved>(`${this.resourceUrl}/${this.getSeatReservedIdentifier(seatReserved)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestSeatReserved>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  findUpcoming(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestSeatReserved[]>(`${this.resourceUrl}/upcoming`, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  findRecent(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestSeatReserved[]>(`${this.resourceUrl}/recent`, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestSeatReserved[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  getAllReservedSeats(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestSeatReserved[]>(`${this.resourceUrl}/all`, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  getPendingReservedSeats(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestSeatReserved[]>(`${this.resourceUrl}/pending`, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  approve(id: number): Observable<{}> {
    return this.http.patch(`${this.resourceUrl}/approve/${id}`, { observe: 'response' });
  }

  cancel(id: number): Observable<{}> {
    return this.http.patch(`${this.resourceUrl}/cancel/${id}`, { observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getSeatReservedIdentifier(seatReserved: Pick<ISeatReserved, 'id'>): number {
    return seatReserved.id;
  }

  compareSeatReserved(o1: Pick<ISeatReserved, 'id'> | null, o2: Pick<ISeatReserved, 'id'> | null): boolean {
    return o1 && o2 ? this.getSeatReservedIdentifier(o1) === this.getSeatReservedIdentifier(o2) : o1 === o2;
  }

  addSeatReservedToCollectionIfMissing<Type extends Pick<ISeatReserved, 'id'>>(
    seatReservedCollection: Type[],
    ...seatReservedsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const seatReserveds: Type[] = seatReservedsToCheck.filter(isPresent);
    if (seatReserveds.length > 0) {
      const seatReservedCollectionIdentifiers = seatReservedCollection.map(
        seatReservedItem => this.getSeatReservedIdentifier(seatReservedItem)!
      );
      const seatReservedsToAdd = seatReserveds.filter(seatReservedItem => {
        const seatReservedIdentifier = this.getSeatReservedIdentifier(seatReservedItem);
        if (seatReservedCollectionIdentifiers.includes(seatReservedIdentifier)) {
          return false;
        }
        seatReservedCollectionIdentifiers.push(seatReservedIdentifier);
        return true;
      });
      return [...seatReservedsToAdd, ...seatReservedCollection];
    }
    return seatReservedCollection;
  }

  protected convertDateFromClient<T extends ISeatReserved | NewSeatReserved | PartialUpdateSeatReserved>(seatReserved: T): RestOf<T> {
    return {
      ...seatReserved,
      fromDate: seatReserved.fromDate?.toJSON() ?? null,
      toDate: seatReserved.toDate?.toJSON() ?? null,
      createdDate: seatReserved.createdDate?.toJSON() ?? null,
    };
  }

  protected convertDateFromServer(restSeatReserved: RestSeatReserved): ISeatReserved {
    return {
      ...restSeatReserved,
      fromDate: restSeatReserved.fromDate ? dayjs(restSeatReserved.fromDate) : undefined,
      toDate: restSeatReserved.toDate ? dayjs(restSeatReserved.toDate) : undefined,
      createdDate: restSeatReserved.createdDate ? dayjs(restSeatReserved.createdDate) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestSeatReserved>): HttpResponse<ISeatReserved> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestSeatReserved[]>): HttpResponse<ISeatReserved[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
