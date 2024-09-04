import { HttpClient  } from '@angular/common/http'          ;
import { Injectable  } from '@angular/core'                 ;
import { SubCategory } from '../model/Category/SubCategory' ;
import { Observable  } from 'rxjs'                          ;
import { environment } from 'src/environments/environment'  ;

@Injectable({
  providedIn: 'root'
})
export class SubCategoryService {

  URL = environment.serverUrl;

  constructor(private http: HttpClient) { }

  create(subCategory: SubCategory): Observable<SubCategory> {
    return this.http.post<SubCategory>(`${this.URL}/api/items/admin/sub-categories`, subCategory);
  }

  getById(id: number) {
    return this.http.get(`${this.URL}/api/items/sub-categories` + id);
  }

}
