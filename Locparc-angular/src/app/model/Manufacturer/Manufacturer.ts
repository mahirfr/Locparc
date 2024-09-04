import { Model } from "./Model";

export interface Manufacturer {
    id     ?: number,
    name    : string,
    models ?: Model[]
}