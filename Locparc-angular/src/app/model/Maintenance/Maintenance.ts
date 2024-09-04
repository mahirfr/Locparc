import { Item } from "../Item";
import { Repairer } from "./Repairer";

export interface Maintenance {
    id           ?: number,
    dateSent     ?: Date,
    dateRecieved ?: Date,
    incident     ?: string,
    repairCost   ?: number,
    repairer     ?: Repairer,
    item         ?: Item    
}