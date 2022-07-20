import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { ContinentComponent } from './list/continent.component';
import { ContinentDetailComponent } from './detail/continent-detail.component';
import { ContinentUpdateComponent } from './update/continent-update.component';
import { ContinentDeleteDialogComponent } from './delete/continent-delete-dialog.component';
import { ContinentRoutingModule } from './route/continent-routing.module';

@NgModule({
  imports: [SharedModule, ContinentRoutingModule],
  declarations: [ContinentComponent, ContinentDetailComponent, ContinentUpdateComponent, ContinentDeleteDialogComponent],
  entryComponents: [ContinentDeleteDialogComponent],
})
export class JhipsterSampleApplicationContinentModule {}
