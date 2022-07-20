import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { IEmployeeContact, EmployeeContact } from '../employee-contact.model';
import { EmployeeContactService } from '../service/employee-contact.service';

@Component({
  selector: 'jhi-employee-contact-update',
  templateUrl: './employee-contact-update.component.html',
})
export class EmployeeContactUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    employeeContactId: [],
    employeeId: [],
    contactType: [],
    contact: [],
  });

  constructor(
    protected employeeContactService: EmployeeContactService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ employeeContact }) => {
      this.updateForm(employeeContact);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const employeeContact = this.createFromForm();
    if (employeeContact.id !== undefined) {
      this.subscribeToSaveResponse(this.employeeContactService.update(employeeContact));
    } else {
      this.subscribeToSaveResponse(this.employeeContactService.create(employeeContact));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IEmployeeContact>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(employeeContact: IEmployeeContact): void {
    this.editForm.patchValue({
      id: employeeContact.id,
      employeeContactId: employeeContact.employeeContactId,
      employeeId: employeeContact.employeeId,
      contactType: employeeContact.contactType,
      contact: employeeContact.contact,
    });
  }

  protected createFromForm(): IEmployeeContact {
    return {
      ...new EmployeeContact(),
      id: this.editForm.get(['id'])!.value,
      employeeContactId: this.editForm.get(['employeeContactId'])!.value,
      employeeId: this.editForm.get(['employeeId'])!.value,
      contactType: this.editForm.get(['contactType'])!.value,
      contact: this.editForm.get(['contact'])!.value,
    };
  }
}
