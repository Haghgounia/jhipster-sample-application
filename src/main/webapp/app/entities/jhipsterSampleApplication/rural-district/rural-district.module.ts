import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { RuralDistrictComponent } from './list/rural-district.component';
import { RuralDistrictDetailComponent } from './detail/rural-district-detail.component';
import { RuralDistrictUpdateComponent } from './update/rural-district-update.component';
import { RuralDistrictDeleteDialogComponent } from './delete/rural-district-delete-dialog.component';
import { RuralDistrictRoutingModule } from './route/rural-district-routing.module';

@NgModule({
  imports: [SharedModule, RuralDistrictRoutingModule],
  declarations: [RuralDistrictComponent, RuralDistrictDetailComponent, RuralDistrictUpdateComponent, RuralDistrictDeleteDialogComponent],
  entryComponents: [RuralDistrictDeleteDialogComponent],
})
export class JhipsterSampleApplicationRuralDistrictModule {}
