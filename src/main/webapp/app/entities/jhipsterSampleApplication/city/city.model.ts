import { IDistrict } from 'app/entities/jhipsterSampleApplication/district/district.model';

export interface ICity {
  id?: number;
  cityId?: number;
  cityCode?: string;
  cityName?: string | null;
  cityEnglishName?: string | null;
  district?: IDistrict | null;
}

export class City implements ICity {
  constructor(
    public id?: number,
    public cityId?: number,
    public cityCode?: string,
    public cityName?: string | null,
    public cityEnglishName?: string | null,
    public district?: IDistrict | null
  ) {}
}

export function getCityIdentifier(city: ICity): number | undefined {
  return city.id;
}
