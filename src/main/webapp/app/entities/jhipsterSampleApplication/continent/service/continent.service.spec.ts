import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IContinent, Continent } from '../continent.model';

import { ContinentService } from './continent.service';

describe('Continent Service', () => {
  let service: ContinentService;
  let httpMock: HttpTestingController;
  let elemDefault: IContinent;
  let expectedResult: IContinent | IContinent[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(ContinentService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      continentId: 0,
      continentCode: 'AAAAAAA',
      continentName: 'AAAAAAA',
      continentEnglishName: 'AAAAAAA',
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

    it('should create a Continent', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new Continent()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Continent', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          continentId: 1,
          continentCode: 'BBBBBB',
          continentName: 'BBBBBB',
          continentEnglishName: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Continent', () => {
      const patchObject = Object.assign(
        {
          continentId: 1,
          continentName: 'BBBBBB',
          continentEnglishName: 'BBBBBB',
        },
        new Continent()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Continent', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          continentId: 1,
          continentCode: 'BBBBBB',
          continentName: 'BBBBBB',
          continentEnglishName: 'BBBBBB',
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

    it('should delete a Continent', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addContinentToCollectionIfMissing', () => {
      it('should add a Continent to an empty array', () => {
        const continent: IContinent = { id: 123 };
        expectedResult = service.addContinentToCollectionIfMissing([], continent);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(continent);
      });

      it('should not add a Continent to an array that contains it', () => {
        const continent: IContinent = { id: 123 };
        const continentCollection: IContinent[] = [
          {
            ...continent,
          },
          { id: 456 },
        ];
        expectedResult = service.addContinentToCollectionIfMissing(continentCollection, continent);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Continent to an array that doesn't contain it", () => {
        const continent: IContinent = { id: 123 };
        const continentCollection: IContinent[] = [{ id: 456 }];
        expectedResult = service.addContinentToCollectionIfMissing(continentCollection, continent);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(continent);
      });

      it('should add only unique Continent to an array', () => {
        const continentArray: IContinent[] = [{ id: 123 }, { id: 456 }, { id: 44027 }];
        const continentCollection: IContinent[] = [{ id: 123 }];
        expectedResult = service.addContinentToCollectionIfMissing(continentCollection, ...continentArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const continent: IContinent = { id: 123 };
        const continent2: IContinent = { id: 456 };
        expectedResult = service.addContinentToCollectionIfMissing([], continent, continent2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(continent);
        expect(expectedResult).toContain(continent2);
      });

      it('should accept null and undefined values', () => {
        const continent: IContinent = { id: 123 };
        expectedResult = service.addContinentToCollectionIfMissing([], null, continent, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(continent);
      });

      it('should return initial array if no Continent is added', () => {
        const continentCollection: IContinent[] = [{ id: 123 }];
        expectedResult = service.addContinentToCollectionIfMissing(continentCollection, undefined, null);
        expect(expectedResult).toEqual(continentCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
