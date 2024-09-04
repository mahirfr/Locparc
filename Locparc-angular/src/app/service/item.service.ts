import { Injectable  } from '@angular/core'                ;
import { Item        } from '../model/Item'                ;
import { HttpClient  } from '@angular/common/http'         ;
import { Observable  } from 'rxjs'                         ;
import { environment } from 'src/environments/environment' ;

@Injectable({
	providedIn: 'root'
})
export class ItemService {
	URL = environment.serverUrl + "/api/items";

	constructor(private http: HttpClient) { }


	isDeviceElectronic(item: Item) {
		return item.subCategory?.name === "INFORMATIQUE" || item.subCategory?.name === "ELECTROMENAGER";
	}

	getAllItems(): Observable<Item[]> {
		return this.http.get<Item[]>(this.URL + "/admin/");
	}


	getAvailableItemsByName(itemName: string): Observable<Item[]> {
		return this.http.get<Item[]>(`${this.URL}/available/search/${itemName}`)
	}

	getAvailableItemsBySubCategories(subCategories: string[]): Observable<Item[]> {
		return this.http.get<Item[]>(`${this.URL}/available/sub-category/${subCategories}`);
	}

	getAvailableItemsByNameAndSubcategories(itemName: string, subCategories: string[]): Observable<Item[]> {
		return this.http.get<Item[]>(`${this.URL}/available/${itemName}/${subCategories}`);
	}


	// TODO: Add a post method to add an item
	create(item: Item): Observable<Item> {
		return this.http.post<Item>(this.URL + "/admin/", item);
	}

	// TODO: Add a post method to add multiple items
	createMultiple(items: Item[]): Observable<Item[]> {
		return this.http.post<Item[]>(this.URL + "/admin/multiple", items);
	}


	// TODO: Add a put method to update an item
	update(item: Item): Observable<Item> {
		return this.http.put<Item>(this.URL + "/admin/", item);
	}

	// TODO: Add a delete method to delete an item
	delete(id: number): Observable<Item> {
		return this.http.delete<Item>(this.URL + "/admin/" + id);
	}


}
