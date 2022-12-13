import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { RestrictionsComponent } from './list/restrictions.component';
import { RestrictionsDetailComponent } from './detail/restrictions-detail.component';
import { RestrictionsUpdateComponent } from './update/restrictions-update.component';
import { RestrictionsDeleteDialogComponent } from './delete/restrictions-delete-dialog.component';
import { RestrictionsRoutingModule } from './route/restrictions-routing.module';

@NgModule({
  imports: [SharedModule, RestrictionsRoutingModule],
  declarations: [RestrictionsComponent, RestrictionsDetailComponent, RestrictionsUpdateComponent, RestrictionsDeleteDialogComponent],
})
export class RestrictionsModule {}
