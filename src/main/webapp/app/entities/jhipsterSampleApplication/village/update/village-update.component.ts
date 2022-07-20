import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IVillage, Village } from '../village.model';
import { VillageService } from '../service/village.service';
import { IRuralDistrict } from 'app/entities/jhipsterSampleApplication/rural-district/rural-district.model';
import { RuralDistrictService } from 'app/entities/jhipsterSampleApplication/rural-district/service/rural-district.service';

@Component({
  selector: 'jhi-village-update',
  templateUrl: './village-update.component.html',
})
export class VillageUpdateComponent implements OnInit {
  isSaving = false;

  ruralDistrictsSharedCollection: IRuralDistrict[] = [];

  editForm = this.fb.group({
    id: [],
    villageId: [null, [Validators.required]],
    villageCode: [null, [Validators.required]],
    villageName: [],
    villageEnglishName: [],
    ruralDistrict: [],
  });

  constructor(
    protected villageService: VillageService,
    protected ruralDistrictService: RuralDistrictService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ village }) => {
      this.updateForm(village);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const village = this.createFromForm();
    if (village.id !== undefined) {
      this.subscribeToSaveResponse(this.villageService.update(village));
    } else {
      this.subscribeToSaveResponse(this.villageService.create(village));
    }
  }

  trackRuralDistrictById(_index: number, item: IRuralDistrict): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IVillage>>): void {
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

  protected updateForm(village: IVillage): void {
    this.editForm.patchValue({
      id: village.id,
      villageId: village.villageId,
      villageCode: village.villageCode,
      villageName: village.villageName,
      villageEnglishName: village.villageEnglishName,
      ruralDistrict: village.ruralDistrict,
    });

    this.ruralDistrictsSharedCollection = this.ruralDistrictService.addRuralDistrictToCollectionIfMissing(
      this.ruralDistrictsSharedCollection,
      village.ruralDistrict
    );
  }

  protected loadRelationshipsOptions(): void {
    this.ruralDistrictService
      .query()
      .pipe(map((res: HttpResponse<IRuralDistrict[]>) => res.body ?? []))
      .pipe(
        map((ruralDistricts: IRuralDistrict[]) =>
          this.ruralDistrictService.addRuralDistrictToCollectionIfMissing(ruralDistricts, this.editForm.get('ruralDistrict')!.value)
        )
      )
      .subscribe((ruralDistricts: IRuralDistrict[]) => (this.ruralDistrictsSharedCollection = ruralDistricts));
  }

  protected createFromForm(): IVillage {
    return {
      ...new Village(),
      id: this.editForm.get(['id'])!.value,
      villageId: this.editForm.get(['villageId'])!.value,
      villageCode: this.editForm.get(['villageCode'])!.value,
      villageName: this.editForm.get(['villageName'])!.value,
      villageEnglishName: this.editForm.get(['villageEnglishName'])!.value,
      ruralDistrict: this.editForm.get(['ruralDistrict'])!.value,
    };
  }
}
