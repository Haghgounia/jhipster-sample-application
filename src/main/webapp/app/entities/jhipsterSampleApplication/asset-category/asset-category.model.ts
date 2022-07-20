export interface IAssetCategory {
  id?: number;
  assetCategoryId?: number | null;
  assetCategoryName?: string | null;
}

export class AssetCategory implements IAssetCategory {
  constructor(public id?: number, public assetCategoryId?: number | null, public assetCategoryName?: string | null) {}
}

export function getAssetCategoryIdentifier(assetCategory: IAssetCategory): number | undefined {
  return assetCategory.id;
}
