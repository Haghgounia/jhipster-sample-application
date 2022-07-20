import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { AssetStatusComponent } from '../list/asset-status.component';
import { AssetStatusDetailComponent } from '../detail/asset-status-detail.component';
import { AssetStatusUpdateComponent } from '../update/asset-status-update.component';
import { AssetStatusRoutingResolveService } from './asset-status-routing-resolve.service';

const assetStatusRoute: Routes = [
  {
    path: '',
    component: AssetStatusComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: AssetStatusDetailComponent,
    resolve: {
      assetStatus: AssetStatusRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: AssetStatusUpdateComponent,
    resolve: {
      assetStatus: AssetStatusRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: AssetStatusUpdateComponent,
    resolve: {
      assetStatus: AssetStatusRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(assetStatusRoute)],
  exports: [RouterModule],
})
export class AssetStatusRoutingModule {}
