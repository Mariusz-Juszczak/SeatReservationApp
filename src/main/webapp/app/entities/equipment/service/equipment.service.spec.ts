import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IEquipment } from '../equipment.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../equipment.test-samples';

import { EquipmentService } from './equipment.service';

const requireRestSample: IEquipment = {
  ...sampleWithRequiredData,
};

describe('Equipment Service', () => {
  let service: EquipmentService;
  let httpMock: HttpTestingController;
  let expectedResult: IEquipment | IEquipment[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(EquipmentService);
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

    it('should create a Equipment', () => {
      // eslint-disable-next-line @typescript-eslint/no-unused-vars
      const equipment = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(equipment).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Equipment', () => {
      const equipment = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(equipment).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Equipment', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a Equipment', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addEquipmentToCollectionIfMissing', () => {
      it('should add a Equipment to an empty array', () => {
        const equipment: IEquipment = sampleWithRequiredData;
        expectedResult = service.addEquipmentToCollectionIfMissing([], equipment);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(equipment);
      });

      it('should not add a Equipment to an array that contains it', () => {
        const equipment: IEquipment = sampleWithRequiredData;
        const equipmentCollection: IEquipment[] = [
          {
            ...equipment,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addEquipmentToCollectionIfMissing(equipmentCollection, equipment);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Equipment to an array that doesn't contain it", () => {
        const equipment: IEquipment = sampleWithRequiredData;
        const equipmentCollection: IEquipment[] = [sampleWithPartialData];
        expectedResult = service.addEquipmentToCollectionIfMissing(equipmentCollection, equipment);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(equipment);
      });

      it('should add only unique Equipment to an array', () => {
        const equipmentArray: IEquipment[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const equipmentCollection: IEquipment[] = [sampleWithRequiredData];
        expectedResult = service.addEquipmentToCollectionIfMissing(equipmentCollection, ...equipmentArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const equipment: IEquipment = sampleWithRequiredData;
        const equipment2: IEquipment = sampleWithPartialData;
        expectedResult = service.addEquipmentToCollectionIfMissing([], equipment, equipment2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(equipment);
        expect(expectedResult).toContain(equipment2);
      });

      it('should accept null and undefined values', () => {
        const equipment: IEquipment = sampleWithRequiredData;
        expectedResult = service.addEquipmentToCollectionIfMissing([], null, equipment, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(equipment);
      });

      it('should return initial array if no Equipment is added', () => {
        const equipmentCollection: IEquipment[] = [sampleWithRequiredData];
        expectedResult = service.addEquipmentToCollectionIfMissing(equipmentCollection, undefined, null);
        expect(expectedResult).toEqual(equipmentCollection);
      });
    });

    describe('compareEquipment', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareEquipment(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareEquipment(entity1, entity2);
        const compareResult2 = service.compareEquipment(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareEquipment(entity1, entity2);
        const compareResult2 = service.compareEquipment(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareEquipment(entity1, entity2);
        const compareResult2 = service.compareEquipment(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
