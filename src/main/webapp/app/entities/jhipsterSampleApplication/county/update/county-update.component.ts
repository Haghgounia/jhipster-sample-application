import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { ICounty, County } from '../county.model';
import { CountyService } from '../service/county.service';
import { IProvince } from 'app/entities/jhipsterSampleApplication/province/province.model';
import { ProvinceService } from 'app/entities/jhipsterSampleApplication/province/service/province.service';

@Component({
  selector: 'jhi-county-update',
  templateUrl: './county-update.component.html',
})
export class CountyUpdateComponent implements OnInit {
  isSaving = false;

  provincesSharedCollection: IProvince[] = [];

  editForm = this.fb.group({
    id: [],
    countyId: [null, [Validators.required]],
    countyCode: [null, [Validators.required]],
    countyName: [],
    countyEnglishName: [],
    province: [],
  });

  constructor(
    protected countyService: CountyService,
    protected provinceService: ProvinceService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ county }) => {
      this.updateForm(county);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const county = this.createFromForm();
    if (county.id !== undefined) {
      this.subscribeToSaveResponse(this.countyService.update(county));
    } else {
      this.subscribeToSaveResponse(this.countyService.create(county));
    }
  }

  trackProvinceById(_index: number, item: IProvince): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ICounty>>): void {
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

  protected updateForm(county: ICounty): void {
    this.editForm.patchValue({
      id: county.id,
      countyId: county.countyId,
      countyCode: county.countyCode,
      countyName: county.countyName,
      countyEnglishName: county.countyEnglishName,
      province: county.province,
    });

    this.provincesSharedCollection = this.provinceService.addProvinceToCollectionIfMissing(this.provincesSharedCollection, county.province);
  }

  protected loadRelationshipsOptions(): void {
    this.provinceService
      .query()
      .pipe(map((res: HttpResponse<IProvince[]>) => res.body ?? []))
      .pipe(
        map((provinces: IProvince[]) =>
          this.provinceService.addProvinceToCollectionIfMissing(provinces, this.editForm.get('province')!.value)
        )
      )
      .subscribe((provinces: IProvince[]) => (this.provincesSharedCollection = provinces));
  }

  protected createFromForm(): ICounty {
    return {
      ...new County(),
      id: this.editForm.get(['id'])!.value,
      countyId: this.editForm.get(['countyId'])!.value,
      countyCode: this.editForm.get(['countyCode'])!.value,
      countyName: this.editForm.get(['countyName'])!.value,
      countyEnglishName: this.editForm.get(['countyEnglishName'])!.value,
      province: this.editForm.get(['province'])!.value,
    };
  }
}
