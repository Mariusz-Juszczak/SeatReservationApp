import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IRestrictions } from '../restrictions.model';
import { RestrictionsService } from '../service/restrictions.service';

@Injectable({ providedIn: 'root' })
export class RestrictionsRoutingResolveService implements Resolve<IRestrictions | null> {
  constructor(protected service: RestrictionsService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IRestrictions | null | never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((restrictions: HttpResponse<IRestrictions>) => {
          if (restrictions.body) {
            return of(restrictions.body);
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
