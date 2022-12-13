import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { RestrictionsDetailComponent } from './restrictions-detail.component';

describe('Restrictions Management Detail Component', () => {
  let comp: RestrictionsDetailComponent;
  let fixture: ComponentFixture<RestrictionsDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [RestrictionsDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ restrictions: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(RestrictionsDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(RestrictionsDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load restrictions on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.restrictions).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
