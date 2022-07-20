import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { AssetAssignComponent } from './list/asset-assign.component';
import { AssetAssignDetailComponent } from './detail/asset-assign-detail.component';
import { AssetAssignUpdateComponent } from './update/asset-assign-update.component';
import { AssetAssignDeleteDialogComponent } from './delete/asset-assign-delete-dialog.component';
import { AssetAssignRoutingModule } from './route/asset-assign-routing.module';

@NgModule({
  imports: [SharedModule, AssetAssignRoutingModule],
  declarations: [AssetAssignComponent, AssetAssignDetailComponent, AssetAssignUpdateComponent, AssetAssignDeleteDialogComponent],
  entryComponents: [AssetAssignDeleteDialogComponent],
})
export class JhipsterSampleApplicationAssetAssignModule {}
