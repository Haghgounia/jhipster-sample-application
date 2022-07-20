import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IEmployeeContact, EmployeeContact } from '../employee-contact.model';
import { EmployeeContactService } from '../service/employee-contact.service';

@Injectable({ providedIn: 'root' })
export class EmployeeContactRoutingResolveService implements Resolve<IEmployeeContact> {
  constructor(protected service: EmployeeContactService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IEmployeeContact> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((employeeContact: HttpResponse<EmployeeContact>) => {
          if (employeeContact.body) {
            return of(employeeContact.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new EmployeeContact());
  }
}
