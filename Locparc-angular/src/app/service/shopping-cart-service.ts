import { Injectable } from '@angular/core';
import { Item } from '../model/Item';
import { ItemService } from './item.service';

@Injectable({
  providedIn: 'root'
})
export class ShoppingCartService {

  // TODO: Modify some stuff in here to accomodate quantity
  private cartKey = 'shopping_cart';

  constructor(private itemService: ItemService) { }

  private cartItems: Item[] = [];

  getCartItems(): Item[] | null {
    const storedItems = localStorage.getItem(this.cartKey);
    if (storedItems) {
      this.cartItems = JSON.parse(storedItems);
      return this.cartItems
    }
    return null;
  }

  addToCart(item: Item, quantity: number): void {
    const existingItem = this.isInCart(item);

    if (existingItem) {
      existingItem.quantity += quantity;
    } else {
      // Add the item to the cart
      this.cartItems.push({ ...item, quantity });
    }
    // Save the cart data to the local storage
    localStorage.setItem(this.cartKey, JSON.stringify(this.cartItems));
  }

  updateCartItem(modifiedItem: Item) {
    let cartItem = this.isInCart(modifiedItem);
    var index = -1;
    if (cartItem) {
      index = this.cartItems.indexOf(cartItem);
      if (index !== -1) {
          this.cartItems[index] = modifiedItem;
          localStorage.setItem(this.cartKey, JSON.stringify(this.cartItems));
      }
    }

  }

  removeFromCart(item: Item) {
    this.cartItems?.forEach((i, index) => {
      if (i.id === item.id) {
        this.cartItems?.splice(index, 1);
      }
    })
    localStorage.setItem(this.cartKey, JSON.stringify(this.cartItems));
  }

  clearCart(): void {
    // Clear the cart and remove cart data from local storage
    this.cartItems = [];
    localStorage.removeItem(this.cartKey);
  }

  isQuantityMaxedOut(item: Item, quantity: number) {
    return item.quantity < quantity;
  }

  isInCart(item: Item): Item | undefined {
    const storedItems = this.getCartItems();
    if (storedItems)
      return this.cartItems.find(itemToFind => itemToFind.id === item.id);
    return undefined;
  }

  getRange(quantity: number): number[] {
		return Array.from({ length: quantity }, (_, index) => index + 1);
	}

}