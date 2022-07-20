export interface IAssetAssign {
  id?: number;
  assetId?: number | null;
  employeeId?: number | null;
  assignDate?: number | null;
  returnDate?: number | null;
}

export class AssetAssign implements IAssetAssign {
  constructor(
    public id?: number,
    public assetId?: number | null,
    public employeeId?: number | null,
    public assignDate?: number | null,
    public returnDate?: number | null
  ) {}
}

export function getAssetAssignIdentifier(assetAssign: IAssetAssign): number | undefined {
  return assetAssign.id;
}
