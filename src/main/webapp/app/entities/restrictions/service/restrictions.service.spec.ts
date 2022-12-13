import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IRestrictions } from '../restrictions.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../restrictions.test-samples';

import { RestrictionsService, RestRestrictions } from './restrictions.service';

const requireRestSample: RestRestrictions = {
  ...sampleWithRequiredData,
  fromDate: sampleWithRequiredData.fromDate?.toJSON(),
  toDate: sampleWithRequiredData.toDate?.toJSON(),
};

describe('Restrictions Service', () => {
  let service: RestrictionsService;
  let httpMock: HttpTestingController;
  let expectedResult: IRestrictions | IRestrictions[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(RestrictionsService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should create a Restrictions', () => {
      // eslint-disable-next-line @typescript-eslint/no-unused-vars
      const restrictions = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(restrictions).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Restrictions', () => {
      const restrictions = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(restrictions).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Restrictions', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a Restrictions', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addRestrictionsToCollectionIfMissing', () => {
      it('should add a Restrictions to an empty array', () => {
        const restrictions: IRestrictions = sampleWithRequiredData;
        expectedResult = service.addRestrictionsToCollectionIfMissing([], restrictions);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(restrictions);
      });

      it('should not add a Restrictions to an array that contains it', () => {
        const restrictions: IRestrictions = sampleWithRequiredData;
        const restrictionsCollection: IRestrictions[] = [
          {
            ...restrictions,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addRestrictionsToCollectionIfMissing(restrictionsCollection, restrictions);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Restrictions to an array that doesn't contain it", () => {
        const restrictions: IRestrictions = sampleWithRequiredData;
        const restrictionsCollection: IRestrictions[] = [sampleWithPartialData];
        expectedResult = service.addRestrictionsToCollectionIfMissing(restrictionsCollection, restrictions);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(restrictions);
      });

      it('should add only unique Restrictions to an array', () => {
        const restrictionsArray: IRestrictions[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const restrictionsCollection: IRestrictions[] = [sampleWithRequiredData];
        expectedResult = service.addRestrictionsToCollectionIfMissing(restrictionsCollection, ...restrictionsArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const restrictions: IRestrictions = sampleWithRequiredData;
        const restrictions2: IRestrictions = sampleWithPartialData;
        expectedResult = service.addRestrictionsToCollectionIfMissing([], restrictions, restrictions2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(restrictions);
        expect(expectedResult).toContain(restrictions2);
      });

      it('should accept null and undefined values', () => {
        const restrictions: IRestrictions = sampleWithRequiredData;
        expectedResult = service.addRestrictionsToCollectionIfMissing([], null, restrictions, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(restrictions);
      });

      it('should return initial array if no Restrictions is added', () => {
        const restrictionsCollection: IRestrictions[] = [sampleWithRequiredData];
        expectedResult = service.addRestrictionsToCollectionIfMissing(restrictionsCollection, undefined, null);
        expect(expectedResult).toEqual(restrictionsCollection);
      });
    });

    describe('compareRestrictions', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareRestrictions(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareRestrictions(entity1, entity2);
        const compareResult2 = service.compareRestrictions(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareRestrictions(entity1, entity2);
        const compareResult2 = service.compareRestrictions(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareRestrictions(entity1, entity2);
        const compareResult2 = service.compareRestrictions(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
