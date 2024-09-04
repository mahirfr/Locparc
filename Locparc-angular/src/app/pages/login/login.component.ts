import { Component } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { LoginService } from 'src/app/service/login.service';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent {

  hiddenContent = true;
  loginError    = false;
  showPassword  = true;

  constructor(private formBuilder: FormBuilder,
              private loginService: LoginService,
              private router: Router) {}

  form: FormGroup = this.formBuilder.group({
    email    : ["", [Validators.email, Validators.required]],
    password : ["", [Validators. required]]
  })

  onSubmit(): void {
    if (this.form.valid) {
      this.loginService.login(this.form.value)
        .subscribe(
          {
            next : (jwt) => {
              localStorage.setItem("jwt", jwt);
              this.loginService.updateConnectedUser();
              this.router.navigateByUrl("home");
            },
            error: (error) => {
              this.loginError = true
            }
          });
    }
  }

  togglePasswordVisibility() {
    this.showPassword = !this.showPassword;
  }

}
