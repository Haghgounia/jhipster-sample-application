import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IContinent, Continent } from '../continent.model';
import { ContinentService } from '../service/continent.service';

@Injectable({ providedIn: 'root' })
export class ContinentRoutingResolveService implements Resolve<IContinent> {
  constructor(protected service: ContinentService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IContinent> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((continent: HttpResponse<Continent>) => {
          if (continent.body) {
            return of(continent.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Continent());
  }
}
