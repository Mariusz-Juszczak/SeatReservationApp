import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IEquipment } from '../equipment.model';
import { EquipmentService } from '../service/equipment.service';

@Injectable({ providedIn: 'root' })
export class EquipmentRoutingResolveService implements Resolve<IEquipment | null> {
  constructor(protected service: EquipmentService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IEquipment | null | never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((equipment: HttpResponse<IEquipment>) => {
          if (equipment.body) {
            return of(equipment.body);
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
