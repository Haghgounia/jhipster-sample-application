import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IRuralDistrict } from '../rural-district.model';
import { RuralDistrictService } from '../service/rural-district.service';

@Component({
  templateUrl: './rural-district-delete-dialog.component.html',
})
export class RuralDistrictDeleteDialogComponent {
  ruralDistrict?: IRuralDistrict;

  constructor(protected ruralDistrictService: RuralDistrictService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.ruralDistrictService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
