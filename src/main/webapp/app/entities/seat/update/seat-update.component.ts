import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { SeatFormService, SeatFormGroup } from './seat-form.service';
import { ISeat } from '../seat.model';
import { SeatService } from '../service/seat.service';
import { IRoom } from 'app/entities/room/room.model';
import { RoomService } from 'app/entities/room/service/room.service';
import { SeatStatus } from "../../enumerations/seat-status.model";

@Component({
  selector: 'app-seat-update',
  templateUrl: './seat-update.component.html',
})
export class SeatUpdateComponent implements OnInit {
  isSaving = false;
  seat: ISeat | null = null;
  availabilityStatusValues = Object.keys(SeatStatus);

  roomsSharedCollection: IRoom[] = [];

  editForm: SeatFormGroup = this.seatFormService.createSeatFormGroup();

  constructor(
    protected seatService: SeatService,
    protected seatFormService: SeatFormService,
    protected roomService: RoomService,
    protected activatedRoute: ActivatedRoute
  ) {}

  compareRoom = (o1: IRoom | null, o2: IRoom | null): boolean => this.roomService.compareRoom(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ seat }) => {
      this.seat = seat;
      if (seat) {
        this.updateForm(seat);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const seat = this.seatFormService.getSeat(this.editForm);
    if (seat.id !== null) {
      this.subscribeToSaveResponse(this.seatService.update(seat));
    } else {
      this.subscribeToSaveResponse(this.seatService.create(seat));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ISeat>>): void {
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

  protected updateForm(seat: ISeat): void {
    this.seat = seat;
    this.seatFormService.resetForm(this.editForm, seat);
    this.roomsSharedCollection = this.roomService.addRoomToCollectionIfMissing<IRoom>(this.roomsSharedCollection, seat.room);
  }

  protected loadRelationshipsOptions(): void {
    this.roomService
      .query()
      .pipe(map((res: HttpResponse<IRoom[]>) => res.body ?? []))
      .pipe(map((rooms: IRoom[]) => this.roomService.addRoomToCollectionIfMissing<IRoom>(rooms, this.seat?.room)))
      .subscribe((rooms: IRoom[]) => (this.roomsSharedCollection = rooms));
  }
}
