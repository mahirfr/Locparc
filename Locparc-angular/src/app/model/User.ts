import { Address } from "./Address";
import { Role } from "./Role";

export interface User {
    id          ?: number | null,
    firstName    : string,
    lastName     : string,
    email        : string,
    password    ?: string,
    phoneNumber ?: string | null,
    role        ?: Role | null,
    address     ?: Address | null
}