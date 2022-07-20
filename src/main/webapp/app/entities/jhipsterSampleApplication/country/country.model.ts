import { IContinent } from 'app/entities/jhipsterSampleApplication/continent/continent.model';
import { ICurrency } from 'app/entities/jhipsterSampleApplication/currency/currency.model';
import { ILanguage } from 'app/entities/jhipsterSampleApplication/language/language.model';

export interface ICountry {
  id?: number;
  countryId?: number;
  countryName?: string;
  countryEnglishName?: string | null;
  countryFullName?: string | null;
  countryEnglishFullName?: string | null;
  countryIsoCode?: string;
  nationality?: string | null;
  englishNationality?: string | null;
  isActive?: number | null;
  sortOrder?: number | null;
  continent?: IContinent | null;
  currency?: ICurrency | null;
  officialLanguage?: ILanguage | null;
}

export class Country implements ICountry {
  constructor(
    public id?: number,
    public countryId?: number,
    public countryName?: string,
    public countryEnglishName?: string | null,
    public countryFullName?: string | null,
    public countryEnglishFullName?: string | null,
    public countryIsoCode?: string,
    public nationality?: string | null,
    public englishNationality?: string | null,
    public isActive?: number | null,
    public sortOrder?: number | null,
    public continent?: IContinent | null,
    public currency?: ICurrency | null,
    public officialLanguage?: ILanguage | null
  ) {}
}

export function getCountryIdentifier(country: ICountry): number | undefined {
  return country.id;
}
