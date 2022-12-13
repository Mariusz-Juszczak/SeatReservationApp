import {Component} from '@angular/core';
import {ActivatedRoute, Router} from '@angular/router';
import {Observable} from 'rxjs';
import {NgbModal} from '@ng-bootstrap/ng-bootstrap';
import {EntityArrayResponseType, SeatReservedService} from '../service/seat-reserved.service';
import {SeatReservedComponent} from "./seat-reserved.component";
import {ReservationViewMode} from "../../enumerations/ReservationViewMode.model";

@Component({
  selector: 'app-pending-seat-reserved',
  templateUrl: './seat-reserved.component.html',
})
export class PendingSeatReservedComponent extends SeatReservedComponent {

  constructor(
    protected seatReservedService: SeatReservedService,
    protected activatedRoute: ActivatedRoute,
    public router: Router,
    protected modalService: NgbModal
  ) {
    super(seatReservedService, activatedRoute, router, modalService)
  }

  getReservationViewMode(): string {
    return ReservationViewMode.RESERVATION_MODE_PENDING;
  }

  protected sendRequest(queryObject: any): Observable<EntityArrayResponseType> {
    return this.seatReservedService.getPendingReservedSeats(queryObject);
  }

}
