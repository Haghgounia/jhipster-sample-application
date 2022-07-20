export interface ICurrency {
  id?: number;
  currencyId?: number;
  currencyAlphabeticIso?: string;
  currencyNumericIso?: string;
  currencyName?: string;
  currencyEnglishName?: string | null;
  currencySymbol?: string | null;
  floatingPoint?: number | null;
  isBaseCurrency?: number | null;
  isDefaultCurrency?: number | null;
  conversionMethod?: number | null;
  isActive?: number | null;
  sortOrder?: number | null;
}

export class Currency implements ICurrency {
  constructor(
    public id?: number,
    public currencyId?: number,
    public currencyAlphabeticIso?: string,
    public currencyNumericIso?: string,
    public currencyName?: string,
    public currencyEnglishName?: string | null,
    public currencySymbol?: string | null,
    public floatingPoint?: number | null,
    public isBaseCurrency?: number | null,
    public isDefaultCurrency?: number | null,
    public conversionMethod?: number | null,
    public isActive?: number | null,
    public sortOrder?: number | null
  ) {}
}

export function getCurrencyIdentifier(currency: ICurrency): number | undefined {
  return currency.id;
}
