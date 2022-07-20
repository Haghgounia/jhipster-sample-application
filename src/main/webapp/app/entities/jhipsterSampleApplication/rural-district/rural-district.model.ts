import { IDistrict } from 'app/entities/jhipsterSampleApplication/district/district.model';

export interface IRuralDistrict {
  id?: number;
  ruralDistrictId?: number;
  ruralDistrictCode?: string;
  ruralDistrictName?: string | null;
  ruralDistrictEnglishName?: string | null;
  district?: IDistrict | null;
}

export class RuralDistrict implements IRuralDistrict {
  constructor(
    public id?: number,
    public ruralDistrictId?: number,
    public ruralDistrictCode?: string,
    public ruralDistrictName?: string | null,
    public ruralDistrictEnglishName?: string | null,
    public district?: IDistrict | null
  ) {}
}

export function getRuralDistrictIdentifier(ruralDistrict: IRuralDistrict): number | undefined {
  return ruralDistrict.id;
}
