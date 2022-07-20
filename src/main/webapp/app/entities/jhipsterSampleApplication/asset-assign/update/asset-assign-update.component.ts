import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { IAssetAssign, AssetAssign } from '../asset-assign.model';
import { AssetAssignService } from '../service/asset-assign.service';

@Component({
  selector: 'jhi-asset-assign-update',
  templateUrl: './asset-assign-update.component.html',
})
export class AssetAssignUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    assetId: [],
    employeeId: [],
    assignDate: [],
    returnDate: [],
  });

  constructor(protected assetAssignService: AssetAssignService, protected activatedRoute: ActivatedRoute, protected fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ assetAssign }) => {
      this.updateForm(assetAssign);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const assetAssign = this.createFromForm();
    if (assetAssign.id !== undefined) {
      this.subscribeToSaveResponse(this.assetAssignService.update(assetAssign));
    } else {
      this.subscribeToSaveResponse(this.assetAssignService.create(assetAssign));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IAssetAssign>>): void {
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

  protected updateForm(assetAssign: IAssetAssign): void {
    this.editForm.patchValue({
      id: assetAssign.id,
      assetId: assetAssign.assetId,
      employeeId: assetAssign.employeeId,
      assignDate: assetAssign.assignDate,
      returnDate: assetAssign.returnDate,
    });
  }

  protected createFromForm(): IAssetAssign {
    return {
      ...new AssetAssign(),
      id: this.editForm.get(['id'])!.value,
      assetId: this.editForm.get(['assetId'])!.value,
      employeeId: this.editForm.get(['employeeId'])!.value,
      assignDate: this.editForm.get(['assignDate'])!.value,
      returnDate: this.editForm.get(['returnDate'])!.value,
    };
  }
}
