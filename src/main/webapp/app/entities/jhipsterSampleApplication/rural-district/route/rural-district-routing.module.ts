import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { RuralDistrictComponent } from '../list/rural-district.component';
import { RuralDistrictDetailComponent } from '../detail/rural-district-detail.component';
import { RuralDistrictUpdateComponent } from '../update/rural-district-update.component';
import { RuralDistrictRoutingResolveService } from './rural-district-routing-resolve.service';

const ruralDistrictRoute: Routes = [
  {
    path: '',
    component: RuralDistrictComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: RuralDistrictDetailComponent,
    resolve: {
      ruralDistrict: RuralDistrictRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: RuralDistrictUpdateComponent,
    resolve: {
      ruralDistrict: RuralDistrictRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: RuralDistrictUpdateComponent,
    resolve: {
      ruralDistrict: RuralDistrictRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(ruralDistrictRoute)],
  exports: [RouterModule],
})
export class RuralDistrictRoutingModule {}
