import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ContinentComponent } from '../list/continent.component';
import { ContinentDetailComponent } from '../detail/continent-detail.component';
import { ContinentUpdateComponent } from '../update/continent-update.component';
import { ContinentRoutingResolveService } from './continent-routing-resolve.service';

const continentRoute: Routes = [
  {
    path: '',
    component: ContinentComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: ContinentDetailComponent,
    resolve: {
      continent: ContinentRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: ContinentUpdateComponent,
    resolve: {
      continent: ContinentRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: ContinentUpdateComponent,
    resolve: {
      continent: ContinentRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(continentRoute)],
  exports: [RouterModule],
})
export class ContinentRoutingModule {}
