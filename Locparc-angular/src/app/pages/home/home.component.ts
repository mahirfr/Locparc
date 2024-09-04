import { Component } from '@angular/core';
import { User } from 'src/app/model/User';
import { LoginService } from 'src/app/service/login.service';

@Component({
	selector: 'app-home',
	templateUrl: './home.component.html',
	styleUrls: ['./home.component.css']
})
export class HomeComponent {
	connectedUser: User | null = null;
	isAdmin: boolean = false;

	constructor(private _loginService: LoginService) { }

	ngOnInit() {
		this._loginService._connectedUser.subscribe(
			(user) => {
				this.connectedUser = user,
				this.isAdmin = user?.role?.name == "ROLE_ADMIN"
			}
		)
	}


}
