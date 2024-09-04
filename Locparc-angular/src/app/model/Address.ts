import { Country } from "./Country";

export class Address {
    id             ?: number | null ;
    streetNumber   ?: string        ;
    street         ?: string        ;
    addressDetails ?: string        ;
    city           ?: string        ;
    postalCode     ?: string        ;
    country        ?: Country | null;

}