import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { IEmployee, Employee } from '../employee.model';
import { EmployeeService } from '../service/employee.service';

@Component({
  selector: 'jhi-employee-update',
  templateUrl: './employee-update.component.html',
})
export class EmployeeUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    employeeId: [],
    firstName: [],
    lastName: [],
    gender: [],
    maritalStatus: [],
    birthDate: [],
    hireDate: [],
    employmentStatus: [],
    managerId: [],
  });

  constructor(protected employeeService: EmployeeService, protected activatedRoute: ActivatedRoute, protected fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ employee }) => {
      this.updateForm(employee);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const employee = this.createFromForm();
    if (employee.id !== undefined) {
      this.subscribeToSaveResponse(this.employeeService.update(employee));
    } else {
      this.subscribeToSaveResponse(this.employeeService.create(employee));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IEmployee>>): void {
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

  protected updateForm(employee: IEmployee): void {
    this.editForm.patchValue({
      id: employee.id,
      employeeId: employee.employeeId,
      firstName: employee.firstName,
      lastName: employee.lastName,
      gender: employee.gender,
      maritalStatus: employee.maritalStatus,
      birthDate: employee.birthDate,
      hireDate: employee.hireDate,
      employmentStatus: employee.employmentStatus,
      managerId: employee.managerId,
    });
  }

  protected createFromForm(): IEmployee {
    return {
      ...new Employee(),
      id: this.editForm.get(['id'])!.value,
      employeeId: this.editForm.get(['employeeId'])!.value,
      firstName: this.editForm.get(['firstName'])!.value,
      lastName: this.editForm.get(['lastName'])!.value,
      gender: this.editForm.get(['gender'])!.value,
      maritalStatus: this.editForm.get(['maritalStatus'])!.value,
      birthDate: this.editForm.get(['birthDate'])!.value,
      hireDate: this.editForm.get(['hireDate'])!.value,
      employmentStatus: this.editForm.get(['employmentStatus'])!.value,
      managerId: this.editForm.get(['managerId'])!.value,
    };
  }
}
