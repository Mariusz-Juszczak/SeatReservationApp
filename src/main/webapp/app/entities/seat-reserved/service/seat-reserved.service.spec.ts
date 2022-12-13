import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { ISeatReserved } from '../seat-reserved.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../seat-reserved.test-samples';

import { SeatReservedService, RestSeatReserved } from './seat-reserved.service';

const requireRestSample: RestSeatReserved = {
  ...sampleWithRequiredData,
  fromDate: sampleWithRequiredData.fromDate?.toJSON(),
  toDate: sampleWithRequiredData.toDate?.toJSON(),
  createdDate: sampleWithRequiredData.createdDate?.toJSON(),
};

describe('SeatReserved Service', () => {
  let service: SeatReservedService;
  let httpMock: HttpTestingController;
  let expectedResult: ISeatReserved | ISeatReserved[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(SeatReservedService);
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

    it('should create a SeatReserved', () => {
      // eslint-disable-next-line @typescript-eslint/no-unused-vars
      const seatReserved = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(seatReserved).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a SeatReserved', () => {
      const seatReserved = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(seatReserved).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of SeatReserved', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a SeatReserved', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addSeatReservedToCollectionIfMissing', () => {
      it('should add a SeatReserved to an empty array', () => {
        const seatReserved: ISeatReserved = sampleWithRequiredData;
        expectedResult = service.addSeatReservedToCollectionIfMissing([], seatReserved);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(seatReserved);
      });

      it('should not add a SeatReserved to an array that contains it', () => {
        const seatReserved: ISeatReserved = sampleWithRequiredData;
        const seatReservedCollection: ISeatReserved[] = [
          {
            ...seatReserved,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addSeatReservedToCollectionIfMissing(seatReservedCollection, seatReserved);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a SeatReserved to an array that doesn't contain it", () => {
        const seatReserved: ISeatReserved = sampleWithRequiredData;
        const seatReservedCollection: ISeatReserved[] = [sampleWithPartialData];
        expectedResult = service.addSeatReservedToCollectionIfMissing(seatReservedCollection, seatReserved);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(seatReserved);
      });

      it('should add only unique SeatReserved to an array', () => {
        const seatReservedArray: ISeatReserved[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const seatReservedCollection: ISeatReserved[] = [sampleWithRequiredData];
        expectedResult = service.addSeatReservedToCollectionIfMissing(seatReservedCollection, ...seatReservedArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const seatReserved: ISeatReserved = sampleWithRequiredData;
        const seatReserved2: ISeatReserved = sampleWithPartialData;
        expectedResult = service.addSeatReservedToCollectionIfMissing([], seatReserved, seatReserved2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(seatReserved);
        expect(expectedResult).toContain(seatReserved2);
      });

      it('should accept null and undefined values', () => {
        const seatReserved: ISeatReserved = sampleWithRequiredData;
        expectedResult = service.addSeatReservedToCollectionIfMissing([], null, seatReserved, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(seatReserved);
      });

      it('should return initial array if no SeatReserved is added', () => {
        const seatReservedCollection: ISeatReserved[] = [sampleWithRequiredData];
        expectedResult = service.addSeatReservedToCollectionIfMissing(seatReservedCollection, undefined, null);
        expect(expectedResult).toEqual(seatReservedCollection);
      });
    });

    describe('compareSeatReserved', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareSeatReserved(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareSeatReserved(entity1, entity2);
        const compareResult2 = service.compareSeatReserved(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareSeatReserved(entity1, entity2);
        const compareResult2 = service.compareSeatReserved(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareSeatReserved(entity1, entity2);
        const compareResult2 = service.compareSeatReserved(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
