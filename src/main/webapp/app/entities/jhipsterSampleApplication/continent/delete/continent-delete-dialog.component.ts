import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IContinent } from '../continent.model';
import { ContinentService } from '../service/continent.service';

@Component({
  templateUrl: './continent-delete-dialog.component.html',
})
export class ContinentDeleteDialogComponent {
  continent?: IContinent;

  constructor(protected continentService: ContinentService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.continentService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
