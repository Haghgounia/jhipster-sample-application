import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { ICurrency, Currency } from '../currency.model';
import { CurrencyService } from '../service/currency.service';

@Component({
  selector: 'jhi-currency-update',
  templateUrl: './currency-update.component.html',
})
export class CurrencyUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    currencyId: [null, [Validators.required]],
    currencyAlphabeticIso: [null, [Validators.required]],
    currencyNumericIso: [null, [Validators.required]],
    currencyName: [null, [Validators.required]],
    currencyEnglishName: [],
    currencySymbol: [],
    floatingPoint: [],
    isBaseCurrency: [],
    isDefaultCurrency: [],
    conversionMethod: [],
    isActive: [],
    sortOrder: [],
  });

  constructor(protected currencyService: CurrencyService, protected activatedRoute: ActivatedRoute, protected fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ currency }) => {
      this.updateForm(currency);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const currency = this.createFromForm();
    if (currency.id !== undefined) {
      this.subscribeToSaveResponse(this.currencyService.update(currency));
    } else {
      this.subscribeToSaveResponse(this.currencyService.create(currency));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ICurrency>>): void {
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

  protected updateForm(currency: ICurrency): void {
    this.editForm.patchValue({
      id: currency.id,
      currencyId: currency.currencyId,
      currencyAlphabeticIso: currency.currencyAlphabeticIso,
      currencyNumericIso: currency.currencyNumericIso,
      currencyName: currency.currencyName,
      currencyEnglishName: currency.currencyEnglishName,
      currencySymbol: currency.currencySymbol,
      floatingPoint: currency.floatingPoint,
      isBaseCurrency: currency.isBaseCurrency,
      isDefaultCurrency: currency.isDefaultCurrency,
      conversionMethod: currency.conversionMethod,
      isActive: currency.isActive,
      sortOrder: currency.sortOrder,
    });
  }

  protected createFromForm(): ICurrency {
    return {
      ...new Currency(),
      id: this.editForm.get(['id'])!.value,
      currencyId: this.editForm.get(['currencyId'])!.value,
      currencyAlphabeticIso: this.editForm.get(['currencyAlphabeticIso'])!.value,
      currencyNumericIso: this.editForm.get(['currencyNumericIso'])!.value,
      currencyName: this.editForm.get(['currencyName'])!.value,
      currencyEnglishName: this.editForm.get(['currencyEnglishName'])!.value,
      currencySymbol: this.editForm.get(['currencySymbol'])!.value,
      floatingPoint: this.editForm.get(['floatingPoint'])!.value,
      isBaseCurrency: this.editForm.get(['isBaseCurrency'])!.value,
      isDefaultCurrency: this.editForm.get(['isDefaultCurrency'])!.value,
      conversionMethod: this.editForm.get(['conversionMethod'])!.value,
      isActive: this.editForm.get(['isActive'])!.value,
      sortOrder: this.editForm.get(['sortOrder'])!.value,
    };
  }
}
