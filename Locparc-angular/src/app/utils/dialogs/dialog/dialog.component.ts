import { Component, Inject } from '@angular/core'            ;
import { MAT_DIALOG_DATA, MatDialog, MatDialogRef } from '@angular/material/dialog' ; 
import { Address } from 'src/app/model/Address';
import { Country } from 'src/app/model/Country';
import { Order } from 'src/app/model/Order';
import { CountryService } from 'src/app/service/country.service';
import { ShoppingCartService } from 'src/app/service/shopping-cart-service';

@Component({
  selector: 'app-dialog',
  templateUrl: './dialog.component.html',
  styleUrls: ['./dialog.component.css']
})
export class DialogComponent {

  selectedQuantity: number | undefined;
  streetNumber: string | undefined;
  street: string | undefined;
  addressDetails: string | undefined;
  city: string | undefined;
  postalCode: string | undefined;
  selectedCountry: Country | undefined;
  countries: Country[] = [];
  motive: string = "";

  // TODO: finish the Order creation 
  
  constructor(
    private shoppingCartService: ShoppingCartService,
    public dialogRef: MatDialogRef<DialogComponent>,
    private countryService: CountryService,
    @Inject(MAT_DIALOG_DATA) public data: any
  ) {
      this.countryService.getAll().subscribe((countries) => {
        this.countries = countries;
      });
   }

  onCloseClick(): void {
    this.dialogRef.close();
  }

  onConfirm(action: string) {
    if (action === "modify") {
      this.dialogRef.close(this.selectedQuantity);
    } else if (action === "completion") {
      this.dialogRef.close({
        action: "completion",
        streetNumber: this.streetNumber,
        street: this.street,
        addressDetails: this.addressDetails,
        city: this.city,
        postalCode: this.postalCode,
        selectedCountry: this.selectedCountry,
        motive: this.motive
      })
    } else {
      this.dialogRef.close(action);
    }
  }

  buildAddressAndMotive(): Address {
    let address = new Address();
    address.streetNumber = this.streetNumber;
    address.street = this.street;
    address.addressDetails = this.addressDetails;
    address.city = address.city;
    address.country = this.selectedCountry;
    return address;
  }

  getRange(quantity: number) {
    return this.shoppingCartService.getRange(quantity);
  }
  
}
