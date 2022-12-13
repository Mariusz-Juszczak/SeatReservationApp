import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ISeatReserved } from '../seat-reserved.model';
import { SeatReservedService } from '../service/seat-reserved.service';

@Injectable({ providedIn: 'root' })
export class SeatReservedRoutingResolveService implements Resolve<ISeatReserved | null> {
  constructor(protected service: SeatReservedService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ISeatReserved | null | never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((seatReserved: HttpResponse<ISeatReserved>) => {
          if (seatReserved.body) {
            return of(seatReserved.body);
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
