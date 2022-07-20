import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { JobOpeningComponent } from './list/job-opening.component';
import { JobOpeningDetailComponent } from './detail/job-opening-detail.component';
import { JobOpeningUpdateComponent } from './update/job-opening-update.component';
import { JobOpeningDeleteDialogComponent } from './delete/job-opening-delete-dialog.component';
import { JobOpeningRoutingModule } from './route/job-opening-routing.module';

@NgModule({
  imports: [SharedModule, JobOpeningRoutingModule],
  declarations: [JobOpeningComponent, JobOpeningDetailComponent, JobOpeningUpdateComponent, JobOpeningDeleteDialogComponent],
  entryComponents: [JobOpeningDeleteDialogComponent],
})
export class JhipsterSampleApplicationJobOpeningModule {}
