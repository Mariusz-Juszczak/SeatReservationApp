import {Component, OnDestroy, OnInit} from '@angular/core';
import {Subject} from 'rxjs';
import {takeUntil} from 'rxjs/operators';

import {AccountService} from 'app/core/auth/account.service';
import {Account} from 'app/core/auth/account.model';
import {ActivatedRoute, Router} from "@angular/router";

@Component({
  selector: 'app-home-not-logged',
  templateUrl: './home-not-logged.component.html',
  styleUrls: ['./home-not-logged.component.scss'],
})
export class HomeNotLoggedComponent implements OnInit, OnDestroy {

  account: Account | null = null;
  isLoading = false;
  totalItems = 0;
  ascending = true;

  private readonly destroy$ = new Subject<void>();

  constructor(private accountService: AccountService,
              private activatedRoute: ActivatedRoute,
              private router: Router
  ) {}

  ngOnInit(): void {
    this.accountService
      .getAuthenticationState()
      .pipe(takeUntil(this.destroy$))
      .subscribe(account => (this.account = account));
  }

  login(): void {
    this.router.navigate(['/']);
  }

  ngOnDestroy(): void {
    this.destroy$.next();
    this.destroy$.complete();
  }

}
