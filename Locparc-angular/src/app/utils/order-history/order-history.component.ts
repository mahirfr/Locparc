import { Component } from '@angular/core';
import { Order } from 'src/app/model/Order';
import { OrderService } from 'src/app/service/order.service';
import { DialogConfirmationComponent } from '../dialogs/dialog-confirmation/dialog-confirmation.component';
import { MatDialog } from '@angular/material/dialog';
import { OrderDialogComponent } from '../dialogs/order-dialog/order-dialog.component';

@Component({
  selector: 'app-order-history',
  templateUrl: './order-history.component.html',
  styleUrls: ['./order-history.component.css']
})
export class OrderHistoryComponent {

  myOrders            : Order[] = []     ;
  ordered             : Order | undefined;
  errorCode           = 0                ;

  constructor(private orderService: OrderService,
              private dialog      : MatDialog) {}

  ngOnInit() {
    this.myOrders = [];
    this.errorCode = 0;
    this.orderService.getMyOrderHistory().subscribe({
      next: (orders) => {
        this.myOrders = orders;
      },
      error: (error) => {
        if (error.status === 404) 
          this.errorCode = 404;
        else 
          this.errorCode = 500;
      }
    });
  }

  getOrderById(id?: number): void {
    this.ordered = this.myOrders.find((order) => order.id === id);
  }

  openOrderDialog(order: Order) {
    const dialogRef = this.dialog.open(OrderDialogComponent, {
      width: '400px',
      data: { 
              title: "Consulter",
              content: "Vous consultez la commande n° " + order.id,
              order: order,
              action: "consult"
            },
    });
  }

  openConfirmationDialog(order: Order) {
      const dialogRef = this.dialog.open(DialogConfirmationComponent, {
        width: '400px',
        data: { 
                title: "Annuler",
                content: "Voulez vous annuler la commande n° " + order.id,
                action: "cancel"
              },
      });
      dialogRef.afterClosed().subscribe((result: string) => {
        if (result === "cancel") {
          this.deleteOrder(order.id!).subscribe(() => {
            alert("Commandé annulé");
            this.ngOnInit();
            }
          )
        };
      });
  }

  // TODO: return order 
  checkOrderDatesForReturn(order: Order) {
    return this.orderService.checkOrderDatesForReturn(order);
  }

  checkOrderDatesForCancelation(order: Order) {
    return this.orderService.checkOrderDatesForCancelation(order);
  }
  
  deleteOrder(id: number) {
    return this.orderService.deleteApprovedOrder(id);
  }

  

}
