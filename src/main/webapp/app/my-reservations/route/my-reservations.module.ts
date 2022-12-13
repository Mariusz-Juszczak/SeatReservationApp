import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import {SeatReservedComponent} from "../../entities/seat-reserved/list/seat-reserved.component";
import {ReservationViewMode} from "../../entities/enumerations/ReservationViewMode.model";
import {SeatReservedDetailComponent} from "../../entities/seat-reserved/detail/seat-reserved-detail.component";
import {
  SeatReservedRoutingResolveService
} from "../../entities/seat-reserved/route/seat-reserved-routing-resolve.service";

const myReservationsRoute: Routes = [
  {
    path: '',
    component: SeatReservedComponent,
    data: {
      defaultSort: 'id,' + ASC,
      reservationsMode: ReservationViewMode.RESERVATION_MODE_USER,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: SeatReservedDetailComponent,
    resolve: {
      seatReserved: SeatReservedRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(myReservationsRoute)],
  exports: [RouterModule],
})
export class MyReservationsRoutingModule {}
