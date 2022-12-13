import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { SeatReservedDetailComponent } from './seat-reserved-detail.component';

describe('SeatReserved Management Detail Component', () => {
  let comp: SeatReservedDetailComponent;
  let fixture: ComponentFixture<SeatReservedDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [SeatReservedDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ seatReserved: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(SeatReservedDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(SeatReservedDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load seatReserved on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.seatReserved).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
