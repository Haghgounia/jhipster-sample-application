import { ICounty } from 'app/entities/jhipsterSampleApplication/county/county.model';

export interface IDistrict {
  id?: number;
  districtId?: number;
  districtCode?: string;
  districtName?: string | null;
  districtEnglishName?: string | null;
  county?: ICounty | null;
}

export class District implements IDistrict {
  constructor(
    public id?: number,
    public districtId?: number,
    public districtCode?: string,
    public districtName?: string | null,
    public districtEnglishName?: string | null,
    public county?: ICounty | null
  ) {}
}

export function getDistrictIdentifier(district: IDistrict): number | undefined {
  return district.id;
}
