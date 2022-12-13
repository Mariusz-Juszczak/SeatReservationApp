import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { NotificationAlertComponent } from '../list/notification-alert.component';
import { NotificationAlertDetailComponent } from '../detail/notification-alert-detail.component';
import { NotificationAlertRoutingResolveService } from './notification-alert-routing-resolve.service';
import { DESC } from 'app/config/navigation.constants';

const notificationAlertRoute: Routes = [
  {
    path: '',
    component: NotificationAlertComponent,
    data: {
      defaultSort: 'createdAt,' + DESC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: NotificationAlertDetailComponent,
    resolve: {
      notificationAlert: NotificationAlertRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  }
];

@NgModule({
  imports: [RouterModule.forChild(notificationAlertRoute)],
  exports: [RouterModule],
})
export class NotificationAlertRoutingModule {}
