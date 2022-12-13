import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { RestrictionsFormService } from './restrictions-form.service';
import { RestrictionsService } from '../service/restrictions.service';
import { IRestrictions } from '../restrictions.model';

import { RestrictionsUpdateComponent } from './restrictions-update.component';

describe('Restrictions Management Update Component', () => {
  let comp: RestrictionsUpdateComponent;
  let fixture: ComponentFixture<RestrictionsUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let restrictionsFormService: RestrictionsFormService;
  let restrictionsService: RestrictionsService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [RestrictionsUpdateComponent],
      providers: [
        FormBuilder,
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    })
      .overrideTemplate(RestrictionsUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(RestrictionsUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    restrictionsFormService = TestBed.inject(RestrictionsFormService);
    restrictionsService = TestBed.inject(RestrictionsService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const restrictions: IRestrictions = { id: 456 };

      activatedRoute.data = of({ restrictions });
      comp.ngOnInit();

      expect(comp.restrictions).toEqual(restrictions);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IRestrictions>>();
      const restrictions = { id: 123 };
      jest.spyOn(restrictionsFormService, 'getRestrictions').mockReturnValue(restrictions);
      jest.spyOn(restrictionsService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ restrictions });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: restrictions }));
      saveSubject.complete();

      // THEN
      expect(restrictionsFormService.getRestrictions).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(restrictionsService.update).toHaveBeenCalledWith(expect.objectContaining(restrictions));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IRestrictions>>();
      const restrictions = { id: 123 };
      jest.spyOn(restrictionsFormService, 'getRestrictions').mockReturnValue({ id: null });
      jest.spyOn(restrictionsService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ restrictions: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: restrictions }));
      saveSubject.complete();

      // THEN
      expect(restrictionsFormService.getRestrictions).toHaveBeenCalled();
      expect(restrictionsService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IRestrictions>>();
      const restrictions = { id: 123 };
      jest.spyOn(restrictionsService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ restrictions });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(restrictionsService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
