import { IProvince } from 'app/entities/jhipsterSampleApplication/province/province.model';

export interface ICounty {
  id?: number;
  countyId?: number;
  countyCode?: string;
  countyName?: string | null;
  countyEnglishName?: string | null;
  province?: IProvince | null;
}

export class County implements ICounty {
  constructor(
    public id?: number,
    public countyId?: number,
    public countyCode?: string,
    public countyName?: string | null,
    public countyEnglishName?: string | null,
    public province?: IProvince | null
  ) {}
}

export function getCountyIdentifier(county: ICounty): number | undefined {
  return county.id;
}
