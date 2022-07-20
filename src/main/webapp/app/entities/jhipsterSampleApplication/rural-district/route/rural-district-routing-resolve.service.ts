import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IRuralDistrict, RuralDistrict } from '../rural-district.model';
import { RuralDistrictService } from '../service/rural-district.service';

@Injectable({ providedIn: 'root' })
export class RuralDistrictRoutingResolveService implements Resolve<IRuralDistrict> {
  constructor(protected service: RuralDistrictService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IRuralDistrict> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((ruralDistrict: HttpResponse<RuralDistrict>) => {
          if (ruralDistrict.body) {
            return of(ruralDistrict.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new RuralDistrict());
  }
}
