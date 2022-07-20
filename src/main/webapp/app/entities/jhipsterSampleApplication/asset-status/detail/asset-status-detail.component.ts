import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IAssetStatus } from '../asset-status.model';

@Component({
  selector: 'jhi-asset-status-detail',
  templateUrl: './asset-status-detail.component.html',
})
export class AssetStatusDetailComponent implements OnInit {
  assetStatus: IAssetStatus | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ assetStatus }) => {
      this.assetStatus = assetStatus;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
