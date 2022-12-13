import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { INotificationAlert } from '../notification-alert.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../notification-alert.test-samples';

import { NotificationAlertService, RestNotificationAlert } from './notification-alert.service';

const requireRestSample: RestNotificationAlert = {
  ...sampleWithRequiredData,
  createdAt: sampleWithRequiredData.createdAt?.toJSON(),
};

describe('NotificationAlert Service', () => {
  let service: NotificationAlertService;
  let httpMock: HttpTestingController;
  let expectedResult: INotificationAlert | INotificationAlert[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(NotificationAlertService);
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

    it('should create a NotificationAlert', () => {
      // eslint-disable-next-line @typescript-eslint/no-unused-vars
      const notificationAlert = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(notificationAlert).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a NotificationAlert', () => {
      const notificationAlert = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(notificationAlert).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of NotificationAlert', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a NotificationAlert', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addNotificationAlertToCollectionIfMissing', () => {
      it('should add a NotificationAlert to an empty array', () => {
        const notificationAlert: INotificationAlert = sampleWithRequiredData;
        expectedResult = service.addNotificationAlertToCollectionIfMissing([], notificationAlert);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(notificationAlert);
      });

      it('should not add a NotificationAlert to an array that contains it', () => {
        const notificationAlert: INotificationAlert = sampleWithRequiredData;
        const notificationAlertCollection: INotificationAlert[] = [
          {
            ...notificationAlert,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addNotificationAlertToCollectionIfMissing(notificationAlertCollection, notificationAlert);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a NotificationAlert to an array that doesn't contain it", () => {
        const notificationAlert: INotificationAlert = sampleWithRequiredData;
        const notificationAlertCollection: INotificationAlert[] = [sampleWithPartialData];
        expectedResult = service.addNotificationAlertToCollectionIfMissing(notificationAlertCollection, notificationAlert);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(notificationAlert);
      });

      it('should add only unique NotificationAlert to an array', () => {
        const notificationAlertArray: INotificationAlert[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const notificationAlertCollection: INotificationAlert[] = [sampleWithRequiredData];
        expectedResult = service.addNotificationAlertToCollectionIfMissing(notificationAlertCollection, ...notificationAlertArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const notificationAlert: INotificationAlert = sampleWithRequiredData;
        const notificationAlert2: INotificationAlert = sampleWithPartialData;
        expectedResult = service.addNotificationAlertToCollectionIfMissing([], notificationAlert, notificationAlert2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(notificationAlert);
        expect(expectedResult).toContain(notificationAlert2);
      });

      it('should accept null and undefined values', () => {
        const notificationAlert: INotificationAlert = sampleWithRequiredData;
        expectedResult = service.addNotificationAlertToCollectionIfMissing([], null, notificationAlert, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(notificationAlert);
      });

      it('should return initial array if no NotificationAlert is added', () => {
        const notificationAlertCollection: INotificationAlert[] = [sampleWithRequiredData];
        expectedResult = service.addNotificationAlertToCollectionIfMissing(notificationAlertCollection, undefined, null);
        expect(expectedResult).toEqual(notificationAlertCollection);
      });
    });

    describe('compareNotificationAlert', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareNotificationAlert(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareNotificationAlert(entity1, entity2);
        const compareResult2 = service.compareNotificationAlert(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareNotificationAlert(entity1, entity2);
        const compareResult2 = service.compareNotificationAlert(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareNotificationAlert(entity1, entity2);
        const compareResult2 = service.compareNotificationAlert(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
