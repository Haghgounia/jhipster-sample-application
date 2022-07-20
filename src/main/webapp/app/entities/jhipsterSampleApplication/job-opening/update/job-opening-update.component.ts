import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { IJobOpening, JobOpening } from '../job-opening.model';
import { JobOpeningService } from '../service/job-opening.service';

@Component({
  selector: 'jhi-job-opening-update',
  templateUrl: './job-opening-update.component.html',
})
export class JobOpeningUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    postingTitle: [],
    jobStatus: [],
    hiringLead: [],
    departmentId: [],
    employmentType: [],
    minimumExperience: [],
    jobDescription: [],
  });

  constructor(protected jobOpeningService: JobOpeningService, protected activatedRoute: ActivatedRoute, protected fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ jobOpening }) => {
      this.updateForm(jobOpening);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const jobOpening = this.createFromForm();
    if (jobOpening.id !== undefined) {
      this.subscribeToSaveResponse(this.jobOpeningService.update(jobOpening));
    } else {
      this.subscribeToSaveResponse(this.jobOpeningService.create(jobOpening));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IJobOpening>>): void {
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

  protected updateForm(jobOpening: IJobOpening): void {
    this.editForm.patchValue({
      id: jobOpening.id,
      postingTitle: jobOpening.postingTitle,
      jobStatus: jobOpening.jobStatus,
      hiringLead: jobOpening.hiringLead,
      departmentId: jobOpening.departmentId,
      employmentType: jobOpening.employmentType,
      minimumExperience: jobOpening.minimumExperience,
      jobDescription: jobOpening.jobDescription,
    });
  }

  protected createFromForm(): IJobOpening {
    return {
      ...new JobOpening(),
      id: this.editForm.get(['id'])!.value,
      postingTitle: this.editForm.get(['postingTitle'])!.value,
      jobStatus: this.editForm.get(['jobStatus'])!.value,
      hiringLead: this.editForm.get(['hiringLead'])!.value,
      departmentId: this.editForm.get(['departmentId'])!.value,
      employmentType: this.editForm.get(['employmentType'])!.value,
      minimumExperience: this.editForm.get(['minimumExperience'])!.value,
      jobDescription: this.editForm.get(['jobDescription'])!.value,
    };
  }
}
