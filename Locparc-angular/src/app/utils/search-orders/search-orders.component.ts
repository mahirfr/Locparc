import { Component } from '@angular/core';
import { FormBuilder, FormGroup } from '@angular/forms';
import { Order } from 'src/app/model/Order';
import { OrderService } from 'src/app/service/order.service';
import { DialogConfirmationComponent } from '../dialogs/dialog-confirmation/dialog-confirmation.component';
import { MatDialog } from '@angular/material/dialog';
import { OrderDialogComponent } from '../dialogs/order-dialog/order-dialog.component';
import { OrderItems } from 'src/app/model/OrderItems';
import { MatDatepickerModule } from '@angular/material/datepicker';
import { MatInputModule } from '@angular/material/input';
import { MatNativeDateModule } from '@angular/material/core';
import { DatePipe } from '@angular/common';

@Component({
  selector: 'app-search-orders',
  templateUrl: './search-orders.component.html',
  styleUrls: ['./search-orders.component.css']
})
export class SearchOrdersComponent {

  searchQuery = "";
  userOrders: Order[] = [];
  order: Order | undefined;
  searchFormGroup: FormGroup;
  errorCode = 0;
  selectedDate: any;
  timeZone = 'Europe/Paris';
  approvedReturnOrder: Order | undefined;

  constructor(private orderService: OrderService,
              private formBuilder : FormBuilder,
              private dialog      : MatDialog,
              private datePipe    : DatePipe) 
  {
    this.searchFormGroup = this.formBuilder.group({
      searchQuery: [""]
    });
  }

  onSubmitSearchFormGroup() {
    this.searchQuery = this.searchFormGroup.get('searchQuery')?.value;
    this.userOrders = [];
    this.errorCode = 0;
    if (this.searchQuery != "") {
      this.orderService.getUserHistory(this.searchQuery).subscribe(
      {
        next: (orders) => {
          this.userOrders = orders;
        },
        error: (error) => {
          if (error.status === 404)
            this.errorCode = 404
          else 
            this.errorCode = 500
        }     
      })
    }
    
  }

  getOrderById(id: number) {
    this.order = this.userOrders.find((order) => order.id === id);
    
  }

  openOrderDialog(order: Order) {
    if ((order.request?.approved && order.request!.responseDate != null) &&
        !this.orderReturnIsApproved(order.orderedItems!)) {

      const dialogRef = this.dialog.open(OrderDialogComponent, {
        width: '400px',
        data: { 
                title: "Clôturer",
                content: "Voulez vous clôturer la commande de l'utilisateur: " + order.user?.email,
                order: order,
                showDatePicker: true, // Add this property
                selectedDate: null,
                action: "approve-return"
              },
      });

      dialogRef.afterClosed().subscribe((result) => {
        if (result)
          if (result.action === "approve-return") {
            const startDate = this.datePipe.transform(order.startDate, 'yyyy-MM-dd', this.timeZone) as string;
            this.selectedDate = this.datePipe.transform(result.selectedDate, 'yyyy-MM-dd', this.timeZone) as string;
            if (this.selectedDate >= startDate) {
              this.approveOrderReturn(order, this.selectedDate);
            }
          }
      });
          
    } else {
      const dialogRef = this.dialog.open(OrderDialogComponent, {
      width: '400px',
      data: { 
              title: "Consulter",
              content: "Vous consultez la commande de l'utilisateur: " + order.user?.email,
              order: order,
              action: "remove"
            },
      });

      dialogRef.afterClosed().subscribe((result: string) => {
        if (result === "remove") {
          this.openRemovalDialog(order)
        }
      });
    }
  }

  openRemovalDialog(order: Order) {
    const dialogRef = this.dialog.open(DialogConfirmationComponent, {
      width: '400px',
      data: { 
              title: "Supprimer",
              content: "Êtes-vous sûr de vouloir supprimer la commande ?",
              order: order,
              action: "remove"
            },
    });

    dialogRef.afterClosed().subscribe((result: string) => {
      if (result === "remove") {
        this.deleteOrder(order);
        alert("Commande supprimé");
      }
    });
  }

  deleteOrder(order: Order) {
    if (order.id) {
      this.orderService.deleteOrder(order.id).subscribe();
      this.userOrders = this.userOrders.filter((o) => order.id != o.id);
    }
  }

  orderReturnIsApproved(orderedItems: OrderItems[]): boolean {
    return orderedItems.every((orderedItem) => {
      return orderedItem.approved && orderedItem.returnDate != null;
    });
  }

  approveOrderReturn(order: Order, returnDate: Date) {
    this.orderService.approveOrderReturn(order, returnDate).subscribe((order) => {
      this.approvedReturnOrder = order;
      this.onSubmitSearchFormGroup();
      alert("Commande clôturé");
    })
  }

}
