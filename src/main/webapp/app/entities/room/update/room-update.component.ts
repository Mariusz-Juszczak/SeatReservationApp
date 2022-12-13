import { Component, OnInit, ElementRef } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { RoomFormService, RoomFormGroup } from './room-form.service';
import { IRoom } from '../room.model';
import { RoomService } from '../service/room.service';
import { AlertError } from 'app/shared/alert/alert-error.model';
import { EventManager, EventWithContent } from 'app/core/util/event-manager.service';
import { DataUtils, FileLoadError } from 'app/core/util/data-util.service';
import { IFloor } from 'app/entities/floor/floor.model';
import { FloorService } from 'app/entities/floor/service/floor.service';
import { AvailabilityStatus } from 'app/entities/enumerations/availability-status.model';
import { RoomType } from 'app/entities/enumerations/room-type.model';

@Component({
  selector: 'app-room-update',
  templateUrl: './room-update.component.html',
})
export class RoomUpdateComponent implements OnInit {
  isSaving = false;
  room: IRoom | null = null;
  availabilityStatusValues = Object.keys(AvailabilityStatus);
  roomTypeValues = Object.keys(RoomType);

  floorsSharedCollection: IFloor[] = [];

  editForm: RoomFormGroup = this.roomFormService.createRoomFormGroup();

  constructor(
    protected dataUtils: DataUtils,
    protected eventManager: EventManager,
    protected roomService: RoomService,
    protected roomFormService: RoomFormService,
    protected floorService: FloorService,
    protected elementRef: ElementRef,
    protected activatedRoute: ActivatedRoute
  ) {}

  compareFloor = (o1: IFloor | null, o2: IFloor | null): boolean => this.floorService.compareFloor(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ room }) => {
      this.room = room;
      if (room) {
        this.updateForm(room);
      }

      this.loadRelationshipsOptions();
    });
  }

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(base64String: string, contentType: string | null | undefined): void {
    this.dataUtils.openFile(base64String, contentType);
  }

  setFileData(event: Event, field: string, isImage: boolean): void {
    this.dataUtils.loadFileToForm(event, this.editForm, field, isImage).subscribe({
      error: (err: FileLoadError) =>
        this.eventManager.broadcast(new EventWithContent<AlertError>('seatReservationApp.error', { ...err, key: 'error.file.' + err.key })),
    });
  }

  clearInputImage(field: string, fieldContentType: string, idInput: string): void {
    this.editForm.patchValue({
      [field]: null,
      [fieldContentType]: null,
    });
    if (idInput && this.elementRef.nativeElement.querySelector('#' + idInput)) {
      this.elementRef.nativeElement.querySelector('#' + idInput).value = null;
    }
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const room = this.roomFormService.getRoom(this.editForm);
    if (room.id !== null) {
      this.subscribeToSaveResponse(this.roomService.update(room));
    } else {
      this.subscribeToSaveResponse(this.roomService.create(room));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IRoom>>): void {
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

  protected updateForm(room: IRoom): void {
    this.room = room;
    this.roomFormService.resetForm(this.editForm, room);

    this.floorsSharedCollection = this.floorService.addFloorToCollectionIfMissing<IFloor>(this.floorsSharedCollection, room.floor);
  }

  protected loadRelationshipsOptions(): void {
    this.floorService
      .query()
      .pipe(map((res: HttpResponse<IFloor[]>) => res.body ?? []))
      .pipe(map((floors: IFloor[]) => this.floorService.addFloorToCollectionIfMissing<IFloor>(floors, this.room?.floor)))
      .subscribe((floors: IFloor[]) => (this.floorsSharedCollection = floors));
  }
}
