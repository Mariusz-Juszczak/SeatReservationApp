import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { EquipmentFormService, EquipmentFormGroup } from './equipment-form.service';
import { IEquipment } from '../equipment.model';
import { EquipmentService } from '../service/equipment.service';
import { ISeat } from 'app/entities/seat/seat.model';
import { SeatService } from 'app/entities/seat/service/seat.service';
import { EquipmentType } from 'app/entities/enumerations/equipment-type.model';

@Component({
  selector: 'app-equipment-update',
  templateUrl: './equipment-update.component.html',
})
export class EquipmentUpdateComponent implements OnInit {
  isSaving = false;
  equipment: IEquipment | null = null;
  equipmentTypeValues = Object.keys(EquipmentType);

  seatsSharedCollection: ISeat[] = [];

  editForm: EquipmentFormGroup = this.equipmentFormService.createEquipmentFormGroup();

  constructor(
    protected equipmentService: EquipmentService,
    protected equipmentFormService: EquipmentFormService,
    protected seatService: SeatService,
    protected activatedRoute: ActivatedRoute
  ) {}

  compareSeat = (o1: ISeat | null, o2: ISeat | null): boolean => this.seatService.compareSeat(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ equipment }) => {
      this.equipment = equipment;
      if (equipment) {
        this.updateForm(equipment);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const equipment = this.equipmentFormService.getEquipment(this.editForm);
    if (equipment.id !== null) {
      this.subscribeToSaveResponse(this.equipmentService.update(equipment));
    } else {
      this.subscribeToSaveResponse(this.equipmentService.create(equipment));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IEquipment>>): void {
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

  protected updateForm(equipment: IEquipment): void {
    this.equipment = equipment;
    this.equipmentFormService.resetForm(this.editForm, equipment);

    this.seatsSharedCollection = this.seatService.addSeatToCollectionIfMissing<ISeat>(this.seatsSharedCollection, equipment.seat);
  }

  protected loadRelationshipsOptions(): void {
    this.seatService
      .query()
      .pipe(map((res: HttpResponse<ISeat[]>) => res.body ?? []))
      .pipe(map((seats: ISeat[]) => this.seatService.addSeatToCollectionIfMissing<ISeat>(seats, this.equipment?.seat)))
      .subscribe((seats: ISeat[]) => (this.seatsSharedCollection = seats));
  }
}
