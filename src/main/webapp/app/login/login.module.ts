import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { SharedModule } from 'app/shared/shared.module';
import { LOGIN_ROUTE } from './login.route';

@NgModule({
    imports: [SharedModule, RouterModule.forChild([LOGIN_ROUTE])],
    declarations: [],
    exports: [

    ]
})
export class LoginModule {}
