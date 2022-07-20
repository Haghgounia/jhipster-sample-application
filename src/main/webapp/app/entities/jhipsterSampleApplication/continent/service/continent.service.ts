import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IContinent, getContinentIdentifier } from '../continent.model';

export type EntityResponseType = HttpResponse<IContinent>;
export type EntityArrayResponseType = HttpResponse<IContinent[]>;

@Injectable({ providedIn: 'root' })
export class ContinentService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/continents', 'jhipstersampleapplication');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(continent: IContinent): Observable<EntityResponseType> {
    return this.http.post<IContinent>(this.resourceUrl, continent, { observe: 'response' });
  }

  update(continent: IContinent): Observable<EntityResponseType> {
    return this.http.put<IContinent>(`${this.resourceUrl}/${getContinentIdentifier(continent) as number}`, continent, {
      observe: 'response',
    });
  }

  partialUpdate(continent: IContinent): Observable<EntityResponseType> {
    return this.http.patch<IContinent>(`${this.resourceUrl}/${getContinentIdentifier(continent) as number}`, continent, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IContinent>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IContinent[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addContinentToCollectionIfMissing(
    continentCollection: IContinent[],
    ...continentsToCheck: (IContinent | null | undefined)[]
  ): IContinent[] {
    const continents: IContinent[] = continentsToCheck.filter(isPresent);
    if (continents.length > 0) {
      const continentCollectionIdentifiers = continentCollection.map(continentItem => getContinentIdentifier(continentItem)!);
      const continentsToAdd = continents.filter(continentItem => {
        const continentIdentifier = getContinentIdentifier(continentItem);
        if (continentIdentifier == null || continentCollectionIdentifiers.includes(continentIdentifier)) {
          return false;
        }
        continentCollectionIdentifiers.push(continentIdentifier);
        return true;
      });
      return [...continentsToAdd, ...continentCollection];
    }
    return continentCollection;
  }
}
