import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, ActivatedRoute, Router, convertToParamMap } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { IAssetAssign, AssetAssign } from '../asset-assign.model';
import { AssetAssignService } from '../service/asset-assign.service';

import { AssetAssignRoutingResolveService } from './asset-assign-routing-resolve.service';

describe('AssetAssign routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let routingResolveService: AssetAssignRoutingResolveService;
  let service: AssetAssignService;
  let resultAssetAssign: IAssetAssign | undefined;

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
    routingResolveService = TestBed.inject(AssetAssignRoutingResolveService);
    service = TestBed.inject(AssetAssignService);
    resultAssetAssign = undefined;
  });

  describe('resolve', () => {
    it('should return IAssetAssign returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultAssetAssign = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultAssetAssign).toEqual({ id: 123 });
    });

    it('should return new IAssetAssign if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultAssetAssign = result;
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultAssetAssign).toEqual(new AssetAssign());
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as AssetAssign })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultAssetAssign = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultAssetAssign).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
