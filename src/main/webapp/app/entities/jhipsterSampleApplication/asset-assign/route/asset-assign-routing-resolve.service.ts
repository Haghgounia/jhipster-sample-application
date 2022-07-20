import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IAssetAssign, AssetAssign } from '../asset-assign.model';
import { AssetAssignService } from '../service/asset-assign.service';

@Injectable({ providedIn: 'root' })
export class AssetAssignRoutingResolveService implements Resolve<IAssetAssign> {
  constructor(protected service: AssetAssignService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IAssetAssign> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((assetAssign: HttpResponse<AssetAssign>) => {
          if (assetAssign.body) {
            return of(assetAssign.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new AssetAssign());
  }
}
