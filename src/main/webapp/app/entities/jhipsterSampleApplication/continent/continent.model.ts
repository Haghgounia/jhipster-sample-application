export interface IContinent {
  id?: number;
  continentId?: number;
  continentCode?: string;
  continentName?: string;
  continentEnglishName?: string | null;
}

export class Continent implements IContinent {
  constructor(
    public id?: number,
    public continentId?: number,
    public continentCode?: string,
    public continentName?: string,
    public continentEnglishName?: string | null
  ) {}
}

export function getContinentIdentifier(continent: IContinent): number | undefined {
  return continent.id;
}
