import { IRuralDistrict } from 'app/entities/jhipsterSampleApplication/rural-district/rural-district.model';

export interface IVillage {
  id?: number;
  villageId?: number;
  villageCode?: string;
  villageName?: string | null;
  villageEnglishName?: string | null;
  ruralDistrict?: IRuralDistrict | null;
}

export class Village implements IVillage {
  constructor(
    public id?: number,
    public villageId?: number,
    public villageCode?: string,
    public villageName?: string | null,
    public villageEnglishName?: string | null,
    public ruralDistrict?: IRuralDistrict | null
  ) {}
}

export function getVillageIdentifier(village: IVillage): number | undefined {
  return village.id;
}
