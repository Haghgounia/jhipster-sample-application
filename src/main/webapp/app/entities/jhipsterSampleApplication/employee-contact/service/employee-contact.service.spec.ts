import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IEmployeeContact, EmployeeContact } from '../employee-contact.model';

import { EmployeeContactService } from './employee-contact.service';

describe('EmployeeContact Service', () => {
  let service: EmployeeContactService;
  let httpMock: HttpTestingController;
  let elemDefault: IEmployeeContact;
  let expectedResult: IEmployeeContact | IEmployeeContact[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(EmployeeContactService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      employeeContactId: 0,
      employeeId: 0,
      contactType: 0,
      contact: 'AAAAAAA',
    };
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = Object.assign({}, elemDefault);

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(elemDefault);
    });

    it('should create a EmployeeContact', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new EmployeeContact()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a EmployeeContact', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          employeeContactId: 1,
          employeeId: 1,
          contactType: 1,
          contact: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a EmployeeContact', () => {
      const patchObject = Object.assign(
        {
          employeeContactId: 1,
          employeeId: 1,
          contactType: 1,
          contact: 'BBBBBB',
        },
        new EmployeeContact()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of EmployeeContact', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          employeeContactId: 1,
          employeeId: 1,
          contactType: 1,
          contact: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toContainEqual(expected);
    });

    it('should delete a EmployeeContact', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addEmployeeContactToCollectionIfMissing', () => {
      it('should add a EmployeeContact to an empty array', () => {
        const employeeContact: IEmployeeContact = { id: 123 };
        expectedResult = service.addEmployeeContactToCollectionIfMissing([], employeeContact);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(employeeContact);
      });

      it('should not add a EmployeeContact to an array that contains it', () => {
        const employeeContact: IEmployeeContact = { id: 123 };
        const employeeContactCollection: IEmployeeContact[] = [
          {
            ...employeeContact,
          },
          { id: 456 },
        ];
        expectedResult = service.addEmployeeContactToCollectionIfMissing(employeeContactCollection, employeeContact);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a EmployeeContact to an array that doesn't contain it", () => {
        const employeeContact: IEmployeeContact = { id: 123 };
        const employeeContactCollection: IEmployeeContact[] = [{ id: 456 }];
        expectedResult = service.addEmployeeContactToCollectionIfMissing(employeeContactCollection, employeeContact);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(employeeContact);
      });

      it('should add only unique EmployeeContact to an array', () => {
        const employeeContactArray: IEmployeeContact[] = [{ id: 123 }, { id: 456 }, { id: 26749 }];
        const employeeContactCollection: IEmployeeContact[] = [{ id: 123 }];
        expectedResult = service.addEmployeeContactToCollectionIfMissing(employeeContactCollection, ...employeeContactArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const employeeContact: IEmployeeContact = { id: 123 };
        const employeeContact2: IEmployeeContact = { id: 456 };
        expectedResult = service.addEmployeeContactToCollectionIfMissing([], employeeContact, employeeContact2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(employeeContact);
        expect(expectedResult).toContain(employeeContact2);
      });

      it('should accept null and undefined values', () => {
        const employeeContact: IEmployeeContact = { id: 123 };
        expectedResult = service.addEmployeeContactToCollectionIfMissing([], null, employeeContact, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(employeeContact);
      });

      it('should return initial array if no EmployeeContact is added', () => {
        const employeeContactCollection: IEmployeeContact[] = [{ id: 123 }];
        expectedResult = service.addEmployeeContactToCollectionIfMissing(employeeContactCollection, undefined, null);
        expect(expectedResult).toEqual(employeeContactCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
