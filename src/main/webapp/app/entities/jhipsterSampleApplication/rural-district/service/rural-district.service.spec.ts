import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IRuralDistrict, RuralDistrict } from '../rural-district.model';

import { RuralDistrictService } from './rural-district.service';

describe('RuralDistrict Service', () => {
  let service: RuralDistrictService;
  let httpMock: HttpTestingController;
  let elemDefault: IRuralDistrict;
  let expectedResult: IRuralDistrict | IRuralDistrict[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(RuralDistrictService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      ruralDistrictId: 0,
      ruralDistrictCode: 'AAAAAAA',
      ruralDistrictName: 'AAAAAAA',
      ruralDistrictEnglishName: 'AAAAAAA',
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

    it('should create a RuralDistrict', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new RuralDistrict()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a RuralDistrict', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          ruralDistrictId: 1,
          ruralDistrictCode: 'BBBBBB',
          ruralDistrictName: 'BBBBBB',
          ruralDistrictEnglishName: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a RuralDistrict', () => {
      const patchObject = Object.assign(
        {
          ruralDistrictId: 1,
          ruralDistrictCode: 'BBBBBB',
          ruralDistrictName: 'BBBBBB',
          ruralDistrictEnglishName: 'BBBBBB',
        },
        new RuralDistrict()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of RuralDistrict', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          ruralDistrictId: 1,
          ruralDistrictCode: 'BBBBBB',
          ruralDistrictName: 'BBBBBB',
          ruralDistrictEnglishName: 'BBBBBB',
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

    it('should delete a RuralDistrict', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addRuralDistrictToCollectionIfMissing', () => {
      it('should add a RuralDistrict to an empty array', () => {
        const ruralDistrict: IRuralDistrict = { id: 123 };
        expectedResult = service.addRuralDistrictToCollectionIfMissing([], ruralDistrict);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(ruralDistrict);
      });

      it('should not add a RuralDistrict to an array that contains it', () => {
        const ruralDistrict: IRuralDistrict = { id: 123 };
        const ruralDistrictCollection: IRuralDistrict[] = [
          {
            ...ruralDistrict,
          },
          { id: 456 },
        ];
        expectedResult = service.addRuralDistrictToCollectionIfMissing(ruralDistrictCollection, ruralDistrict);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a RuralDistrict to an array that doesn't contain it", () => {
        const ruralDistrict: IRuralDistrict = { id: 123 };
        const ruralDistrictCollection: IRuralDistrict[] = [{ id: 456 }];
        expectedResult = service.addRuralDistrictToCollectionIfMissing(ruralDistrictCollection, ruralDistrict);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(ruralDistrict);
      });

      it('should add only unique RuralDistrict to an array', () => {
        const ruralDistrictArray: IRuralDistrict[] = [{ id: 123 }, { id: 456 }, { id: 689 }];
        const ruralDistrictCollection: IRuralDistrict[] = [{ id: 123 }];
        expectedResult = service.addRuralDistrictToCollectionIfMissing(ruralDistrictCollection, ...ruralDistrictArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const ruralDistrict: IRuralDistrict = { id: 123 };
        const ruralDistrict2: IRuralDistrict = { id: 456 };
        expectedResult = service.addRuralDistrictToCollectionIfMissing([], ruralDistrict, ruralDistrict2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(ruralDistrict);
        expect(expectedResult).toContain(ruralDistrict2);
      });

      it('should accept null and undefined values', () => {
        const ruralDistrict: IRuralDistrict = { id: 123 };
        expectedResult = service.addRuralDistrictToCollectionIfMissing([], null, ruralDistrict, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(ruralDistrict);
      });

      it('should return initial array if no RuralDistrict is added', () => {
        const ruralDistrictCollection: IRuralDistrict[] = [{ id: 123 }];
        expectedResult = service.addRuralDistrictToCollectionIfMissing(ruralDistrictCollection, undefined, null);
        expect(expectedResult).toEqual(ruralDistrictCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
