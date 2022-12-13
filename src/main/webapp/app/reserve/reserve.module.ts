import { NgModule } from '@angular/core';

import { SharedModule } from 'app/shared/shared.module';
import { ReserveComponent } from './reserve.component';
import {NgSelectModule} from "@ng-select/ng-select";

@NgModule({
    imports: [SharedModule, NgSelectModule],
  declarations: [ReserveComponent],
})
export class ReserveModule {}
