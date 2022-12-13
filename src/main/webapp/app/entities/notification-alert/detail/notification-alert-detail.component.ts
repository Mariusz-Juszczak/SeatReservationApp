import {Component, OnInit} from '@angular/core';
import {ActivatedRoute} from '@angular/router';

import {INotificationAlert} from '../notification-alert.model';
import {NotificationAlertService} from "../service/notification-alert.service";

@Component({
  selector: 'app-notification-alert-detail',
  templateUrl: './notification-alert-detail.component.html',
})
export class NotificationAlertDetailComponent implements OnInit {
  notificationAlert: INotificationAlert | null = null;

  constructor(protected activatedRoute: ActivatedRoute,
              protected notificationAlertService: NotificationAlertService
  ) {
  }

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({notificationAlert}) => {
      this.notificationAlert = notificationAlert;
      if (this.notificationAlert?.id != null) {
        this.notificationAlertService.markAsRead(this.notificationAlert.id).subscribe(() => {
          // do nothing
        });
      }
    });
  }

  previousState(): void {
    window.history.back();
  }
}
