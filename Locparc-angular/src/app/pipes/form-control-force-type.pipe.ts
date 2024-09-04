import { Pipe, PipeTransform } from '@angular/core';
import { AbstractControl, FormGroup } from '@angular/forms';

@Pipe({
  name: 'formControlForceType'
})
export class FormControlForceTypePipe implements PipeTransform {

  transform(value: AbstractControl): FormGroup {
    return value as FormGroup;
  }

}
