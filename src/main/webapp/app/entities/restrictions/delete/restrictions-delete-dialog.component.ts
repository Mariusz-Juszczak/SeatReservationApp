import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IRestrictions } from '../restrictions.model';
import { RestrictionsService } from '../service/restrictions.service';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';

@Component({
  templateUrl: './restrictions-delete-dialog.component.html',
})
export class RestrictionsDeleteDialogComponent {
  restrictions?: IRestrictions;

  constructor(protected restrictionsService: RestrictionsService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.restrictionsService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
