import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IJobOpening, JobOpening } from '../job-opening.model';
import { JobOpeningService } from '../service/job-opening.service';

@Injectable({ providedIn: 'root' })
export class JobOpeningRoutingResolveService implements Resolve<IJobOpening> {
  constructor(protected service: JobOpeningService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IJobOpening> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((jobOpening: HttpResponse<JobOpening>) => {
          if (jobOpening.body) {
            return of(jobOpening.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new JobOpening());
  }
}
