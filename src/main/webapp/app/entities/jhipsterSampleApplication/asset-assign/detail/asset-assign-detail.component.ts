import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IAssetAssign } from '../asset-assign.model';

@Component({
  selector: 'jhi-asset-assign-detail',
  templateUrl: './asset-assign-detail.component.html',
})
export class AssetAssignDetailComponent implements OnInit {
  assetAssign: IAssetAssign | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ assetAssign }) => {
      this.assetAssign = assetAssign;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
