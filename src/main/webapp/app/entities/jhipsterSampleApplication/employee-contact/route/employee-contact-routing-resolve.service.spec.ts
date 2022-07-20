import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, ActivatedRoute, Router, convertToParamMap } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { IEmployeeContact, EmployeeContact } from '../employee-contact.model';
import { EmployeeContactService } from '../service/employee-contact.service';

import { EmployeeContactRoutingResolveService } from './employee-contact-routing-resolve.service';

describe('EmployeeContact routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let routingResolveService: EmployeeContactRoutingResolveService;
  let service: EmployeeContactService;
  let resultEmployeeContact: IEmployeeContact | undefined;

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
    routingResolveService = TestBed.inject(EmployeeContactRoutingResolveService);
    service = TestBed.inject(EmployeeContactService);
    resultEmployeeContact = undefined;
  });

  describe('resolve', () => {
    it('should return IEmployeeContact returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultEmployeeContact = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultEmployeeContact).toEqual({ id: 123 });
    });

    it('should return new IEmployeeContact if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultEmployeeContact = result;
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultEmployeeContact).toEqual(new EmployeeContact());
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as EmployeeContact })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultEmployeeContact = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultEmployeeContact).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
