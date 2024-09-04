import { Component, ViewChild                                                             } from '@angular/core'                          ;
import { FormArray           , FormBuilder, FormControl, FormGroup, Validators } from '@angular/forms'                         ;
import { Observable                                                            } from 'rxjs'                                   ;
import { Category                                                              } from 'src/app/model/Category/Category'        ;
import { SubCategory                                                           } from 'src/app/model/Category/SubCategory'     ;
import { Item                                                                  } from 'src/app/model/Item'                     ;
import { Manufacturer                                                          } from 'src/app/model/Manufacturer/Manufacturer';
import { Model                                                                 } from 'src/app/model/Manufacturer/Model'       ;
import { CategoriesService                                                     } from 'src/app/service/categories.service'     ;
import { ItemService                                                           } from 'src/app/service/item.service'           ;
import { ManufacturersService                                                  } from 'src/app/service/manufacturers.service'  ;
import { ModelService                                                          } from 'src/app/service/model.service'          ;
import { SubCategoryService                                                    } from 'src/app/service/sub-category.service'   ;

@Component({
  selector: 'app-equipement',
  templateUrl: './equipement.component.html',
  styleUrls: ['./equipement.component.css']
})
export class EquipementComponent {

  categories          : Category    [] = [];
  manufacturers       : Manufacturer[] = [];
  formGroup           : FormGroup;
  itemName            : string = "";
  selectedSubCategory : string = "";
  selectedModel       : string = "";
  itemDescription     : string | null = null;
  quantity            : number = 1 ;
  // warantyDate         : string = "";
  // arrivalDate         : string = "";
  existing            : boolean = true;
  onMaintenance       : boolean = false;
  imageUrl              : string | null = null;

  // form group for category
  formGroupCategory   : FormGroup;
  categoryName        : string = "";

  // form group for manufacturer
  formGroupManufacturer      : FormGroup;
  manufacturerName           : string = "";

  // form group for adding a sub category
  formGroupSubCategory       : FormGroup;
  subCategoryName            : string = "";
  categoryId                 : number = 0;

  // form group for adding a model
  formGroupModel             : FormGroup;
  modelName                  : string = "";
  manufacturerId             : number = 0;

  // form group for adding a serial number
  formGroupSerialNumber : FormGroup;
  serialNumberArray     : (string | null)[] = [];

  // validator patterns
  static numberPattern = "^[0-9]*$";
  static datePattern   = "^(0[1-9]|1[0-2])-(0[1-9]|[12]\d|3[01])-d{4}$";
  

  constructor(private formBuilder        : FormBuilder         ,
              private categoryService    : CategoriesService   ,
              private manufacturerService: ManufacturersService,
              private subCategoryService : SubCategoryService  ,
              private modelService       : ModelService        ,
              private itemService        : ItemService
              ) {
                  // form group for adding an item
                  this.formGroup = this.formBuilder.group({
                    itemName           : ["", [Validators.required]], 
                    selectedSubCategory: ["", [Validators.required]],
                    selectedModel      : "",
                    itemDescription    : "",
                    quantity           : ["",  [Validators.required]],
                    // validator pattern for date
                    // arrivalDate        : ["", [Validators.pattern("^(0[1-9]|1[0-2])-(0[1-9]|[12]\d|3[01])-d{4}$")]],
                    // warantyDate        : ["", [Validators.pattern("^(0[1-9]|1[0-2])-(0[1-9]|[12]\d|3[01])d{4}$")]],
                    existing           : true,
                    onMaintenance      : false,
                    imageUrl             : ""
                  }); 
                  // form group for category
                  this.formGroupCategory = this.formBuilder.group({
                    categoryName       : ["", [Validators.required]],
                  });
                  // form group for manufacturer
                  this.formGroupManufacturer = this.formBuilder.group({
                    manufacturerName   : ["", [Validators.required]],
                  });
                  // form group for adding a sub category
                  this.formGroupSubCategory = this.formBuilder.group({
                    subCategoryName    : ["", [Validators.required]],
                  });
                  // form group for adding a model
                  this.formGroupModel = this.formBuilder.group({
                    modelName          : ["", [Validators.required]],
                  });
                  // form group for adding serial numbers
                  this.formGroupSerialNumber = this.formBuilder.group({
                    serialNumbers: this.formBuilder.array([])
                  });
                  
  }

  get serialNumbers():  FormArray{
    return this.formGroupSerialNumber.controls['serialNumbers'] as FormArray;
  }


  getRange(count: number): number[] {
    const range = [];
    for (let i = 0; i < count; i++) {
      range.push(i);
    }
    return range;
  }


  generateSerialNumberFormControls(selectedQuantity: number) {
    this.serialNumbers.clear();

    for (let i = 0; i < selectedQuantity; i++) {
      const serialNumberForm = this.formBuilder.group({
        serialNumber: ['', Validators.maxLength(255)]
      });
      this.serialNumbers.push(serialNumberForm);
    }
  }


  ngOnInit() {
		this.categoryService.getAllCategories().subscribe(
			categories => this.categories = categories
		);
    this.manufacturerService.getAllManufacturers().subscribe(
      manufacturers => this.manufacturers = manufacturers
    );

	}


  onSelectedSubCatValueChange() {
    this.selectedSubCategory = this.formGroup.get('selectedSubCategory')?.value;
    // disable model input if the selected sub category doesn't have models
    for(let i = 0; i < this.categories.length; i++) 
      for(let j = 0; j < this.categories[i].subCategories!.length; j++) 
        if (this.categories[i].subCategories![j].name == this.selectedSubCategory) {
          this.categoryId = i;  
          if (this.categories[i].name !== "INFORMATIQUE") {
            this.formGroup.get('selectedModel')?.disable(); 
            this.selectedModel = "";
            return;
          } else {
            this.formGroup.get('selectedModel')?.enable(); 
            return;
          }
        }
          
  }


  onSelectedModelValueChange() {
    this.selectedModel = this.formGroup.get('selectedModel')?.value
  }
  

  openSubCategoryModal(categoryId: number) {
    // open modal for adding a sub categor
    if (categoryId == undefined) return;
    this.categoryId = categoryId;

  }
  
  openModelModal(manufacturerId: number) {
    // open modal for adding a model
    if (manufacturerId == undefined) return;
    this.manufacturerId = manufacturerId;
  }

  
  // add item to database
  onSubmit() {
    // assign formFields to attributes
    this.itemName            = this.formGroup.get('itemName'           )?.value;
    this.selectedSubCategory = this.formGroup.get('selectedSubCategory')?.value;
    this.selectedModel       = this.formGroup.get('selectedModel'      )?.value;
    this.quantity            = this.formGroup.get('quantity'           )?.value;
    this.itemDescription     = this.formGroup.get('itemDescription'    )?.value;
    this.onMaintenance       = this.formGroup.get('onMaintenance'      )?.value;
    this.existing            = this.formGroup.get('existing'           )?.value;
    this.imageUrl            = this.formGroup.get('imageUrl'           )?.value;

    // if the user didn't enter a description
    if (this.itemDescription == undefined) 
      this.itemDescription = null;  
    
    // if the user didn't enter an image url
    if (this.imageUrl == undefined)
      this.imageUrl = null;

      
    if (this.itemName.length != 0 && this.itemName != undefined ||
      this.selectedSubCategory.length != 0 && this.selectedSubCategory != undefined ||
      this.quantity != undefined && this.quantity < 1 ||
      this.existing != undefined || 
      this.onMaintenance != undefined) {

        // FIXME: REFACTOR THE BELLOW CODE !!!! 
      
      // if the category IS NOT informatique, we don't need to add a model
      if (this.categories[this.categoryId].name != "INFORMATIQUE" &&
          (this.selectedModel.length == 0 ||
            this.selectedModel == undefined)) { 


        const item: Item = {
          name: this.itemName,
          subCategory: {
            id: this.categories[this.categoryId].subCategories!.find(subCategory => subCategory.name == this.selectedSubCategory)!.id,
            name: this.selectedSubCategory,
            category: {
              id: this.categories[this.categoryId].id,
              name: this.categories[this.categoryId].name
            }
          },
          description: this.itemDescription,
          onMaintenance: this.onMaintenance,
          existing: this.existing,
          quantity: this.quantity,
          imageUrl: this.imageUrl
        }


        this.itemService.create(item).subscribe({
            next: () => {  
              if (item.quantity > 1 )             
                alert("Equipements créé avec succès");
              else 
                alert("Equipement créé avec succès");
            },
            error: () => {
              alert("Erreur lors de la création");
            }
          }
        );
      

      }
      // if the category is informatique, we need to add a model and possibly serial numbers
      else if ((this.categories[this.categoryId].name === "INFORMATIQUE"    || 
                this.categories[this.categoryId].name === "ELECTROMENAGER") &&
          (this.selectedModel.length > 0 || this.selectedModel != undefined)) {
            

        const subCategory = {
          id: this.categories[this.categoryId].subCategories!.find(subCategory => subCategory.name == this.selectedSubCategory)!.id,
          name: this.selectedSubCategory,
          category: {
            id: this.categories[this.categoryId].id,
            name: this.categories[this.categoryId].name
          }
        }

        const model = {
          id: this.manufacturers[this.manufacturerId].models!.find(model => model.reference == this.selectedModel)!.id,
          reference: this.selectedModel,
          manufacturer: {
            id: this.manufacturers[this.manufacturerId].id,
            name: this.manufacturers[this.manufacturerId].name
          }
        }
        
        if (this.imageUrl === "" || this.imageUrl === undefined) {
          this.imageUrl = null;
        }

        // assign what's independent of model
        const item: Item = {
          name: this.itemName,
          subCategory: subCategory,
          model: model,
          quantity: 1,
          description: this.itemDescription,
          onMaintenance: this.onMaintenance,
          existing: this.existing,
          imageUrl: this.imageUrl
        }

        // if it's only 1 item without a serial #
        if (this.serialNumberArray.length == 0 && this.quantity == 1) {
          this.itemService.create(item).subscribe({
              next: () => {
                alert("Equipement créé avec succès");
              },
              error: () => {
                alert("Erreur lors de la création de l'équipement");
              }
            });
        } // More than 1 item without serial #
        else if (this.serialNumberArray.length == 0 && this.quantity > 1) {
          let items: Item[] = [];
          for (let i = 0; i < this.quantity; i++) {
            // adding the same item to the array
            items.push(item);
          }
          // istead of calling the service multiple times, we call it just once
          this.itemService.createMultiple(items).subscribe({
              next: () => {
                alert("Equipements créés avec succès");
              },
              error: (error) => {
                alert("Erreur lors de la création des équipements  " + error);
              }
            }
          );
        } // multiple items and at least one serial # 
        else if (this.serialNumberArray.length > 0 && this.quantity > 1) {
          const items: Item[] = [];
          if (this.serialNumberArray.length < this.quantity) {
            for (let i = 0; i < this.quantity - this.serialNumberArray.length; i++)
              this.serialNumberArray.push(null);
          }
          for (let i = 0; i < this.serialNumberArray.length; i++) {
            const item: Item = {
              name: this.itemName,
              subCategory: subCategory,
              model: model,
              description: this.itemDescription,
              onMaintenance: this.onMaintenance,
              existing: this.existing,
              quantity: 1,
              serialNumber: this.serialNumberArray[i]
            }
            items.push(item);
          }
          
          this.itemService.createMultiple(items).subscribe({
              next: () => {
                alert("Equipement créé avec succès");
              },
              error: () => {
                alert("Erreur lors de la création de l'équipement");
              }
            });
        } else if (this.serialNumberArray.length > 0 && this.quantity == 1) {
          // beacuse serial numbers are unique they need to be null 
          if (this.serialNumberArray[0] == undefined || this.serialNumberArray[0] == "")
            this.serialNumberArray[0] = null;

          const item: Item = {
            name: this.itemName,
            subCategory: subCategory,
            model: model,
            description: this.itemDescription,
            onMaintenance: this.onMaintenance,
            existing: this.existing,
            quantity: 1,
            serialNumber: this.serialNumberArray[0]
          }

          this.itemService.create(item).subscribe({
              next: () => {
                alert("Equipement créé avec succès");
              },
              error: () => {
                alert("Erreur lors de la création de l'équipement");
              }
            });

        }
          // alert("Erreur lors de la création de l'équipement");

      }
    }
  }
  

  onSubmitCategory() {
    // add category to database
    this.categoryName = this.formGroupCategory.get('categoryName')?.value;

    if (this.categoryName.length != 0 && this.categoryName != undefined) {
      
      const category: Category = {
        name: this.categoryName
      }

      this.categoryService.create(category).subscribe(
        {
          next: () => { 
            this.categoryService.getAllCategories().subscribe(
              categories => this.categories = categories
            );
            this.formGroupCategory.get("categoryName")?.setValue("");
            alert("Catégorie créé avec succès");
          },
          // FIXME: error handling
          error: (error) => {
            console.log("Erreur lors de la création de la catégorie", error);
          }
        }
      );


    }
  }

  onSubmitManufacturer() {
    // add manufacturer to database
    this.manufacturerName = this.formGroupManufacturer.get('manufacturerName')?.value;

    if (this.manufacturerName.length != 0 && this.manufacturerName != undefined) {

      this.manufacturerName = this.manufacturerName.toUpperCase();
      const manufacturer: Manufacturer = {
        name: this.manufacturerName
      }

      this.manufacturerService.create(manufacturer).subscribe(
        {
          next: () => {
            this.manufacturerService.getAllManufacturers().subscribe(
              manufacturers => this.manufacturers = manufacturers
            );
            this.formGroupManufacturer.get("manufacturerName")?.setValue("");
            alert("Fabricant créé avec succès");
          },
        // FIXME: error handling
          error: (error) => {
            console.log("Erreur lors de la création du fabricant", error);
          }
        }
      );

    }    

  }

  onSubmitSubCategory() {
    // add sub category to database
    this.subCategoryName = this.formGroupSubCategory.get('subCategoryName')?.value;

    if (this.categoryId != 0 && this.subCategoryName.length != 0) {

      // find category by id from list of categories
      const category: Category | undefined = this.categories.find(category => category.id == this.categoryId);

      if (category == undefined) {
        console.log("Category not found");
        return;
      }

      category.id = this.categoryId;

      const subCategory: SubCategory = {
        name: this.subCategoryName,
        category: category
      }

      this.subCategoryService.create(subCategory).subscribe(
        {
          next: () => { 
            // update category with new sub category from the category list
            this.categoryService.getAllCategories().subscribe(
              categories => this.categories = categories
            );
            this.formGroupSubCategory.get("subCategoryName")?.setValue("");
            alert("Sous catégorie créé avec succès");
          },
          // FIXME: error handling
          error: (error) => {
            console.log("Une erreur est survenue, reesayez plus tard", error);
          }
        }
      );
    }
  }

  onSubmitModel() {
    // add model to database
    this.modelName = this.formGroupModel.get('modelName')?.value;

    if (this.manufacturerId != 0 && this.modelName.length != 0) {

      // find manufacturer by id from list of manufacturers
      const manufacturer: Manufacturer | undefined = this.manufacturers.find(manufacturer => manufacturer.id == this.manufacturerId);

      if (manufacturer == undefined) {
        console.log("Manufacturer not found");
        return;
      }

      manufacturer.id = this.manufacturerId;

      const model: Model = {
        reference: this.modelName,
        manufacturer: manufacturer
      }

      this.modelService.create(model).subscribe(
        {
          next: () => {
            // update manufacturer with new model from the manufacturer list
            this.manufacturerService.getAllManufacturers().subscribe(
              manufacturers => this.manufacturers = manufacturers
            );
            this.formGroupModel.get("modelName")?.setValue("");
            alert("Modèle créé avec succès");
            
          },
          // FIXME: error handling
          error: (error) => {
            console.log("Une erreur est survenue, reesayez plus tard", error);
          }
        }
      );
    }
  }

  // Get every serial number from the form
  onSubmitSerialNumbers() {
    this.serialNumberArray = [];
    if (this.serialNumbers.value.length > 0) { 
      for (let serialNumber of this.serialNumbers.value) {
        console.log(serialNumber.serialNumber);
        this.serialNumberArray.push(serialNumber.serialNumber);
      }
    }
  }

}

