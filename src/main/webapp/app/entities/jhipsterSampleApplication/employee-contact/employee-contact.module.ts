import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { EmployeeContactComponent } from './list/employee-contact.component';
import { EmployeeContactDetailComponent } from './detail/employee-contact-detail.component';
import { EmployeeContactUpdateComponent } from './update/employee-contact-update.component';
import { EmployeeContactDeleteDialogComponent } from './delete/employee-contact-delete-dialog.component';
import { EmployeeContactRoutingModule } from './route/employee-contact-routing.module';

@NgModule({
  imports: [SharedModule, EmployeeContactRoutingModule],
  declarations: [
    EmployeeContactComponent,
    EmployeeContactDetailComponent,
    EmployeeContactUpdateComponent,
    EmployeeContactDeleteDialogComponent,
  ],
  entryComponents: [EmployeeContactDeleteDialogComponent],
})
export class JhipsterSampleApplicationEmployeeContactModule {}
