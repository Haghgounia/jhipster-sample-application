import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, ActivatedRoute, Router, convertToParamMap } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { IRuralDistrict, RuralDistrict } from '../rural-district.model';
import { RuralDistrictService } from '../service/rural-district.service';

import { RuralDistrictRoutingResolveService } from './rural-district-routing-resolve.service';

describe('RuralDistrict routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let routingResolveService: RuralDistrictRoutingResolveService;
  let service: RuralDistrictService;
  let resultRuralDistrict: IRuralDistrict | undefined;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: {
            snapshot: {
              paramMap: convertToParamMap({}),
            },
          },
        },
      ],
    });
    mockRouter = TestBed.inject(Router);
    jest.spyOn(mockRouter, 'navigate').mockImplementation(() => Promise.resolve(true));
    mockActivatedRouteSnapshot = TestBed.inject(ActivatedRoute).snapshot;
    routingResolveService = TestBed.inject(RuralDistrictRoutingResolveService);
    service = TestBed.inject(RuralDistrictService);
    resultRuralDistrict = undefined;
  });

  describe('resolve', () => {
    it('should return IRuralDistrict returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultRuralDistrict = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultRuralDistrict).toEqual({ id: 123 });
    });

    it('should return new IRuralDistrict if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultRuralDistrict = result;
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultRuralDistrict).toEqual(new RuralDistrict());
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as RuralDistrict })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultRuralDistrict = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultRuralDistrict).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
