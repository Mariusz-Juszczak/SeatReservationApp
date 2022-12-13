import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { NotificationAlertDetailComponent } from './notification-alert-detail.component';

describe('NotificationAlert Management Detail Component', () => {
  let comp: NotificationAlertDetailComponent;
  let fixture: ComponentFixture<NotificationAlertDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [NotificationAlertDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ notificationAlert: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(NotificationAlertDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(NotificationAlertDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load notificationAlert on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.notificationAlert).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
