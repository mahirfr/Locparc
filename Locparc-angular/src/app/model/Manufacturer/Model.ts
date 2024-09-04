import { Manufacturer } from "./Manufacturer";

export interface Model {
    id         ?: number,
    reference   : string,
    manufacturer: Manufacturer
}