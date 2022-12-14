import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IBuilding } from '../building.model';

@Component({
  selector: 'app-building-detail',
  templateUrl: './building-detail.component.html',
})
export class BuildingDetailComponent implements OnInit {
  building: IBuilding | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ building }) => {
      this.building = building;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
