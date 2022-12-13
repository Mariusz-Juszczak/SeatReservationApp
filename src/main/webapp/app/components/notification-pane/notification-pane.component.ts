import {Component, EventEmitter, Input, Output} from '@angular/core';
import {INotificationAlert} from "../../entities/notification-alert/notification-alert.model";
import {NotificationAlertService} from "../../entities/notification-alert/service/notification-alert.service";

@Component({
  selector: 'app-notification-pane',
  templateUrl: './notification-pane.component.html',
  styleUrls: ['./notification-pane.component.scss'],
})
export class NotificationPaneComponent {

  @Input() notification?: INotificationAlert | undefined | null;
  @Output() closeNotification = new EventEmitter<void>();

  constructor(
    protected notificationAlertService: NotificationAlertService,
  ) {
  }

  close(): void {
    if (this.notification?.id != null) {
      this.notificationAlertService.markAsRead(this.notification.id).subscribe(() => {
          this.closeNotification.emit();
      });
    }
    this.notification = null;
  }

}
