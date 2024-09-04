import { SubCategory } from "./Category/SubCategory";
import { Model } from "./Manufacturer/Model";

export interface Item {
    id            ?: number,
    serialNumber  ?: string | null,
    arrivalDate   ?: Date,
    name           : string,
    description   ?: string | null,
    existing       : boolean,
    quantity       : number,
    pricePerDay   ?: number,
    waranty       ?: Date,
    onMaintenance  : boolean,
    imageUrl      ?: string | null,
    subCategory   ?: SubCategory,
    model         ?: Model
}