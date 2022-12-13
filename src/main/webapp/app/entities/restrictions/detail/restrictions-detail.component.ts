import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IRestrictions } from '../restrictions.model';

@Component({
  selector: 'app-restrictions-detail',
  templateUrl: './restrictions-detail.component.html',
})
export class RestrictionsDetailComponent implements OnInit {
  restrictions: IRestrictions | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ restrictions }) => {
      this.restrictions = restrictions;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
