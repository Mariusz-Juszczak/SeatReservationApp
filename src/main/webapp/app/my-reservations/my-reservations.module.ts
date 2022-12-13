import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { MyReservationsRoutingModule } from "./route/my-reservations.module";

@NgModule({
  imports: [SharedModule, MyReservationsRoutingModule],
})
export class MyReservationsModule {}
