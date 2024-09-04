import { Component } from '@angular/core';
import { Order } from 'src/app/model/Order';
import { OrderService } from 'src/app/service/order.service';
import { MatDialog } from '@angular/material/dialog';
import { DialogConfirmationComponent } from '../dialogs/dialog-confirmation/dialog-confirmation.component';
import { OrderDialogComponent } from '../dialogs/order-dialog/order-dialog.component';

@Component({
  selector: 'app-my-orders',
  templateUrl: './my-orders.component.html',
  styleUrls: ['./my-orders.component.css']
})
export class MyOrdersComponent {

  myPendingOrders: Order[] = [];
  returnCode = 0;
  ordered: Order | undefined; 

  constructor(private orderService: OrderService,
              private dialog      : MatDialog) {}
              
  ngOnInit() {
    this.myPendingOrders = [];
    this.orderService.getMyPendingOrders().subscribe({
      next: (orders) => {
        this.myPendingOrders = orders;
      },
      error: (error) => {
        if (error.status === 404) 
          this.returnCode = 404;
        else 
          this.returnCode = 500;
      }
    });
  }

  getOrderById(id?: number): void {
    this.ordered = this.myPendingOrders.find((order) => order.id === id);
  }

  openOrderDialog(order : Order) {
    const dialogRef = this.dialog.open(OrderDialogComponent, {
      width: '400px',
      data: { 
              title: "Consulter",
              content: "Vous consultez la commande n°: " + order.id,
              order: order,
              action: "cancel"
            },
    });

    dialogRef.afterClosed().subscribe((result: string) => {
      if (result === "cancel") {
        this.openRemovalDialog(order)
      }
    });
  }

  openRemovalDialog(order: Order) {
    const dialogRef = this.dialog.open(DialogConfirmationComponent, {
      width: '400px',
      data: { 
              title: "Annuler",
              content: "Êtes-vous sûr de vouloir anuller votre commande ?",
              order: order,
              action: "cancel"
            },
    });

    dialogRef.afterClosed().subscribe(result => {
      if (result === "cancel") {
        console.log(order)  
        this.deleteOrder(order);
        alert("Commande annulé");
      }
    });
  }

  deleteOrder(order: Order) {
    if (order.id) {
      this.orderService.deletePendingOrder(order.id).subscribe();
      this.myPendingOrders = this.myPendingOrders.filter((o) => order.id != o.id);
      console.log("delete Order" + order.id)
    }
    console.log(this.myPendingOrders);
  }

}
