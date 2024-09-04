import { Injectable } from '@angular/core';
import { ActivatedRouteSnapshot, Router, RouterStateSnapshot, UrlTree } from '@angular/router';
import { Observable } from 'rxjs';
import { LoginService } from '../service/login.service';

@Injectable({
  providedIn: 'root'
})
export class UserGuard {

  constructor(private loginService: LoginService,
              private router      : Router) {}


  canActivate(
        route: ActivatedRouteSnapshot,
        state: RouterStateSnapshot): Observable<boolean | UrlTree> | Promise<boolean | UrlTree> | boolean | UrlTree {
          
    if (this.loginService._connectedUser.value != null) 
      return true;

    return this.router.parseUrl("/login");
  }
  
}
