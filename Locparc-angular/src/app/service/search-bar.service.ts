import { Injectable } from '@angular/core';
import { Item } from '../model/Item';

@Injectable({
  providedIn: 'root'
})
export class SearchBarService {

  constructor() { }

  private selectedItem: Item | undefined;

  getSelectedItem() {
    if (this.selectedItem)
      return this.selectedItem;
    return null;
  }

  setSelectedItem(item: Item) {
    this.selectedItem = item;
  }
  
}
