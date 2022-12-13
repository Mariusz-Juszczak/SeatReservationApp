import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { INotificationAlert } from '../notification-alert.model';
import { NotificationAlertService } from '../service/notification-alert.service';

@Injectable({ providedIn: 'root' })
export class NotificationAlertRoutingResolveService implements Resolve<INotificationAlert | null> {
  constructor(protected service: NotificationAlertService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<INotificationAlert | null | never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((notificationAlert: HttpResponse<INotificationAlert>) => {
          if (notificationAlert.body) {
            return of(notificationAlert.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(null);
  }
}
