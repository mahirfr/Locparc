import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Category } from '../model/Category/Category';
import { Observable, catchError, throwError } from 'rxjs';
import { environment } from 'src/environments/environment';

@Injectable({
  providedIn: 'root'
})
export class CategoriesService {

  URL = environment.serverUrl;  

  constructor(private http: HttpClient) { }

  create(category: Category): Observable<Category> {
    return this.http.post<Category>(`${this.URL}/api/items/admin/categories`, category);
  }

  getAllCategories(): Observable<Category[]> {
    return this.http.get<Category[]>(this.URL + "/api/items/categories")
  }

}
