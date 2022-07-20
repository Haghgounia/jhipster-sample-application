import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IAssetAssign, getAssetAssignIdentifier } from '../asset-assign.model';

export type EntityResponseType = HttpResponse<IAssetAssign>;
export type EntityArrayResponseType = HttpResponse<IAssetAssign[]>;

@Injectable({ providedIn: 'root' })
export class AssetAssignService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/asset-assigns', 'jhipstersampleapplication');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(assetAssign: IAssetAssign): Observable<EntityResponseType> {
    return this.http.post<IAssetAssign>(this.resourceUrl, assetAssign, { observe: 'response' });
  }

  update(assetAssign: IAssetAssign): Observable<EntityResponseType> {
    return this.http.put<IAssetAssign>(`${this.resourceUrl}/${getAssetAssignIdentifier(assetAssign) as number}`, assetAssign, {
      observe: 'response',
    });
  }

  partialUpdate(assetAssign: IAssetAssign): Observable<EntityResponseType> {
    return this.http.patch<IAssetAssign>(`${this.resourceUrl}/${getAssetAssignIdentifier(assetAssign) as number}`, assetAssign, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IAssetAssign>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IAssetAssign[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addAssetAssignToCollectionIfMissing(
    assetAssignCollection: IAssetAssign[],
    ...assetAssignsToCheck: (IAssetAssign | null | undefined)[]
  ): IAssetAssign[] {
    const assetAssigns: IAssetAssign[] = assetAssignsToCheck.filter(isPresent);
    if (assetAssigns.length > 0) {
      const assetAssignCollectionIdentifiers = assetAssignCollection.map(assetAssignItem => getAssetAssignIdentifier(assetAssignItem)!);
      const assetAssignsToAdd = assetAssigns.filter(assetAssignItem => {
        const assetAssignIdentifier = getAssetAssignIdentifier(assetAssignItem);
        if (assetAssignIdentifier == null || assetAssignCollectionIdentifiers.includes(assetAssignIdentifier)) {
          return false;
        }
        assetAssignCollectionIdentifiers.push(assetAssignIdentifier);
        return true;
      });
      return [...assetAssignsToAdd, ...assetAssignCollection];
    }
    return assetAssignCollection;
  }
}
