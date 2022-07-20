import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IAssetAssign } from '../asset-assign.model';
import { AssetAssignService } from '../service/asset-assign.service';

@Component({
  templateUrl: './asset-assign-delete-dialog.component.html',
})
export class AssetAssignDeleteDialogComponent {
  assetAssign?: IAssetAssign;

  constructor(protected assetAssignService: AssetAssignService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.assetAssignService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
