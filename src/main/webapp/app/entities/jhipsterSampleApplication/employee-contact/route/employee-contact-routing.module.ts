import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { EmployeeContactComponent } from '../list/employee-contact.component';
import { EmployeeContactDetailComponent } from '../detail/employee-contact-detail.component';
import { EmployeeContactUpdateComponent } from '../update/employee-contact-update.component';
import { EmployeeContactRoutingResolveService } from './employee-contact-routing-resolve.service';

const employeeContactRoute: Routes = [
  {
    path: '',
    component: EmployeeContactComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: EmployeeContactDetailComponent,
    resolve: {
      employeeContact: EmployeeContactRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: EmployeeContactUpdateComponent,
    resolve: {
      employeeContact: EmployeeContactRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: EmployeeContactUpdateComponent,
    resolve: {
      employeeContact: EmployeeContactRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(employeeContactRoute)],
  exports: [RouterModule],
})
export class EmployeeContactRoutingModule {}
