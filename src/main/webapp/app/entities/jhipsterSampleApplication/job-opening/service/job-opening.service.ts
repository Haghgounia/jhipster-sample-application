import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IJobOpening, getJobOpeningIdentifier } from '../job-opening.model';

export type EntityResponseType = HttpResponse<IJobOpening>;
export type EntityArrayResponseType = HttpResponse<IJobOpening[]>;

@Injectable({ providedIn: 'root' })
export class JobOpeningService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/job-openings', 'jhipstersampleapplication');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(jobOpening: IJobOpening): Observable<EntityResponseType> {
    return this.http.post<IJobOpening>(this.resourceUrl, jobOpening, { observe: 'response' });
  }

  update(jobOpening: IJobOpening): Observable<EntityResponseType> {
    return this.http.put<IJobOpening>(`${this.resourceUrl}/${getJobOpeningIdentifier(jobOpening) as number}`, jobOpening, {
      observe: 'response',
    });
  }

  partialUpdate(jobOpening: IJobOpening): Observable<EntityResponseType> {
    return this.http.patch<IJobOpening>(`${this.resourceUrl}/${getJobOpeningIdentifier(jobOpening) as number}`, jobOpening, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IJobOpening>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IJobOpening[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addJobOpeningToCollectionIfMissing(
    jobOpeningCollection: IJobOpening[],
    ...jobOpeningsToCheck: (IJobOpening | null | undefined)[]
  ): IJobOpening[] {
    const jobOpenings: IJobOpening[] = jobOpeningsToCheck.filter(isPresent);
    if (jobOpenings.length > 0) {
      const jobOpeningCollectionIdentifiers = jobOpeningCollection.map(jobOpeningItem => getJobOpeningIdentifier(jobOpeningItem)!);
      const jobOpeningsToAdd = jobOpenings.filter(jobOpeningItem => {
        const jobOpeningIdentifier = getJobOpeningIdentifier(jobOpeningItem);
        if (jobOpeningIdentifier == null || jobOpeningCollectionIdentifiers.includes(jobOpeningIdentifier)) {
          return false;
        }
        jobOpeningCollectionIdentifiers.push(jobOpeningIdentifier);
        return true;
      });
      return [...jobOpeningsToAdd, ...jobOpeningCollection];
    }
    return jobOpeningCollection;
  }
}
