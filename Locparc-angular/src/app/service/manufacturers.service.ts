import { HttpClient   } from '@angular/common/http'               ;
import { Injectable   } from '@angular/core'                      ;
import { Manufacturer } from '../model/Manufacturer/Manufacturer' ;
import { Observable   } from 'rxjs'                               ;
import { environment  } from 'src/environments/environment'       ;

@Injectable({
  providedIn: 'root'
})
export class ManufacturersService {

  URL: string = environment.serverUrl;
  
  constructor(private http: HttpClient) { }

  create(manufacturer: Manufacturer): Observable<Manufacturer> {
    return this.http.post<Manufacturer>(`${this.URL}/api/items/admin/manufacturers`, manufacturer);
  }
  
  // TODO: call an api to get all manufacturers 
  getAllManufacturers(): Observable<Manufacturer[]> {
    return this.http.get<Manufacturer[]>(`${this.URL}/api/items/manufacturers`);
  }
  
}