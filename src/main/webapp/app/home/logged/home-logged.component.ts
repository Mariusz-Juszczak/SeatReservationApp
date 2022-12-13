import {Component, Input, OnDestroy, OnInit} from '@angular/core';
import {Observable, Subject, tap} from 'rxjs';
import {takeUntil} from 'rxjs/operators';

import {AccountService} from 'app/core/auth/account.service';
import {Account} from 'app/core/auth/account.model';
import {ISeatReserved} from "../../entities/seat-reserved/seat-reserved.model";
import {EntityArrayResponseType, SeatReservedService} from "../../entities/seat-reserved/service/seat-reserved.service";
import {INotificationAlert} from "../../entities/notification-alert/notification-alert.model";
import {NotificationAlertService} from "../../entities/notification-alert/service/notification-alert.service";
import {EntityResponseType, RestrictionsService} from "../../entities/restrictions/service/restrictions.service";
import {IRestrictions} from "../../entities/restrictions/restrictions.model";

@Component({
  selector: 'app-home-logged',
  templateUrl: './home-logged.component.html',
  styleUrls: ['./home-logged.component.scss'],
})
export class HomeLoggedComponent implements OnInit, OnDestroy {
  account: Account | null = null;
  isLoading = false;
  isNotificationsLoading = false;
  restriction?: IRestrictions | null;
  notifications?: INotificationAlert[];
  upcomingReservedSeats?: ISeatReserved[];
  recentReservedSeats?: ISeatReserved[];
  totalItems = 0;
  ascending = true;
  firstName = '';

  reservedSeatsQueryObject = {
    page: 0,
    size: 3,
    sort: ['fromDate,asc'],
  };

  notificationsQueryObject = {  // TODO waiting for the endpoint
    page: 0,
    size: 5,
    sort: ['createdAt,desc'],
  };


  private readonly destroy$ = new Subject<void>();

  constructor(private accountService: AccountService,
              private seatReservedService: SeatReservedService,
              private restrictionsService: RestrictionsService,
              private notificationAlertService: NotificationAlertService
  ) {}

  @Input()
  set firstNameAttribute(firstName: string) {
    this.firstName = firstName;
  }

  trackId = (_index: number, item: INotificationAlert): number => this.notificationAlertService.getNotificationAlertIdentifier(item);

  ngOnInit(): void {
    this.accountService
      .getAuthenticationState()
      .pipe(takeUntil(this.destroy$))
      .subscribe(account => (this.account = account));
    this.load();
  }

  load(): void {
    this.loadUpcomingReservedSeats().subscribe({
      next: (res: EntityArrayResponseType) => {
        this.upcomingReservedSeats = res.body ?? [];
      },
    });

    this.loadRecentReservedSeats().subscribe({
      next: (res: EntityArrayResponseType) => {
        this.recentReservedSeats = res.body ?? [];
      },
    });

    this.loadNotifications();

    this.loadRestriction().subscribe({
      next: (res: EntityResponseType) => {
        this.restriction = res.body;
      },
    });
  }

  ngOnDestroy(): void {
    this.destroy$.next();
    this.destroy$.complete();
  }

  loadNotifications(): void {
    this.isNotificationsLoading = true;
    this.notificationAlertService.findNewNotifications(this.notificationsQueryObject)
      .pipe(tap(() => (this.isNotificationsLoading = false))).subscribe({
        next: (res: EntityArrayResponseType) => {
          this.notifications = res.body ?? [];
        },
      });
  }

  close(notification: INotificationAlert): void {
      this.notificationAlertService.markAsRead(notification.id).subscribe(() => {
          this.loadNotifications();
      });
  }


  protected loadUpcomingReservedSeats(): Observable<EntityArrayResponseType> {
    return this.seatReservedService.findUpcoming(this.reservedSeatsQueryObject)
      .pipe(tap(() => (this.isLoading = false)));
  }

  protected loadRecentReservedSeats(): Observable<EntityArrayResponseType> {
    return this.seatReservedService.findRecent(this.reservedSeatsQueryObject)
      .pipe(tap(() => (this.isLoading = false)));
  }

  protected loadRestriction(): Observable<EntityResponseType> {
    return this.restrictionsService.findCurrent()
      .pipe(tap(() => (this.isLoading = false)));
  }

}
