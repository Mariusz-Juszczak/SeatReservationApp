import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { EquipmentDetailComponent } from './equipment-detail.component';

describe('Equipment Management Detail Component', () => {
  let comp: EquipmentDetailComponent;
  let fixture: ComponentFixture<EquipmentDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [EquipmentDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ equipment: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(EquipmentDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(EquipmentDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load equipment on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.equipment).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
