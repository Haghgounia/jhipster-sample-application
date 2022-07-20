import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { ICountry, Country } from '../country.model';
import { CountryService } from '../service/country.service';
import { IContinent } from 'app/entities/jhipsterSampleApplication/continent/continent.model';
import { ContinentService } from 'app/entities/jhipsterSampleApplication/continent/service/continent.service';
import { ICurrency } from 'app/entities/jhipsterSampleApplication/currency/currency.model';
import { CurrencyService } from 'app/entities/jhipsterSampleApplication/currency/service/currency.service';
import { ILanguage } from 'app/entities/jhipsterSampleApplication/language/language.model';
import { LanguageService } from 'app/entities/jhipsterSampleApplication/language/service/language.service';

@Component({
  selector: 'jhi-country-update',
  templateUrl: './country-update.component.html',
})
export class CountryUpdateComponent implements OnInit {
  isSaving = false;

  continentsSharedCollection: IContinent[] = [];
  currenciesSharedCollection: ICurrency[] = [];
  languagesSharedCollection: ILanguage[] = [];

  editForm = this.fb.group({
    id: [],
    countryId: [null, [Validators.required]],
    countryName: [null, [Validators.required]],
    countryEnglishName: [],
    countryFullName: [],
    countryEnglishFullName: [],
    countryIsoCode: [null, [Validators.required]],
    nationality: [],
    englishNationality: [],
    isActive: [],
    sortOrder: [],
    continent: [],
    currency: [],
    officialLanguage: [],
  });

  constructor(
    protected countryService: CountryService,
    protected continentService: ContinentService,
    protected currencyService: CurrencyService,
    protected languageService: LanguageService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ country }) => {
      this.updateForm(country);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const country = this.createFromForm();
    if (country.id !== undefined) {
      this.subscribeToSaveResponse(this.countryService.update(country));
    } else {
      this.subscribeToSaveResponse(this.countryService.create(country));
    }
  }

  trackContinentById(_index: number, item: IContinent): number {
    return item.id!;
  }

  trackCurrencyById(_index: number, item: ICurrency): number {
    return item.id!;
  }

  trackLanguageById(_index: number, item: ILanguage): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ICountry>>): void {
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

  protected updateForm(country: ICountry): void {
    this.editForm.patchValue({
      id: country.id,
      countryId: country.countryId,
      countryName: country.countryName,
      countryEnglishName: country.countryEnglishName,
      countryFullName: country.countryFullName,
      countryEnglishFullName: country.countryEnglishFullName,
      countryIsoCode: country.countryIsoCode,
      nationality: country.nationality,
      englishNationality: country.englishNationality,
      isActive: country.isActive,
      sortOrder: country.sortOrder,
      continent: country.continent,
      currency: country.currency,
      officialLanguage: country.officialLanguage,
    });

    this.continentsSharedCollection = this.continentService.addContinentToCollectionIfMissing(
      this.continentsSharedCollection,
      country.continent
    );
    this.currenciesSharedCollection = this.currencyService.addCurrencyToCollectionIfMissing(
      this.currenciesSharedCollection,
      country.currency
    );
    this.languagesSharedCollection = this.languageService.addLanguageToCollectionIfMissing(
      this.languagesSharedCollection,
      country.officialLanguage
    );
  }

  protected loadRelationshipsOptions(): void {
    this.continentService
      .query()
      .pipe(map((res: HttpResponse<IContinent[]>) => res.body ?? []))
      .pipe(
        map((continents: IContinent[]) =>
          this.continentService.addContinentToCollectionIfMissing(continents, this.editForm.get('continent')!.value)
        )
      )
      .subscribe((continents: IContinent[]) => (this.continentsSharedCollection = continents));

    this.currencyService
      .query()
      .pipe(map((res: HttpResponse<ICurrency[]>) => res.body ?? []))
      .pipe(
        map((currencies: ICurrency[]) =>
          this.currencyService.addCurrencyToCollectionIfMissing(currencies, this.editForm.get('currency')!.value)
        )
      )
      .subscribe((currencies: ICurrency[]) => (this.currenciesSharedCollection = currencies));

    this.languageService
      .query()
      .pipe(map((res: HttpResponse<ILanguage[]>) => res.body ?? []))
      .pipe(
        map((languages: ILanguage[]) =>
          this.languageService.addLanguageToCollectionIfMissing(languages, this.editForm.get('officialLanguage')!.value)
        )
      )
      .subscribe((languages: ILanguage[]) => (this.languagesSharedCollection = languages));
  }

  protected createFromForm(): ICountry {
    return {
      ...new Country(),
      id: this.editForm.get(['id'])!.value,
      countryId: this.editForm.get(['countryId'])!.value,
      countryName: this.editForm.get(['countryName'])!.value,
      countryEnglishName: this.editForm.get(['countryEnglishName'])!.value,
      countryFullName: this.editForm.get(['countryFullName'])!.value,
      countryEnglishFullName: this.editForm.get(['countryEnglishFullName'])!.value,
      countryIsoCode: this.editForm.get(['countryIsoCode'])!.value,
      nationality: this.editForm.get(['nationality'])!.value,
      englishNationality: this.editForm.get(['englishNationality'])!.value,
      isActive: this.editForm.get(['isActive'])!.value,
      sortOrder: this.editForm.get(['sortOrder'])!.value,
      continent: this.editForm.get(['continent'])!.value,
      currency: this.editForm.get(['currency'])!.value,
      officialLanguage: this.editForm.get(['officialLanguage'])!.value,
    };
  }
}
