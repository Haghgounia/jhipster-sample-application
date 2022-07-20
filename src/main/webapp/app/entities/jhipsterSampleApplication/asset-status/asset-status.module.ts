import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { AssetStatusComponent } from './list/asset-status.component';
import { AssetStatusDetailComponent } from './detail/asset-status-detail.component';
import { AssetStatusUpdateComponent } from './update/asset-status-update.component';
import { AssetStatusDeleteDialogComponent } from './delete/asset-status-delete-dialog.component';
import { AssetStatusRoutingModule } from './route/asset-status-routing.module';

@NgModule({
  imports: [SharedModule, AssetStatusRoutingModule],
  declarations: [AssetStatusComponent, AssetStatusDetailComponent, AssetStatusUpdateComponent, AssetStatusDeleteDialogComponent],
  entryComponents: [AssetStatusDeleteDialogComponent],
})
export class JhipsterSampleApplicationAssetStatusModule {}
