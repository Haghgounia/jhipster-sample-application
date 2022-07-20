import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IRuralDistrict, getRuralDistrictIdentifier } from '../rural-district.model';

export type EntityResponseType = HttpResponse<IRuralDistrict>;
export type EntityArrayResponseType = HttpResponse<IRuralDistrict[]>;

@Injectable({ providedIn: 'root' })
export class RuralDistrictService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/rural-districts', 'jhipstersampleapplication');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(ruralDistrict: IRuralDistrict): Observable<EntityResponseType> {
    return this.http.post<IRuralDistrict>(this.resourceUrl, ruralDistrict, { observe: 'response' });
  }

  update(ruralDistrict: IRuralDistrict): Observable<EntityResponseType> {
    return this.http.put<IRuralDistrict>(`${this.resourceUrl}/${getRuralDistrictIdentifier(ruralDistrict) as number}`, ruralDistrict, {
      observe: 'response',
    });
  }

  partialUpdate(ruralDistrict: IRuralDistrict): Observable<EntityResponseType> {
    return this.http.patch<IRuralDistrict>(`${this.resourceUrl}/${getRuralDistrictIdentifier(ruralDistrict) as number}`, ruralDistrict, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IRuralDistrict>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IRuralDistrict[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addRuralDistrictToCollectionIfMissing(
    ruralDistrictCollection: IRuralDistrict[],
    ...ruralDistrictsToCheck: (IRuralDistrict | null | undefined)[]
  ): IRuralDistrict[] {
    const ruralDistricts: IRuralDistrict[] = ruralDistrictsToCheck.filter(isPresent);
    if (ruralDistricts.length > 0) {
      const ruralDistrictCollectionIdentifiers = ruralDistrictCollection.map(
        ruralDistrictItem => getRuralDistrictIdentifier(ruralDistrictItem)!
      );
      const ruralDistrictsToAdd = ruralDistricts.filter(ruralDistrictItem => {
        const ruralDistrictIdentifier = getRuralDistrictIdentifier(ruralDistrictItem);
        if (ruralDistrictIdentifier == null || ruralDistrictCollectionIdentifiers.includes(ruralDistrictIdentifier)) {
          return false;
        }
        ruralDistrictCollectionIdentifiers.push(ruralDistrictIdentifier);
        return true;
      });
      return [...ruralDistrictsToAdd, ...ruralDistrictCollection];
    }
    return ruralDistrictCollection;
  }
}
