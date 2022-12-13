import {Component, Input} from '@angular/core';
import {ISeatReserved} from "../../entities/seat-reserved/seat-reserved.model";

@Component({
  selector: 'app-reserved-view',
  templateUrl: './reservation-view.component.html',
  styleUrls: ['./reservation-view.component.scss'],
})
export class ReservationViewComponent {

  @Input() reservedSeats?: ISeatReserved[];

}
