import { HttpClient  } from '@angular/common/http'         ;
import { Injectable  } from '@angular/core'                ;
import { Observable  } from 'rxjs'                         ;
import { Model       } from '../model/Manufacturer/Model'  ;
import { environment } from 'src/environments/environment' ;

@Injectable({
  providedIn: 'root'
})
export class ModelService {

  URL = environment.serverUrl;

  constructor(private http: HttpClient) { }

  getAll(): Observable<any> {
    return this.http.get(this.URL + '/api/items/models');
  }

  // getById(id: number): Observable<any> {
  //   return this.http.get(this.URL + '/api/items/models/' + id);
  // }

  create(model: Model): Observable<Model> {
    return this.http.post<Model>(this.URL + '/api/items/admin/models', model);
  }

  delete(id: number) {
    return this.http.delete(this.URL + '/api/items/admin/models/' + id);
  }
  
}
