import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ISeatReserved } from '../seat-reserved.model';
import {SeatReservedService} from "../service/seat-reserved.service";

@Component({
  selector: 'app-seat-reserved-detail',
  templateUrl: './seat-reserved-detail.component.html',
})
export class SeatReservedDetailComponent implements OnInit {
  seatReserved: ISeatReserved | null = null;

  constructor(protected activatedRoute: ActivatedRoute,
              protected seatReservedService: SeatReservedService) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ seatReserved }) => {
      this.seatReserved = seatReserved;
    });
  }

  previousState(): void {
    window.history.back();
  }

  cancel(): void {
    if (this.seatReserved) {
      this.seatReservedService.cancel(this.seatReserved.id).subscribe(() => {
        this.previousState();
      });
    }
  }
}
