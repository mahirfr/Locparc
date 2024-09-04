import { HttpClient  } from '@angular/common/http'         ;
import { Injectable  } from '@angular/core'                ;
import { User        } from '../model/User'                ;
import { Observable  } from 'rxjs'                         ;
import { environment } from 'src/environments/environment' ;

@Injectable({
  providedIn: 'root'
})
export class UserService {

  URL = environment.serverUrl;

  constructor(private http: HttpClient) { }

  getAllUsers(): Observable<User[]> {
    return this.http.get<User[]>(this.URL + "/api/admin/users");
  }

  getUserById(id?: number): Observable<User> {
    return this.http.get<User>(this.URL + "/api/users/" + id);
  }
  
  getUserByEmail(email?: string): Observable<User> {
    return this.http.get<User>(this.URL + "/api/users/email/" + email);
  }
  
  addUser(user: User) {
    return this.http.post(this.URL + "/api/users/", user);
  }

  addUsers(users: User[]) {
    return this.http.post(this.URL + "/api/users/multiple", users);
  }

  updateUser(user: User) {
    return this.http.put(this.URL + "/api/users/", user);
  }

  deleteUser(id?: number) {
    return this.http.delete(this.URL + "/api/users/" + id);
  }

}
