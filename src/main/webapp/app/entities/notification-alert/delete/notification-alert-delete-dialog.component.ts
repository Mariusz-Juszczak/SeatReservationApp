import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { INotificationAlert } from '../notification-alert.model';
import { NotificationAlertService } from '../service/notification-alert.service';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';

@Component({
  templateUrl: './notification-alert-delete-dialog.component.html',
})
export class NotificationAlertDeleteDialogComponent {
  notificationAlert?: INotificationAlert;

  constructor(protected notificationAlertService: NotificationAlertService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.notificationAlertService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
