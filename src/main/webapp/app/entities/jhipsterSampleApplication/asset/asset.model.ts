export interface IAsset {
  id?: number;
  assetId?: number | null;
  assetSerial?: string | null;
  assetStatus?: number | null;
}

export class Asset implements IAsset {
  constructor(public id?: number, public assetId?: number | null, public assetSerial?: string | null, public assetStatus?: number | null) {}
}

export function getAssetIdentifier(asset: IAsset): number | undefined {
  return asset.id;
}
