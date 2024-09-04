import { User } from "./User";

export interface Request {
    id            : number,
    approved      : boolean,
    responseDate  : Date,
    motive       ?: string,
    admin        ?: User
}