import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { RestrictionsComponent } from '../list/restrictions.component';
import { RestrictionsDetailComponent } from '../detail/restrictions-detail.component';
import { RestrictionsUpdateComponent } from '../update/restrictions-update.component';
import { RestrictionsRoutingResolveService } from './restrictions-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';

const restrictionsRoute: Routes = [
  {
    path: '',
    component: RestrictionsComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: RestrictionsDetailComponent,
    resolve: {
      restrictions: RestrictionsRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: RestrictionsUpdateComponent,
    resolve: {
      restrictions: RestrictionsRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(restrictionsRoute)],
  exports: [RouterModule],
})
export class RestrictionsRoutingModule {}
