import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IJobOpening } from '../job-opening.model';
import { JobOpeningService } from '../service/job-opening.service';

@Component({
  templateUrl: './job-opening-delete-dialog.component.html',
})
export class JobOpeningDeleteDialogComponent {
  jobOpening?: IJobOpening;

  constructor(protected jobOpeningService: JobOpeningService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.jobOpeningService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
