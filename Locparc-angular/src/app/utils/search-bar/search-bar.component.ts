import { Component           , OnInit                 } from '@angular/core'                                 ;
import { ItemService                                  } from 'src/app/service/item.service'                  ;
import { FormBuilder         , FormControl, FormGroup } from '@angular/forms'                                ;
import { CategoriesService                            } from 'src/app/service/categories.service'            ;
import { Category                                     } from 'src/app/model/Category/Category'               ;
import { catchError         , map                     } from 'rxjs/operators'                                ;
import { throwError                                   } from 'rxjs/internal/observable/throwError'           ;
import { Item                                         } from 'src/app/model/Item'                            ;
import { ShoppingCartService                          } from 'src/app/service/shopping-cart-service' 		 ;

@Component({
	selector: 'app-search-bar',
	templateUrl: './search-bar.component.html',
	styleUrls: ['./search-bar.component.css']
})
export class SearchBarComponent implements OnInit {
	searchQuery = "";
	categories: Category[] = [];
	searchResults: any[] = [];
	items: any[] = [];
	selectedSubCategories: string[] = [];
	searchForm: FormGroup;
	hasError = false;

	selectedItem: Item | undefined;
	selectedQuantity: number | undefined;
	
	constructor(
		private itemService: ItemService,
		private categoryService: CategoriesService,
		private shoppingCartService: ShoppingCartService,
		private formBuilder: FormBuilder) {
		this.searchForm = this.formBuilder.group({
			selectedSubCategories: [] // Initialize with an empty array
		});
	}

	ngOnInit() {

		this.categoryService.getAllCategories().subscribe(
			categories => this.categories = categories
		);

	}

	onSelectedCategoryValuesChange() {
		this.selectedSubCategories = this.searchForm.get('selectedSubCategories')?.value;

		if (this.searchQuery.length === 0 && this.selectedSubCategories.length >= 1) {
			// get all items of that category 
			this.itemService.getAvailableItemsBySubCategories(this.selectedSubCategories)
			.pipe(
				catchError(error => {
					return this.handleError(error);
				})
			)
			.subscribe(
				items => {
					this.searchResults = items;
				}
			);

		} else if (this.searchQuery.length >= 1 && this.selectedSubCategories.length >= 1) {
			// get all items of that category and of that name 
			this.itemService.getAvailableItemsByNameAndSubcategories(this.searchQuery.toLowerCase(), this.selectedSubCategories)
			.pipe(
				catchError(error => {
					return this.handleError(error);
				})
			)
			.subscribe(
				items => {
					this.searchResults = items;
				}
			);
		} else if (this.searchQuery.length >= 1 && this.selectedSubCategories.length === 0) {
			// get all items of that name
			this.itemService.getAvailableItemsByName(this.searchQuery)
			.pipe(
				catchError(error => {
					return this.handleError(error);
				})
			)
			.subscribe(
				items => {
					this.searchResults = items;
				}
			);
		} else {
			this.searchResults = [];
		}		

		this.hasError = false;
	}

	search() {
		// If input not empty get items
		if (this.searchQuery.length >= 1 && this.selectedSubCategories.length === 0) {
			this.itemService.getAvailableItemsByName(this.searchQuery.toLowerCase())
			.pipe(
				catchError(error => {
					return this.handleError(error);
				}),
			)
			.subscribe(
				items => {
					this.searchResults = items;
				}
			)
		} else if (this.searchQuery.length >= 1 && this.selectedSubCategories.length >= 1) {
			this.itemService.getAvailableItemsByNameAndSubcategories(this.searchQuery.toLowerCase(), this.selectedSubCategories)
			.pipe(
				catchError(error => {
					return this.handleError(error);
				})
			)
			.subscribe(
				items => {
					this.searchResults = items;
				}
			);
			// this.hasError = false;
		} else if (this.searchQuery.length === 0 && this.selectedSubCategories.length >= 1) {
			this.itemService.getAvailableItemsBySubCategories(this.selectedSubCategories)
			.pipe(
				catchError(error => {
					return this.handleError(error);
				})
			)
			.subscribe(
				items => {
					this.searchResults = items;
				}
			);
		} else {
			this.searchResults = [];
		}

		this.hasError = false;
	}

	

	handleError(error: any) {
		this.searchResults = [];
		this.hasError = true;
		if (error.status == 404) {
			return throwError(() => new Error("La ressource n'a pas été trouvé"));
		}
		return throwError(() => new Error('Une erreur est survenue, ressayez plus tard'));
	}

	
	sendItemToModal(selectedItem: Item) {
		this.selectedItem = selectedItem;
	}

	getRange(quantity: number): number[] {
		return Array.from({ length: quantity }, (_, index) => index + 1);
	}
	
	setSelectedItemQuantity(item: Item, quantity: number) {
		if (item.quantity >= quantity) {
			item.quantity = quantity;
			return;
		}
		alert("Vous avez dépasé la quantité disponible! ");
	}

	addToCart(selectedItem: Item, selectedQuantity: number) {
		// Cast item.quantity & selectedQuantity to numbers  
		selectedItem.quantity = +selectedItem.quantity;
		selectedQuantity 	  = +selectedQuantity;	

		if (selectedItem.quantity < selectedQuantity) {
			alert("Vous avez dépassé la quantité disponible");
			return;
		}

		let itemInCart = this.shoppingCartService.isInCart(selectedItem);
		console.log(itemInCart);
		if (itemInCart) {
			if ((itemInCart.quantity + selectedQuantity) > selectedItem.quantity) {
				alert("Vous avez dépassé la quantité disponible");
			} else {
				this.shoppingCartService.addToCart(selectedItem, selectedQuantity);
				this.alertByQuantity(selectedItem.name, selectedQuantity);
			}

		} else {
			this.shoppingCartService.addToCart(selectedItem, selectedQuantity);
			this.alertByQuantity(selectedItem.name, selectedQuantity);
		}		
	}

	alertByQuantity(name: string, quantity: number) {
		if (quantity > 1)
			alert(quantity + " exemplaires de " + name + " viennent d'être ajoutés au panier");
		else 
			alert("Un exemplaire de " + name + " vient d'être ajouté au panier");
	}
}
