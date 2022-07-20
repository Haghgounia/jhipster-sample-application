import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IDistrict, District } from '../district.model';
import { DistrictService } from '../service/district.service';
import { ICounty } from 'app/entities/jhipsterSampleApplication/county/county.model';
import { CountyService } from 'app/entities/jhipsterSampleApplication/county/service/county.service';

@Component({
  selector: 'jhi-district-update',
  templateUrl: './district-update.component.html',
})
export class DistrictUpdateComponent implements OnInit {
  isSaving = false;

  countiesSharedCollection: ICounty[] = [];

  editForm = this.fb.group({
    id: [],
    districtId: [null, [Validators.required]],
    districtCode: [null, [Validators.required]],
    districtName: [],
    districtEnglishName: [],
    county: [],
  });

  constructor(
    protected districtService: DistrictService,
    protected countyService: CountyService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ district }) => {
      this.updateForm(district);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const district = this.createFromForm();
    if (district.id !== undefined) {
      this.subscribeToSaveResponse(this.districtService.update(district));
    } else {
      this.subscribeToSaveResponse(this.districtService.create(district));
    }
  }

  trackCountyById(_index: number, item: ICounty): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IDistrict>>): void {
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

  protected updateForm(district: IDistrict): void {
    this.editForm.patchValue({
      id: district.id,
      districtId: district.districtId,
      districtCode: district.districtCode,
      districtName: district.districtName,
      districtEnglishName: district.districtEnglishName,
      county: district.county,
    });

    this.countiesSharedCollection = this.countyService.addCountyToCollectionIfMissing(this.countiesSharedCollection, district.county);
  }

  protected loadRelationshipsOptions(): void {
    this.countyService
      .query()
      .pipe(map((res: HttpResponse<ICounty[]>) => res.body ?? []))
      .pipe(map((counties: ICounty[]) => this.countyService.addCountyToCollectionIfMissing(counties, this.editForm.get('county')!.value)))
      .subscribe((counties: ICounty[]) => (this.countiesSharedCollection = counties));
  }

  protected createFromForm(): IDistrict {
    return {
      ...new District(),
      id: this.editForm.get(['id'])!.value,
      districtId: this.editForm.get(['districtId'])!.value,
      districtCode: this.editForm.get(['districtCode'])!.value,
      districtName: this.editForm.get(['districtName'])!.value,
      districtEnglishName: this.editForm.get(['districtEnglishName'])!.value,
      county: this.editForm.get(['county'])!.value,
    };
  }
}
