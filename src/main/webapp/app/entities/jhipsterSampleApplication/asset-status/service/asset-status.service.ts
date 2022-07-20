import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IAssetStatus, getAssetStatusIdentifier } from '../asset-status.model';

export type EntityResponseType = HttpResponse<IAssetStatus>;
export type EntityArrayResponseType = HttpResponse<IAssetStatus[]>;

@Injectable({ providedIn: 'root' })
export class AssetStatusService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/asset-statuses', 'jhipstersampleapplication');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(assetStatus: IAssetStatus): Observable<EntityResponseType> {
    return this.http.post<IAssetStatus>(this.resourceUrl, assetStatus, { observe: 'response' });
  }

  update(assetStatus: IAssetStatus): Observable<EntityResponseType> {
    return this.http.put<IAssetStatus>(`${this.resourceUrl}/${getAssetStatusIdentifier(assetStatus) as number}`, assetStatus, {
      observe: 'response',
    });
  }

  partialUpdate(assetStatus: IAssetStatus): Observable<EntityResponseType> {
    return this.http.patch<IAssetStatus>(`${this.resourceUrl}/${getAssetStatusIdentifier(assetStatus) as number}`, assetStatus, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IAssetStatus>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IAssetStatus[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addAssetStatusToCollectionIfMissing(
    assetStatusCollection: IAssetStatus[],
    ...assetStatusesToCheck: (IAssetStatus | null | undefined)[]
  ): IAssetStatus[] {
    const assetStatuses: IAssetStatus[] = assetStatusesToCheck.filter(isPresent);
    if (assetStatuses.length > 0) {
      const assetStatusCollectionIdentifiers = assetStatusCollection.map(assetStatusItem => getAssetStatusIdentifier(assetStatusItem)!);
      const assetStatusesToAdd = assetStatuses.filter(assetStatusItem => {
        const assetStatusIdentifier = getAssetStatusIdentifier(assetStatusItem);
        if (assetStatusIdentifier == null || assetStatusCollectionIdentifiers.includes(assetStatusIdentifier)) {
          return false;
        }
        assetStatusCollectionIdentifiers.push(assetStatusIdentifier);
        return true;
      });
      return [...assetStatusesToAdd, ...assetStatusCollection];
    }
    return assetStatusCollection;
  }
}
