import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { AssetAssignComponent } from '../list/asset-assign.component';
import { AssetAssignDetailComponent } from '../detail/asset-assign-detail.component';
import { AssetAssignUpdateComponent } from '../update/asset-assign-update.component';
import { AssetAssignRoutingResolveService } from './asset-assign-routing-resolve.service';

const assetAssignRoute: Routes = [
  {
    path: '',
    component: AssetAssignComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: AssetAssignDetailComponent,
    resolve: {
      assetAssign: AssetAssignRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: AssetAssignUpdateComponent,
    resolve: {
      assetAssign: AssetAssignRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: AssetAssignUpdateComponent,
    resolve: {
      assetAssign: AssetAssignRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(assetAssignRoute)],
  exports: [RouterModule],
})
export class AssetAssignRoutingModule {}
