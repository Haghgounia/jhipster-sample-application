import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IEmployeeContact } from '../employee-contact.model';
import { EmployeeContactService } from '../service/employee-contact.service';

@Component({
  templateUrl: './employee-contact-delete-dialog.component.html',
})
export class EmployeeContactDeleteDialogComponent {
  employeeContact?: IEmployeeContact;

  constructor(protected employeeContactService: EmployeeContactService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.employeeContactService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
