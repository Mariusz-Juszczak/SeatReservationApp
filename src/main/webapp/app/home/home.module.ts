import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { SharedModule } from 'app/shared/shared.module';
import { HOME_ROUTE } from './home.route';
import { HomeComponent } from './home.component';
import { HomeLoggedComponent } from './logged/home-logged.component';
import { HomeNotLoggedComponent } from "./notLogged/home-not-logged.component";
import { NotificationPaneComponent } from "../components/notification-pane/notification-pane.component";
import { LoginComponent } from "../login/login.component";
import {ReservationViewComponent} from "./reservation-view/reservation-view.component";

@NgModule({
    imports: [SharedModule, RouterModule.forChild([HOME_ROUTE])],
    declarations: [HomeComponent, HomeLoggedComponent, NotificationPaneComponent, HomeNotLoggedComponent, LoginComponent, ReservationViewComponent],
    exports: [
        NotificationPaneComponent, LoginComponent
    ]
})
export class HomeModule {}
