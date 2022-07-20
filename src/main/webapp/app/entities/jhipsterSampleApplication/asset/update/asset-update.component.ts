import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { IAsset, Asset } from '../asset.model';
import { AssetService } from '../service/asset.service';

@Component({
  selector: 'jhi-asset-update',
  templateUrl: './asset-update.component.html',
})
export class AssetUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    assetId: [],
    assetSerial: [],
    assetStatus: [],
  });

  constructor(protected assetService: AssetService, protected activatedRoute: ActivatedRoute, protected fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ asset }) => {
      this.updateForm(asset);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const asset = this.createFromForm();
    if (asset.id !== undefined) {
      this.subscribeToSaveResponse(this.assetService.update(asset));
    } else {
      this.subscribeToSaveResponse(this.assetService.create(asset));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IAsset>>): void {
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

  protected updateForm(asset: IAsset): void {
    this.editForm.patchValue({
      id: asset.id,
      assetId: asset.assetId,
      assetSerial: asset.assetSerial,
      assetStatus: asset.assetStatus,
    });
  }

  protected createFromForm(): IAsset {
    return {
      ...new Asset(),
      id: this.editForm.get(['id'])!.value,
      assetId: this.editForm.get(['assetId'])!.value,
      assetSerial: this.editForm.get(['assetSerial'])!.value,
      assetStatus: this.editForm.get(['assetStatus'])!.value,
    };
  }
}
