import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IEmployeeContact, getEmployeeContactIdentifier } from '../employee-contact.model';

export type EntityResponseType = HttpResponse<IEmployeeContact>;
export type EntityArrayResponseType = HttpResponse<IEmployeeContact[]>;

@Injectable({ providedIn: 'root' })
export class EmployeeContactService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/employee-contacts', 'jhipstersampleapplication');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(employeeContact: IEmployeeContact): Observable<EntityResponseType> {
    return this.http.post<IEmployeeContact>(this.resourceUrl, employeeContact, { observe: 'response' });
  }

  update(employeeContact: IEmployeeContact): Observable<EntityResponseType> {
    return this.http.put<IEmployeeContact>(
      `${this.resourceUrl}/${getEmployeeContactIdentifier(employeeContact) as number}`,
      employeeContact,
      { observe: 'response' }
    );
  }

  partialUpdate(employeeContact: IEmployeeContact): Observable<EntityResponseType> {
    return this.http.patch<IEmployeeContact>(
      `${this.resourceUrl}/${getEmployeeContactIdentifier(employeeContact) as number}`,
      employeeContact,
      { observe: 'response' }
    );
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IEmployeeContact>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IEmployeeContact[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addEmployeeContactToCollectionIfMissing(
    employeeContactCollection: IEmployeeContact[],
    ...employeeContactsToCheck: (IEmployeeContact | null | undefined)[]
  ): IEmployeeContact[] {
    const employeeContacts: IEmployeeContact[] = employeeContactsToCheck.filter(isPresent);
    if (employeeContacts.length > 0) {
      const employeeContactCollectionIdentifiers = employeeContactCollection.map(
        employeeContactItem => getEmployeeContactIdentifier(employeeContactItem)!
      );
      const employeeContactsToAdd = employeeContacts.filter(employeeContactItem => {
        const employeeContactIdentifier = getEmployeeContactIdentifier(employeeContactItem);
        if (employeeContactIdentifier == null || employeeContactCollectionIdentifiers.includes(employeeContactIdentifier)) {
          return false;
        }
        employeeContactCollectionIdentifiers.push(employeeContactIdentifier);
        return true;
      });
      return [...employeeContactsToAdd, ...employeeContactCollection];
    }
    return employeeContactCollection;
  }
}
