import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IJobOpening, JobOpening } from '../job-opening.model';

import { JobOpeningService } from './job-opening.service';

describe('JobOpening Service', () => {
  let service: JobOpeningService;
  let httpMock: HttpTestingController;
  let elemDefault: IJobOpening;
  let expectedResult: IJobOpening | IJobOpening[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(JobOpeningService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      postingTitle: 'AAAAAAA',
      jobStatus: 0,
      hiringLead: 0,
      departmentId: 0,
      employmentType: 0,
      minimumExperience: 0,
      jobDescription: 'AAAAAAA',
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

    it('should create a JobOpening', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new JobOpening()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a JobOpening', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          postingTitle: 'BBBBBB',
          jobStatus: 1,
          hiringLead: 1,
          departmentId: 1,
          employmentType: 1,
          minimumExperience: 1,
          jobDescription: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a JobOpening', () => {
      const patchObject = Object.assign(
        {
          hiringLead: 1,
          employmentType: 1,
          jobDescription: 'BBBBBB',
        },
        new JobOpening()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of JobOpening', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          postingTitle: 'BBBBBB',
          jobStatus: 1,
          hiringLead: 1,
          departmentId: 1,
          employmentType: 1,
          minimumExperience: 1,
          jobDescription: 'BBBBBB',
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

    it('should delete a JobOpening', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addJobOpeningToCollectionIfMissing', () => {
      it('should add a JobOpening to an empty array', () => {
        const jobOpening: IJobOpening = { id: 123 };
        expectedResult = service.addJobOpeningToCollectionIfMissing([], jobOpening);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(jobOpening);
      });

      it('should not add a JobOpening to an array that contains it', () => {
        const jobOpening: IJobOpening = { id: 123 };
        const jobOpeningCollection: IJobOpening[] = [
          {
            ...jobOpening,
          },
          { id: 456 },
        ];
        expectedResult = service.addJobOpeningToCollectionIfMissing(jobOpeningCollection, jobOpening);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a JobOpening to an array that doesn't contain it", () => {
        const jobOpening: IJobOpening = { id: 123 };
        const jobOpeningCollection: IJobOpening[] = [{ id: 456 }];
        expectedResult = service.addJobOpeningToCollectionIfMissing(jobOpeningCollection, jobOpening);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(jobOpening);
      });

      it('should add only unique JobOpening to an array', () => {
        const jobOpeningArray: IJobOpening[] = [{ id: 123 }, { id: 456 }, { id: 80389 }];
        const jobOpeningCollection: IJobOpening[] = [{ id: 123 }];
        expectedResult = service.addJobOpeningToCollectionIfMissing(jobOpeningCollection, ...jobOpeningArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const jobOpening: IJobOpening = { id: 123 };
        const jobOpening2: IJobOpening = { id: 456 };
        expectedResult = service.addJobOpeningToCollectionIfMissing([], jobOpening, jobOpening2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(jobOpening);
        expect(expectedResult).toContain(jobOpening2);
      });

      it('should accept null and undefined values', () => {
        const jobOpening: IJobOpening = { id: 123 };
        expectedResult = service.addJobOpeningToCollectionIfMissing([], null, jobOpening, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(jobOpening);
      });

      it('should return initial array if no JobOpening is added', () => {
        const jobOpeningCollection: IJobOpening[] = [{ id: 123 }];
        expectedResult = service.addJobOpeningToCollectionIfMissing(jobOpeningCollection, undefined, null);
        expect(expectedResult).toEqual(jobOpeningCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
