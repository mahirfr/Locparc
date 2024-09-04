import { Address } from "../Address";

export interface Repairer {
    id     ?: number,
    name    : string,
    phone  ?: string,
    email  ?: string,
    address ?: Address
}