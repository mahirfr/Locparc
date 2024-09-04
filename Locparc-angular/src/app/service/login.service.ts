import { Injectable                  } from '@angular/core'                ;
import { User                        } from '../model/User'                ;
import { HttpClient                  } from '@angular/common/http'         ;
import { BehaviorSubject, Observable } from 'rxjs'                         ;
import { Router                      } from '@angular/router'              ;
import { environment                 } from 'src/environments/environment' ;


@Injectable({
  /* This option makes the service available as singleton at root level */
  providedIn: 'root'
})
export class LoginService {

  public _connectedUser = new BehaviorSubject<User | null>(null);
  URL = environment.serverUrl;

  
  constructor(private http:   HttpClient,
              private router: Router) { 
    this.updateConnectedUser()
  }


  updateConnectedUser() {
    
    const jwt = localStorage.getItem("jwt");

    if (jwt != null) {
      const payload = jwt.split(".")[1];
      const json = window.atob(payload);
      const userData = JSON.parse(json);
      const user: User = {
        email    : userData.sub      ,
        firstName: userData.firstName,
        lastName : userData.lastName ,
        role     : { name: userData.role }
      }
      this._connectedUser.next(user);

    } else {
      this._connectedUser.next(null);
    } 
  }

  login(user: User): Observable<string> {
    /** Post method sends the user to the url as well as header where the header indicates the type of response.
    It returns an Obsevable which is asyncronus */
    return this.http.post(this.URL + "/api/login", user, {responseType: "text"});
  }


  logout() {
    localStorage.removeItem("jwt");
    this._connectedUser.next(null);
    this.router.navigateByUrl("login"); 
  }
}
