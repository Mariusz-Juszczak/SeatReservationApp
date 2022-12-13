import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { RestrictionsFormService, RestrictionsFormGroup } from './restrictions-form.service';
import { IRestrictions } from '../restrictions.model';
import { RestrictionsService } from '../service/restrictions.service';
import { RestrictionType } from 'app/entities/enumerations/restriction-type.model';

@Component({
  selector: 'app-restrictions-update',
  templateUrl: './restrictions-update.component.html',
})
export class RestrictionsUpdateComponent implements OnInit {
  isSaving = false;
  restrictions: IRestrictions | null = null;
  restrictionTypeValues = Object.keys(RestrictionType);

  editForm: RestrictionsFormGroup = this.restrictionsFormService.createRestrictionsFormGroup();

  constructor(
    protected restrictionsService: RestrictionsService,
    protected restrictionsFormService: RestrictionsFormService,
    protected activatedRoute: ActivatedRoute
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ restrictions }) => {
      this.restrictions = restrictions;
      if (restrictions) {
        this.updateForm(restrictions);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const restrictions = this.restrictionsFormService.getRestrictions(this.editForm);
    if (restrictions.id !== null) {
      this.subscribeToSaveResponse(this.restrictionsService.update(restrictions));
    } else {
      this.subscribeToSaveResponse(this.restrictionsService.create(restrictions));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IRestrictions>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(restrictions: IRestrictions): void {
    this.restrictions = restrictions;
    this.restrictionsFormService.resetForm(this.editForm, restrictions);
  }
}
