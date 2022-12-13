import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';
import {Authority} from "../config/authority.constants";

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'location',
        data: {
          pageTitle: 'seatReservationApp.location.home.title',
          authorities: [Authority.ADMIN, Authority.LOCATION_ADMIN],
        },
        loadChildren: () => import('./location/location.module').then(m => m.LocationModule),
      },
      {
        path: 'restrictions',
        data: {
          pageTitle: 'seatReservationApp.restrictions.home.title',
          authorities: [Authority.ADMIN, Authority.LOCATION_ADMIN],
        },
        loadChildren: () => import('./restrictions/restrictions.module').then(m => m.RestrictionsModule),
      },
      {
        path: 'building',
        data: {
          pageTitle: 'seatReservationApp.building.home.title',
          authorities: [Authority.ADMIN, Authority.LOCATION_ADMIN],
        },
        loadChildren: () => import('./building/building.module').then(m => m.BuildingModule),
      },
      {
        path: 'floor',
        data: {
          pageTitle: 'seatReservationApp.floor.home.title',
          authorities: [Authority.ADMIN, Authority.LOCATION_ADMIN],
        },
        loadChildren: () => import('./floor/floor.module').then(m => m.FloorModule),
      },
      {
        path: 'room',
        data: {
          pageTitle: 'seatReservationApp.room.home.title',
          authorities: [Authority.ADMIN, Authority.LOCATION_ADMIN],
        },
        loadChildren: () => import('./room/room.module').then(m => m.RoomModule),
      },
      {
        path: 'seat',
        data: {
          pageTitle: 'seatReservationApp.seat.home.title',
          authorities: [Authority.ADMIN, Authority.LOCATION_ADMIN],
        },
        loadChildren: () => import('./seat/seat.module').then(m => m.SeatModule),
      },
      {
        path: 'equipment',
        data: {
          pageTitle: 'seatReservationApp.equipment.home.title',
          authorities: [Authority.ADMIN, Authority.LOCATION_ADMIN],
        },
        loadChildren: () => import('./equipment/equipment.module').then(m => m.EquipmentModule),
      },
      {
        path: 'seat-reserved',
        data: {
          pageTitle: 'seatReservationApp.seatReserved.home.title',
          authorities: [Authority.ADMIN, Authority.LOCATION_ADMIN],
        },
        loadChildren: () => import('./seat-reserved/seat-reserved.module').then(m => m.SeatReservedModule),
      },
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ]),
  ],
})
export class EntityRoutingModule {}
