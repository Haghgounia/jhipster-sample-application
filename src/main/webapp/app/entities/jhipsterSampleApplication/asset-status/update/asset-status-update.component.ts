import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { IAssetStatus, AssetStatus } from '../asset-status.model';
import { AssetStatusService } from '../service/asset-status.service';

@Component({
  selector: 'jhi-asset-status-update',
  templateUrl: './asset-status-update.component.html',
})
export class AssetStatusUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    assetStatusId: [],
    assetStatusName: [],
  });

  constructor(protected assetStatusService: AssetStatusService, protected activatedRoute: ActivatedRoute, protected fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ assetStatus }) => {
      this.updateForm(assetStatus);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const assetStatus = this.createFromForm();
    if (assetStatus.id !== undefined) {
      this.subscribeToSaveResponse(this.assetStatusService.update(assetStatus));
    } else {
      this.subscribeToSaveResponse(this.assetStatusService.create(assetStatus));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IAssetStatus>>): void {
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

  protected updateForm(assetStatus: IAssetStatus): void {
    this.editForm.patchValue({
      id: assetStatus.id,
      assetStatusId: assetStatus.assetStatusId,
      assetStatusName: assetStatus.assetStatusName,
    });
  }

  protected createFromForm(): IAssetStatus {
    return {
      ...new AssetStatus(),
      id: this.editForm.get(['id'])!.value,
      assetStatusId: this.editForm.get(['assetStatusId'])!.value,
      assetStatusName: this.editForm.get(['assetStatusName'])!.value,
    };
  }
}
