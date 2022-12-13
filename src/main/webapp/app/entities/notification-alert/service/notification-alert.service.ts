import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { INotificationAlert, NewNotificationAlert } from '../notification-alert.model';

export type PartialUpdateNotificationAlert = Partial<INotificationAlert> & Pick<INotificationAlert, 'id'>;

type RestOf<T extends INotificationAlert | NewNotificationAlert> = Omit<T, 'createdAt'> & {
  createdAt?: string | null;
};

export type RestNotificationAlert = RestOf<INotificationAlert>;

export type NewRestNotificationAlert = RestOf<NewNotificationAlert>;

export type PartialUpdateRestNotificationAlert = RestOf<PartialUpdateNotificationAlert>;

export type EntityResponseType = HttpResponse<INotificationAlert>;
export type EntityArrayResponseType = HttpResponse<INotificationAlert[]>;

@Injectable({ providedIn: 'root' })
export class NotificationAlertService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/notification-alerts');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(notificationAlert: NewNotificationAlert): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(notificationAlert);
    return this.http
      .post<RestNotificationAlert>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(notificationAlert: INotificationAlert): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(notificationAlert);
    return this.http
      .put<RestNotificationAlert>(`${this.resourceUrl}/${this.getNotificationAlertIdentifier(notificationAlert)}`, copy, {
        observe: 'response',
      })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestNotificationAlert>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  findNewNotifications(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestNotificationAlert[]>(`${this.resourceUrl}/new`, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  markAsRead(id: number): Observable<{}> {
    return this.http.patch(`${this.resourceUrl}/read/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestNotificationAlert[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getNotificationAlertIdentifier(notificationAlert: Pick<INotificationAlert, 'id'>): number {
    return notificationAlert.id;
  }

  compareNotificationAlert(o1: Pick<INotificationAlert, 'id'> | null, o2: Pick<INotificationAlert, 'id'> | null): boolean {
    return o1 && o2 ? this.getNotificationAlertIdentifier(o1) === this.getNotificationAlertIdentifier(o2) : o1 === o2;
  }

  addNotificationAlertToCollectionIfMissing<Type extends Pick<INotificationAlert, 'id'>>(
    notificationAlertCollection: Type[],
    ...notificationAlertsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const notificationAlerts: Type[] = notificationAlertsToCheck.filter(isPresent);
    if (notificationAlerts.length > 0) {
      const notificationAlertCollectionIdentifiers = notificationAlertCollection.map(
        notificationAlertItem => this.getNotificationAlertIdentifier(notificationAlertItem)!
      );
      const notificationAlertsToAdd = notificationAlerts.filter(notificationAlertItem => {
        const notificationAlertIdentifier = this.getNotificationAlertIdentifier(notificationAlertItem);
        if (notificationAlertCollectionIdentifiers.includes(notificationAlertIdentifier)) {
          return false;
        }
        notificationAlertCollectionIdentifiers.push(notificationAlertIdentifier);
        return true;
      });
      return [...notificationAlertsToAdd, ...notificationAlertCollection];
    }
    return notificationAlertCollection;
  }

  protected convertDateFromClient<T extends INotificationAlert | NewNotificationAlert | PartialUpdateNotificationAlert>(
    notificationAlert: T
  ): RestOf<T> {
    return {
      ...notificationAlert,
      createdAt: notificationAlert.createdAt?.toJSON() ?? null,
    };
  }

  protected convertDateFromServer(restNotificationAlert: RestNotificationAlert): INotificationAlert {
    return {
      ...restNotificationAlert,
      createdAt: restNotificationAlert.createdAt ? dayjs(restNotificationAlert.createdAt) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestNotificationAlert>): HttpResponse<INotificationAlert> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestNotificationAlert[]>): HttpResponse<INotificationAlert[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
