import { Injectable } from '@angular/core';
import { ActivatedRouteSnapshot, CanActivate, Router, RouterStateSnapshot, UrlTree } from '@angular/router';
import { Observable } from 'rxjs';
import { LoginService } from '../service/login.service';

@Injectable({
  providedIn: 'root'
})
export class AdminGuard {
  constructor(private loginService: LoginService,
              private router      : Router) { }

  canActivate(
  route: ActivatedRouteSnapshot,
  state: RouterStateSnapshot): Observable<boolean | UrlTree> | Promise<boolean | UrlTree> | boolean | UrlTree {

  if (this.loginService._connectedUser.value?.role?.name == "ROLE_ADMIN") 
    return true;
  

  return this.router.parseUrl("/access-denied");

}
  
}
