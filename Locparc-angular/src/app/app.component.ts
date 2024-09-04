import { Component } from '@angular/core';
import { LoginService } from './service/login.service';
import { User } from './model/User';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {
  title = 'locparc';
  connectedUser: User | null = null;
  isAdmin = false;

  

  constructor(private loginService: LoginService) {}

  ngOnInit() {
    this.loginService._connectedUser.subscribe(
      (user) => {
        this.connectedUser = user,
        this.isAdmin = user?.role?.name == "ROLE_ADMIN"
      }
    )
  }

  onLogout() {
    this.loginService.logout();
  }

  
}
