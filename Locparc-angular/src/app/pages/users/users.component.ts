import { Component } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { User } from 'src/app/model/User';
import { UserService } from 'src/app/service/user.service';

@Component({
  selector: 'app-users',
  templateUrl: './users.component.html',
  styleUrls: ['./users.component.css']
})
export class UsersComponent {

  searchFormGroup : FormGroup;
  users           : User[] = [];
  newListOfUsers  : User[] = [];
  searchQuery     : string = "";


  formGroup     : FormGroup  ;
  userFirstName : string = "";
  userLastName  : string = "";
  userEmail     : string = "";

  formGroupMultiple : FormGroup;
  file              : File = new File([""], "");


  getUserId(id: any): any {
    return 1;
  }


  constructor(private formBuilder: FormBuilder,
              private userService: UserService) { 

    this.userService.getAllUsers().subscribe(
      (data: User[]) => {
        this.users = data;
        console.log(this.users);
      }
    );

    this.searchFormGroup = this.formBuilder.group({
      searchQuery: [""]
    });

    this.formGroup = this.formBuilder.group({
      userLastName  : [""  , [Validators.required, Validators.minLength(2), Validators.maxLength(50)]],
      userFirstName : [""  , [Validators.required, Validators.minLength(2), Validators.maxLength(50)]],
      userEmail     : [""  , [Validators.required, Validators.email                                 ]],
    });

    this.formGroupMultiple = this.formBuilder.group({ 
      file: [null, Validators.required]
    });
  }

  deleteUser() {
    throw new Error('Method not implemented.');
  }

  openDialog() {
    throw new Error('Method not implemented.');
  }



  onSubmitFormGroup() {
    console.log(this.formGroup.value);
  }

  // TODO: Add function to create users
  onSubmitFormGroupMultiple() {
    console.log(this.file);
    console.log("je suis dans la fonction onSubmitFormGroupMultiple");
  }

  onSelectedFile(event: any) {
    if (event.target.files.length > 0) {
      this.file = event.target.files[0];
      console.log(this.file);
      this.convertToJson();
    }
  }

  // TODO: finish file conversion
  convertToJson() {
  
    const reader = new FileReader();
    reader.readAsText(this.file);
    reader.onload = () => {
      reader.result;
    }
  }

  // TODO: Add function to map json to user
  mapToUser() {
    // const userString = this.convertToJson();
    // const userJson = JSON.parse(userString);
    // console.l
  }


  onSubmitSearchFormGroup() {
    this.searchQuery = this.searchFormGroup.get('searchQuery')?.value;
    this.newListOfUsers = [];

    if (this.searchQuery.length > 0) {
      this.users.forEach(user => {
        if (user.firstName.toLowerCase().startsWith(this.searchQuery.toLowerCase())) {
          this.newListOfUsers.push(user);
        }
      });
    } 
    if (this.newListOfUsers.length === 0) {
      alert("Aucun utilisateur ne correspond à votre recherche");
    }
  }

}
