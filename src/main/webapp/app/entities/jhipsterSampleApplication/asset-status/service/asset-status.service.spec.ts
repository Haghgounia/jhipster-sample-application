import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IAssetStatus, AssetStatus } from '../asset-status.model';

import { AssetStatusService } from './asset-status.service';

describe('AssetStatus Service', () => {
  let service: AssetStatusService;
  let httpMock: HttpTestingController;
  let elemDefault: IAssetStatus;
  let expectedResult: IAssetStatus | IAssetStatus[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(AssetStatusService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      assetStatusId: 0,
      assetStatusName: 'AAAAAAA',
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

    it('should create a AssetStatus', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new AssetStatus()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a AssetStatus', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          assetStatusId: 1,
          assetStatusName: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a AssetStatus', () => {
      const patchObject = Object.assign({}, new AssetStatus());

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of AssetStatus', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          assetStatusId: 1,
          assetStatusName: 'BBBBBB',
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

    it('should delete a AssetStatus', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addAssetStatusToCollectionIfMissing', () => {
      it('should add a AssetStatus to an empty array', () => {
        const assetStatus: IAssetStatus = { id: 123 };
        expectedResult = service.addAssetStatusToCollectionIfMissing([], assetStatus);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(assetStatus);
      });

      it('should not add a AssetStatus to an array that contains it', () => {
        const assetStatus: IAssetStatus = { id: 123 };
        const assetStatusCollection: IAssetStatus[] = [
          {
            ...assetStatus,
          },
          { id: 456 },
        ];
        expectedResult = service.addAssetStatusToCollectionIfMissing(assetStatusCollection, assetStatus);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a AssetStatus to an array that doesn't contain it", () => {
        const assetStatus: IAssetStatus = { id: 123 };
        const assetStatusCollection: IAssetStatus[] = [{ id: 456 }];
        expectedResult = service.addAssetStatusToCollectionIfMissing(assetStatusCollection, assetStatus);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(assetStatus);
      });

      it('should add only unique AssetStatus to an array', () => {
        const assetStatusArray: IAssetStatus[] = [{ id: 123 }, { id: 456 }, { id: 61368 }];
        const assetStatusCollection: IAssetStatus[] = [{ id: 123 }];
        expectedResult = service.addAssetStatusToCollectionIfMissing(assetStatusCollection, ...assetStatusArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const assetStatus: IAssetStatus = { id: 123 };
        const assetStatus2: IAssetStatus = { id: 456 };
        expectedResult = service.addAssetStatusToCollectionIfMissing([], assetStatus, assetStatus2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(assetStatus);
        expect(expectedResult).toContain(assetStatus2);
      });

      it('should accept null and undefined values', () => {
        const assetStatus: IAssetStatus = { id: 123 };
        expectedResult = service.addAssetStatusToCollectionIfMissing([], null, assetStatus, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(assetStatus);
      });

      it('should return initial array if no AssetStatus is added', () => {
        const assetStatusCollection: IAssetStatus[] = [{ id: 123 }];
        expectedResult = service.addAssetStatusToCollectionIfMissing(assetStatusCollection, undefined, null);
        expect(expectedResult).toEqual(assetStatusCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
