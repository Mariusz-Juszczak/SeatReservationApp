import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ISeatReserved } from '../seat-reserved.model';
import { SeatReservedService } from '../service/seat-reserved.service';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';

@Component({
  templateUrl: './seat-reserved-delete-dialog.component.html',
})
export class SeatReservedDeleteDialogComponent {
  seatReserved?: ISeatReserved;

  constructor(protected seatReservedService: SeatReservedService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.seatReservedService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
