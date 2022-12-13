import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IEquipment } from '../equipment.model';

@Component({
  selector: 'app-equipment-detail',
  templateUrl: './equipment-detail.component.html',
})
export class EquipmentDetailComponent implements OnInit {
  equipment: IEquipment | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ equipment }) => {
      this.equipment = equipment;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
