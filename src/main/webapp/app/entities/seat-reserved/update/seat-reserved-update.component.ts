import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { SeatReservedFormService, SeatReservedFormGroup } from './seat-reserved-form.service';
import { ISeatReserved } from '../seat-reserved.model';
import { SeatReservedService } from '../service/seat-reserved.service';
import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/user.service';
import { ReservationStatus } from 'app/entities/enumerations/reservation-status.model';

@Component({
  selector: 'app-seat-reserved-update',
  templateUrl: './seat-reserved-update.component.html',
})
export class SeatReservedUpdateComponent implements OnInit {
  isSaving = false;
  seatReserved: ISeatReserved | null = null;
  reservationStatusValues = Object.keys(ReservationStatus);

  usersSharedCollection: IUser[] = [];

  editForm: SeatReservedFormGroup = this.seatReservedFormService.createSeatReservedFormGroup();

  constructor(
    protected seatReservedService: SeatReservedService,
    protected seatReservedFormService: SeatReservedFormService,
    protected userService: UserService,
    protected activatedRoute: ActivatedRoute
  ) {}

  compareUser = (o1: IUser | null, o2: IUser | null): boolean => this.userService.compareUser(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ seatReserved }) => {
      this.seatReserved = seatReserved;
      if (seatReserved) {
        this.updateForm(seatReserved);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const seatReserved = this.seatReservedFormService.getSeatReserved(this.editForm);
    if (seatReserved.id !== null) {
      this.subscribeToSaveResponse(this.seatReservedService.update(seatReserved));
    } else {
      this.subscribeToSaveResponse(this.seatReservedService.create(seatReserved));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ISeatReserved>>): void {
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

  protected updateForm(seatReserved: ISeatReserved): void {
    this.seatReserved = seatReserved;
    this.seatReservedFormService.resetForm(this.editForm, seatReserved);

    this.usersSharedCollection = this.userService.addUserToCollectionIfMissing<IUser>(this.usersSharedCollection, seatReserved.user);
  }

  protected loadRelationshipsOptions(): void {
    this.userService
      .query()
      .pipe(map((res: HttpResponse<IUser[]>) => res.body ?? []))
      .pipe(map((users: IUser[]) => this.userService.addUserToCollectionIfMissing<IUser>(users, this.seatReserved?.user)))
      .subscribe((users: IUser[]) => (this.usersSharedCollection = users));
  }
}
