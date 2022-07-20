import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IRuralDistrict, RuralDistrict } from '../rural-district.model';
import { RuralDistrictService } from '../service/rural-district.service';
import { IDistrict } from 'app/entities/jhipsterSampleApplication/district/district.model';
import { DistrictService } from 'app/entities/jhipsterSampleApplication/district/service/district.service';

@Component({
  selector: 'jhi-rural-district-update',
  templateUrl: './rural-district-update.component.html',
})
export class RuralDistrictUpdateComponent implements OnInit {
  isSaving = false;

  districtsSharedCollection: IDistrict[] = [];

  editForm = this.fb.group({
    id: [],
    ruralDistrictId: [null, [Validators.required]],
    ruralDistrictCode: [null, [Validators.required]],
    ruralDistrictName: [],
    ruralDistrictEnglishName: [],
    district: [],
  });

  constructor(
    protected ruralDistrictService: RuralDistrictService,
    protected districtService: DistrictService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ ruralDistrict }) => {
      this.updateForm(ruralDistrict);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const ruralDistrict = this.createFromForm();
    if (ruralDistrict.id !== undefined) {
      this.subscribeToSaveResponse(this.ruralDistrictService.update(ruralDistrict));
    } else {
      this.subscribeToSaveResponse(this.ruralDistrictService.create(ruralDistrict));
    }
  }

  trackDistrictById(_index: number, item: IDistrict): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IRuralDistrict>>): void {
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

  protected updateForm(ruralDistrict: IRuralDistrict): void {
    this.editForm.patchValue({
      id: ruralDistrict.id,
      ruralDistrictId: ruralDistrict.ruralDistrictId,
      ruralDistrictCode: ruralDistrict.ruralDistrictCode,
      ruralDistrictName: ruralDistrict.ruralDistrictName,
      ruralDistrictEnglishName: ruralDistrict.ruralDistrictEnglishName,
      district: ruralDistrict.district,
    });

    this.districtsSharedCollection = this.districtService.addDistrictToCollectionIfMissing(
      this.districtsSharedCollection,
      ruralDistrict.district
    );
  }

  protected loadRelationshipsOptions(): void {
    this.districtService
      .query()
      .pipe(map((res: HttpResponse<IDistrict[]>) => res.body ?? []))
      .pipe(
        map((districts: IDistrict[]) =>
          this.districtService.addDistrictToCollectionIfMissing(districts, this.editForm.get('district')!.value)
        )
      )
      .subscribe((districts: IDistrict[]) => (this.districtsSharedCollection = districts));
  }

  protected createFromForm(): IRuralDistrict {
    return {
      ...new RuralDistrict(),
      id: this.editForm.get(['id'])!.value,
      ruralDistrictId: this.editForm.get(['ruralDistrictId'])!.value,
      ruralDistrictCode: this.editForm.get(['ruralDistrictCode'])!.value,
      ruralDistrictName: this.editForm.get(['ruralDistrictName'])!.value,
      ruralDistrictEnglishName: this.editForm.get(['ruralDistrictEnglishName'])!.value,
      district: this.editForm.get(['district'])!.value,
    };
  }
}
