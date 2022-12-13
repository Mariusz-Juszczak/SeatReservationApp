import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { NotificationAlertComponent } from './list/notification-alert.component';
import { NotificationAlertDetailComponent } from './detail/notification-alert-detail.component';
import { NotificationAlertDeleteDialogComponent } from './delete/notification-alert-delete-dialog.component';
import { NotificationAlertRoutingModule } from './route/notification-alert-routing.module';
import {HomeModule} from "../../home/home.module";

@NgModule({
  imports: [SharedModule, NotificationAlertRoutingModule, HomeModule],
  declarations: [
    NotificationAlertComponent,
    NotificationAlertDetailComponent,
    NotificationAlertDeleteDialogComponent,
  ],
})
export class NotificationAlertModule {}
