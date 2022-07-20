import { ICountry } from 'app/entities/jhipsterSampleApplication/country/country.model';

export interface IProvince {
  id?: number;
  provinceId?: number;
  provinceCode?: string;
  provinceName?: string | null;
  provinceEnglishName?: string | null;
  diallingCode?: string | null;
  country?: ICountry | null;
}

export class Province implements IProvince {
  constructor(
    public id?: number,
    public provinceId?: number,
    public provinceCode?: string,
    public provinceName?: string | null,
    public provinceEnglishName?: string | null,
    public diallingCode?: string | null,
    public country?: ICountry | null
  ) {}
}

export function getProvinceIdentifier(province: IProvince): number | undefined {
  return province.id;
}
