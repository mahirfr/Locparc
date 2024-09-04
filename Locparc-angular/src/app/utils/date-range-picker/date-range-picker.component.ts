import { Component } from '@angular/core';
import { FormControl, FormGroup } from '@angular/forms';

@Component({
  selector: 'app-date-range-picker',
  templateUrl: './date-range-picker.component.html',
  styleUrls: ['./date-range-picker.component.css']
})
export class DateRangePickerComponent {

  dateRange = new FormGroup({
    start: new FormControl(),
    end: new FormControl()
  });

  isValidDateRange(startDate: Date, endDate: Date) {
    return endDate >= startDate;
  }
  
}
