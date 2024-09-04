import { Component, ViewChild } from '@angular/core';
import { MatTabChangeEvent } from '@angular/material/tabs';
import { Router } from '@angular/router';
import { User } from 'src/app/model/User';
import { LoginService } from 'src/app/service/login.service';
import { MyOrdersComponent } from 'src/app/utils/my-orders/my-orders.component';
import { OrderHistoryComponent } from 'src/app/utils/order-history/order-history.component';
import { PlanningComponent } from 'src/app/utils/planning/planning.component';
import { SearchOrdersComponent } from 'src/app/utils/search-orders/search-orders.component';

@Component({
  selector: 'app-orders',
  templateUrl: './orders.component.html',
  styleUrls: ['./orders.component.css']
})
export class OrdersComponent {

  @ViewChild(PlanningComponent    , { static: false }) private planningComponent    !: PlanningComponent    ;
  @ViewChild(SearchOrdersComponent, { static: false }) private searchOrderComponent !: SearchOrdersComponent;
  @ViewChild(MyOrdersComponent    , { static: false }) private myOrdersComponent    !: MyOrdersComponent    ;
  @ViewChild(OrderHistoryComponent, { static: false }) private orderHistoryComponent!: OrderHistoryComponent;

  connectedUser: User | null = null;
	isAdmin: boolean = false;

  constructor(private loginService: LoginService) {
    this.loginService._connectedUser.subscribe(
			(user) => {
				this.connectedUser = user,
				this.isAdmin = user?.role?.name == "ROLE_ADMIN"
			}
		)
  }

  onTabChange(event: MatTabChangeEvent) {
    // Call refreshData() on each tab 
    if(event.index == 0) {
      this.planningComponent.ngOnInit();
    } else if (event.index == 1) {
      this.searchOrderComponent.onSubmitSearchFormGroup();
    } else if(event.index == 2) {
      this.myOrdersComponent.ngOnInit();
    } else if (event.index == 3) {
      this.orderHistoryComponent.ngOnInit();
    }
  }


  

}
