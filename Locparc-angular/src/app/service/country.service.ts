import { Injectable } from '@angular/core';
import { environment } from 'src/environments/environment';
import { Country } from '../model/Country';
import { Observable } from 'rxjs';
import { HttpClient } from '@angular/common/http';

@Injectable({
  providedIn: 'root'
})
export class CountryService {

  URL = environment.serverUrl;  
  

  constructor(private http: HttpClient) { }

  getAll(): Observable<Country[]> {
    return this.http.get<Country[]>(`${this.URL}/api/countries/`)
  }

  getByName(name: string): Observable<Country> {
    return this.http.get<Country>(`${this.URL}/api/${name}`)
  }

}
