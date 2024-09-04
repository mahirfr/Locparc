import { Component               , ViewChild } from '@angular/core'                                    ;
import { MatDialog                           } from '@angular/material/dialog'                         ;
import { Item                                } from 'src/app/model/Item'                               ;
import { ShoppingCartService                 } from 'src/app/service/shopping-cart-service'            ;
import { DialogComponent                     } from '../dialogs/dialog/dialog.component'                       ;
import { OrderService                        } from 'src/app/service/order.service'                    ;
import { LoginService                        } from 'src/app/service/login.service'                    ;
import { User                                } from 'src/app/model/User'                               ;
import { DateRangePickerComponent            } from '../date-range-picker/date-range-picker.component' ;
import { Address } from 'src/app/model/Address';
import { Order } from 'src/app/model/Order';
import { Observable } from 'rxjs';
import { DatePipe } from '@angular/common';


@Component({
  selector: 'app-shopping-cart',
  templateUrl: './shopping-cart.component.html',
  styleUrls: ['./shopping-cart.component.css'],
})
export class ShoppingCartComponent {

  // TODO: get selectedItem
  cartItems        : Item[] | null = [];
  selectedQuantity: number | undefined ;
  connectedUser    : User | null = null;
  isAdmin          = false             ;
  startDate        : Date | undefined  ;
  endDate          : Date | undefined  ;
  order            = new Order()       ;
  timeZone         = 'Europe/Paris'    ;
  
  @ViewChild(DateRangePickerComponent) dateRangePicker: DateRangePickerComponent | undefined;

  constructor(private shoppingCartService: ShoppingCartService,
              private oderService        : OrderService,
              private loginService       : LoginService,
              private dialog             : MatDialog,
              private datePipe           : DatePipe) {
    this.cartItems = this.shoppingCartService.getCartItems();
    this.loginService._connectedUser.subscribe(
      (user) => {
        this.connectedUser = user,
        this.isAdmin = user?.role?.name == "ROLE_ADMIN"
      }
    )
  }

  ngAfterViewInit() {
    if (this.dateRangePicker) {
      this.dateRangePicker.dateRange.valueChanges.subscribe((value) => {
        this.startDate = value.start;
        this.endDate = value.end;
      });
    }
  }
  
  openModifyDialog(item: Item) {
    const dialogRef = this.dialog.open(DialogComponent, {
      width: '400px', 
      data: { 
              title: "Modifier",
              content: "Modifiez la quantité de l'élément",
              item: item,
              action: "modify"
            },
    });

    dialogRef.afterClosed().subscribe(result => {
      if (result !== undefined) {
        const newQuantity: number = result as number;
        if (newQuantity >= 1 && newQuantity <= item.quantity) {
          item.quantity = newQuantity;
          this.updateItem(item);
          alert("Quantité modifié !");
        } else {
          alert("Quantité dépasée !");
        }
      }
    });
  }

  openRemovalDialog(item: Item) {
    const dialogRef = this.dialog.open(DialogComponent, {
      width: '400px',
      data: { 
              title: "Supprimer",
              content: "Êtes-vous sûr de vouloir retirer " + item.name +" du panier ?",
              item: item,
              action: "remove"
            },
    });

    dialogRef.afterClosed().subscribe(result => {
      if (result === "remove") {
        this.deleteItem(item);
      }
    });
  }

  openOrderCompletionDialog() {
    // persist an empty order first and only once
    if (Object.keys(this.order).length === 0) 
      this.getOrderId(this.order);
    
    const dialogRef = this.dialog.open(DialogComponent, {
      width: '400px',
      data: { 
              title: "Complétez la commande",
              content: "Fournir des détails supplémentaires sur votre commande (optionnel)",
              action: "completion"
            },
    });

    dialogRef.afterClosed().subscribe(result => {

      if (this.order.id != undefined && this.order.id != null) {
        this.order.startDate = this.datePipe.transform(this.startDate, 'yyyy-MM-dd', this.timeZone) as string;
        this.order.endDate = this.datePipe.transform(this.endDate, 'yyyy-MM-dd', this.timeZone) as string;
        
        if (result) {
          let address = new Address();
          address.streetNumber = result.streetNumber;
          address.street = result.street;
          address.addressDetails = result.addressDetails;
          address.city = result.city;
          address.postalCode = result.postalCode;
          address.country = result.selectedCountry;
          this.order = this.oderService.buildOrder(this.order,
                                                    this.cartItems!, 
                                                    this.connectedUser!, 
                                                    address,  
                                                    result.motive);
          this.sendOrder(this.order).subscribe((order) => {
            this.order = order;
            this.clearCart();
            alert("Commande envoyé avec succès!")
          });
        }
      }
    });
  }

  

  openCartClearingConfirmationDialog() {
    const dialogRef = this.dialog.open(DialogComponent, {
      width: '400px',
      data: { 
              title: "Confirmation",
              content: "Etes vous sûr de vouloir vider le panier",
              action: "empty"
            },
    });

    dialogRef.afterClosed().subscribe(result => {
      if (result === "empty") {
        this.clearCart();
        this.cartItems = null;
      }
    });
  }

  updateItem(item: Item) {
    this.shoppingCartService.updateCartItem(item);
  }

  deleteItem(item: Item) {
    this.shoppingCartService.removeFromCart(item);
  }

  clearCart() {
    this.shoppingCartService.clearCart();
    this.cartItems = [];
  }

  isValidDateRange(startDate: Date, endDate: Date) {
    return this.dateRangePicker?.isValidDateRange(startDate, endDate);
  }

  sendOrder(order: Order): Observable<Order> {
    return this.oderService.sendOrder(order);
  }

  getOrderId(order: Order) {
    this.oderService.persistOrder(order).subscribe((order) => {
      return this.order!.id = order.id
    })
  }
}
