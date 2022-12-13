import { Injectable } from '@angular/core';
import {HttpClient, HttpParams, HttpResponse} from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ISeat, NewSeat } from '../seat.model';
import dayjs from 'dayjs/esm';
import {DATE_TIME_FORMAT} from "../../../config/input.constants";
import { FormatMediumDatetimePipe } from '../../../shared/date/format-medium-datetime.pipe';

export type PartialUpdateSeat = Partial<ISeat> & Pick<ISeat, 'id'>;

export type EntityResponseType = HttpResponse<ISeat>;
export type EntityArrayResponseType = HttpResponse<ISeat[]>;

@Injectable({ providedIn: 'root' })
export class SeatService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/seats');
  protected formatMediumDatetimePipe = new FormatMediumDatetimePipe();


  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(seat: NewSeat): Observable<EntityResponseType> {
    return this.http.post<ISeat>(this.resourceUrl, seat, { observe: 'response' });
  }

  update(seat: ISeat): Observable<EntityResponseType> {
    return this.http.put<ISeat>(`${this.resourceUrl}/${this.getSeatIdentifier(seat)}`, seat, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ISeat>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  findByFloorIdAndPeriod(floorId: number, pageableParams: any, fromDate: dayjs.Dayjs, toDate: dayjs.Dayjs): Observable<EntityArrayResponseType> {
    const queryParams = createRequestOption(pageableParams)
      .append("from_date", dayjs(fromDate, DATE_TIME_FORMAT).toJSON())
      .append("to_date", dayjs(toDate, DATE_TIME_FORMAT).toJSON());
    return this.http.get<ISeat[]>(`${this.resourceUrl}/floors/${floorId}`, { params: queryParams, observe: 'response',  });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ISeat[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getSeatIdentifier(seat: Pick<ISeat, 'id'>): number {
    return seat.id;
  }

  compareSeat(o1: Pick<ISeat, 'id'> | null, o2: Pick<ISeat, 'id'> | null): boolean {
    return o1 && o2 ? this.getSeatIdentifier(o1) === this.getSeatIdentifier(o2) : o1 === o2;
  }

  addSeatToCollectionIfMissing<Type extends Pick<ISeat, 'id'>>(
    seatCollection: Type[],
    ...seatsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const seats: Type[] = seatsToCheck.filter(isPresent);
    if (seats.length > 0) {
      const seatCollectionIdentifiers = seatCollection.map(seatItem => this.getSeatIdentifier(seatItem)!);
      const seatsToAdd = seats.filter(seatItem => {
        const seatIdentifier = this.getSeatIdentifier(seatItem);
        if (seatCollectionIdentifiers.includes(seatIdentifier)) {
          return false;
        }
        seatCollectionIdentifiers.push(seatIdentifier);
        return true;
      });
      return [...seatsToAdd, ...seatCollection];
    }
    return seatCollection;
  }
}
