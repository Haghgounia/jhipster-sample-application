import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { JobOpeningComponent } from '../list/job-opening.component';
import { JobOpeningDetailComponent } from '../detail/job-opening-detail.component';
import { JobOpeningUpdateComponent } from '../update/job-opening-update.component';
import { JobOpeningRoutingResolveService } from './job-opening-routing-resolve.service';

const jobOpeningRoute: Routes = [
  {
    path: '',
    component: JobOpeningComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: JobOpeningDetailComponent,
    resolve: {
      jobOpening: JobOpeningRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: JobOpeningUpdateComponent,
    resolve: {
      jobOpening: JobOpeningRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: JobOpeningUpdateComponent,
    resolve: {
      jobOpening: JobOpeningRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(jobOpeningRoute)],
  exports: [RouterModule],
})
export class JobOpeningRoutingModule {}
