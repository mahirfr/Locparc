import { ChangeDetectorRef, Component   , OnInit } from '@angular/core'                ;
import { Order                } from 'src/app/model/Order'          ;
import { OrderService         } from 'src/app/service/order.service';
import { OrderItems           } from 'src/app/model/OrderItems'     ;
import { catchError, throwError } from 'rxjs';



@Component({
  selector: 'app-planning',
  templateUrl: './planning.component.html',
  styleUrls: ['./planning.component.css']
})
export class PlanningComponent implements OnInit {

  todaysDate = new Date();
  orders        : Order[] = []       ;
  orderedItems  : OrderItems[] = []  ;
  ordered       : Order = new Order();
  selectedOrders: any[] = []         ;
  acceptedOrder : Order | undefined  ;
  refusedOrder  : Order | undefined  ;
  returnCode     = 0                 ;
  refusalMotive : string = "Matériel momentanément indisponible";

  
  constructor(private orderService: OrderService,
              private cdr: ChangeDetectorRef) { }

  ngOnInit() {
    this.orders = [];
    this.orderService.getPendingOrders()
    .subscribe({
      next: (orders: Order[]) => {
        this.orders = orders;
      },
      error: requestError => {
        if (requestError.status === 404) {
          this.returnCode = 404;
        } else {
          this.returnCode = 500;
        }
      }
    });
  }

  getOrderById(id?: number): void {
    this.orderService.getOrderById(id).subscribe((order: Order) => {
        this.ordered = order;
      }
    )
  }

  // TODO: accept order
  acceptOrder(ordered: Order) {
    this.orderService.approveOrder(ordered).subscribe((order) => {
      this.acceptedOrder = order;
      this.orderService.removeOrder(this.orders, ordered);
      alert("La commande a été validée !");
      
    })
  }

  // TODO: refuse order
  refuseOrder(order: Order, refusalMotive: string) {
    this.orderService.refuseOrder(order, refusalMotive).subscribe((o) => {
      this.refusedOrder = o;
      this.orderService.removeOrder(this.orders, order);
      alert("La commande a été refusée !");
    })
  }
}
