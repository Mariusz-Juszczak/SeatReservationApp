import { Route } from '@angular/router';

import { ReserveComponent } from './reserve.component';
import {ASC} from "../config/navigation.constants";

export const reserveRoute: Route = {
  path: 'reserve',
  component: ReserveComponent,
  data: {
    pageTitle: 'reservation.title',
    defaultSort: 'status,' + ASC,
  },
};
