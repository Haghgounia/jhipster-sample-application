import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IProvince, Province } from '../province.model';
import { ProvinceService } from '../service/province.service';
import { ICountry } from 'app/entities/jhipsterSampleApplication/country/country.model';
import { CountryService } from 'app/entities/jhipsterSampleApplication/country/service/country.service';

@Component({
  selector: 'jhi-province-update',
  templateUrl: './province-update.component.html',
})
export class ProvinceUpdateComponent implements OnInit {
  isSaving = false;

  countriesSharedCollection: ICountry[] = [];

  editForm = this.fb.group({
    id: [],
    provinceId: [null, [Validators.required]],
    provinceCode: [null, [Validators.required]],
    provinceName: [],
    provinceEnglishName: [],
    diallingCode: [],
    country: [],
  });

  constructor(
    protected provinceService: ProvinceService,
    protected countryService: CountryService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ province }) => {
      this.updateForm(province);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const province = this.createFromForm();
    if (province.id !== undefined) {
      this.subscribeToSaveResponse(this.provinceService.update(province));
    } else {
      this.subscribeToSaveResponse(this.provinceService.create(province));
    }
  }

  trackCountryById(_index: number, item: ICountry): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IProvince>>): void {
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

  protected updateForm(province: IProvince): void {
    this.editForm.patchValue({
      id: province.id,
      provinceId: province.provinceId,
      provinceCode: province.provinceCode,
      provinceName: province.provinceName,
      provinceEnglishName: province.provinceEnglishName,
      diallingCode: province.diallingCode,
      country: province.country,
    });

    this.countriesSharedCollection = this.countryService.addCountryToCollectionIfMissing(this.countriesSharedCollection, province.country);
  }

  protected loadRelationshipsOptions(): void {
    this.countryService
      .query()
      .pipe(map((res: HttpResponse<ICountry[]>) => res.body ?? []))
      .pipe(
        map((countries: ICountry[]) => this.countryService.addCountryToCollectionIfMissing(countries, this.editForm.get('country')!.value))
      )
      .subscribe((countries: ICountry[]) => (this.countriesSharedCollection = countries));
  }

  protected createFromForm(): IProvince {
    return {
      ...new Province(),
      id: this.editForm.get(['id'])!.value,
      provinceId: this.editForm.get(['provinceId'])!.value,
      provinceCode: this.editForm.get(['provinceCode'])!.value,
      provinceName: this.editForm.get(['provinceName'])!.value,
      provinceEnglishName: this.editForm.get(['provinceEnglishName'])!.value,
      diallingCode: this.editForm.get(['diallingCode'])!.value,
      country: this.editForm.get(['country'])!.value,
    };
  }
}
