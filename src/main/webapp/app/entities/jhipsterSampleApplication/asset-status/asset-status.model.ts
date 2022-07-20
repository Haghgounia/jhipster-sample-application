export interface IAssetStatus {
  id?: number;
  assetStatusId?: number | null;
  assetStatusName?: string | null;
}

export class AssetStatus implements IAssetStatus {
  constructor(public id?: number, public assetStatusId?: number | null, public assetStatusName?: string | null) {}
}

export function getAssetStatusIdentifier(assetStatus: IAssetStatus): number | undefined {
  return assetStatus.id;
}
