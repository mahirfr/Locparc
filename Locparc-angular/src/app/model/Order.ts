import { Address } from "./Address";
import { OrderItems } from "./OrderItems";
import { Request } from "./Request";
import { User } from "./User";

export class Order {
    id           ?: number | undefined;
    startDate    ?: any;
    endDate      ?: any;
    event        ?: string;
    user         ?: User;
    orderedItems ?: OrderItems[];
    request      ?: Request;
    address      ?: Address;
}