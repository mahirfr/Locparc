import { Item } from "./Item";

export class OrderItems {
    orderId    ?: number ;
    itemId     ?: number ;
    item       ?: Item   ;
    returnDate ?: any    ;
    approved   ?: boolean;
}