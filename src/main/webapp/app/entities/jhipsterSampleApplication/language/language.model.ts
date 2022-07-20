export interface ILanguage {
  id?: number;
  languageId?: number;
  languageIsoCode?: string;
  languageName?: string | null;
  languageEnglishName?: string | null;
  isActive?: number | null;
  sortOrder?: number | null;
}

export class Language implements ILanguage {
  constructor(
    public id?: number,
    public languageId?: number,
    public languageIsoCode?: string,
    public languageName?: string | null,
    public languageEnglishName?: string | null,
    public isActive?: number | null,
    public sortOrder?: number | null
  ) {}
}

export function getLanguageIdentifier(language: ILanguage): number | undefined {
  return language.id;
}
