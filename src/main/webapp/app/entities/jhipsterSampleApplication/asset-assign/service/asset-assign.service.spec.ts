import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IAssetAssign, AssetAssign } from '../asset-assign.model';

import { AssetAssignService } from './asset-assign.service';

describe('AssetAssign Service', () => {
  let service: AssetAssignService;
  let httpMock: HttpTestingController;
  let elemDefault: IAssetAssign;
  let expectedResult: IAssetAssign | IAssetAssign[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(AssetAssignService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      assetId: 0,
      employeeId: 0,
      assignDate: 0,
      returnDate: 0,
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

    it('should create a AssetAssign', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new AssetAssign()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a AssetAssign', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          assetId: 1,
          employeeId: 1,
          assignDate: 1,
          returnDate: 1,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a AssetAssign', () => {
      const patchObject = Object.assign(
        {
          assetId: 1,
          employeeId: 1,
          assignDate: 1,
          returnDate: 1,
        },
        new AssetAssign()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of AssetAssign', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          assetId: 1,
          employeeId: 1,
          assignDate: 1,
          returnDate: 1,
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

    it('should delete a AssetAssign', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addAssetAssignToCollectionIfMissing', () => {
      it('should add a AssetAssign to an empty array', () => {
        const assetAssign: IAssetAssign = { id: 123 };
        expectedResult = service.addAssetAssignToCollectionIfMissing([], assetAssign);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(assetAssign);
      });

      it('should not add a AssetAssign to an array that contains it', () => {
        const assetAssign: IAssetAssign = { id: 123 };
        const assetAssignCollection: IAssetAssign[] = [
          {
            ...assetAssign,
          },
          { id: 456 },
        ];
        expectedResult = service.addAssetAssignToCollectionIfMissing(assetAssignCollection, assetAssign);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a AssetAssign to an array that doesn't contain it", () => {
        const assetAssign: IAssetAssign = { id: 123 };
        const assetAssignCollection: IAssetAssign[] = [{ id: 456 }];
        expectedResult = service.addAssetAssignToCollectionIfMissing(assetAssignCollection, assetAssign);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(assetAssign);
      });

      it('should add only unique AssetAssign to an array', () => {
        const assetAssignArray: IAssetAssign[] = [{ id: 123 }, { id: 456 }, { id: 38224 }];
        const assetAssignCollection: IAssetAssign[] = [{ id: 123 }];
        expectedResult = service.addAssetAssignToCollectionIfMissing(assetAssignCollection, ...assetAssignArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const assetAssign: IAssetAssign = { id: 123 };
        const assetAssign2: IAssetAssign = { id: 456 };
        expectedResult = service.addAssetAssignToCollectionIfMissing([], assetAssign, assetAssign2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(assetAssign);
        expect(expectedResult).toContain(assetAssign2);
      });

      it('should accept null and undefined values', () => {
        const assetAssign: IAssetAssign = { id: 123 };
        expectedResult = service.addAssetAssignToCollectionIfMissing([], null, assetAssign, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(assetAssign);
      });

      it('should return initial array if no AssetAssign is added', () => {
        const assetAssignCollection: IAssetAssign[] = [{ id: 123 }];
        expectedResult = service.addAssetAssignToCollectionIfMissing(assetAssignCollection, undefined, null);
        expect(expectedResult).toEqual(assetAssignCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
