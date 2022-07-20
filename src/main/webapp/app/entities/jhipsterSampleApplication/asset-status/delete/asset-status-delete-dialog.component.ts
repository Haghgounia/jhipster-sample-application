import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IAssetStatus } from '../asset-status.model';
import { AssetStatusService } from '../service/asset-status.service';

@Component({
  templateUrl: './asset-status-delete-dialog.component.html',
})
export class AssetStatusDeleteDialogComponent {
  assetStatus?: IAssetStatus;

  constructor(protected assetStatusService: AssetStatusService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.assetStatusService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
