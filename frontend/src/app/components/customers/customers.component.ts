import { Component, OnInit } from '@angular/core';
import { catchError, Observable, throwError } from 'rxjs';
import { Customer } from '../../models/customer';
import { CustomerService } from '../../services/customer.service';
import { FormBuilder, FormGroup, ReactiveFormsModule } from '@angular/forms';
import { RouterLink } from '@angular/router';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-customers',
  imports: [CommonModule, ReactiveFormsModule, RouterLink],
  templateUrl: './customers.component.html',
  styleUrl: './customers.component.css'
})
export class CustomersComponent implements OnInit {
  customers! : Observable<Array<Customer>>;
  errorMessage! : string;
  deleteSucessMessage! : string;
  deleteErrorMessage! : string;
  searchFormGroup! : FormGroup

  constructor(private customerService : CustomerService, private fb : FormBuilder) { }

  ngOnInit(): void {
    this.searchFormGroup = this.fb.group({
      keyword: this.fb.control("")
    });

    this.searchForCustomer();
  }

  searchForCustomer(){
    let kw = this.searchFormGroup?.value.keyword;
    this.customers = this.customerService.searchCustomers(kw).pipe(
      catchError(err => {
        this.errorMessage = err.message;
        return throwError(err);
      })
    )
  }

  deleteCustomer(customerToDelete: Customer){
    this.customerService.deleteCustomer(customerToDelete.id).subscribe({
      next : _ => {
        this.deleteSucessMessage = 'Customer deleted sucessfully';
        setTimeout(() => {this.deleteSucessMessage = ''}, 3000);
        this.searchForCustomer();
      },
      error : err => {
        this.errorMessage = err.message;
        setTimeout(() => {this.errorMessage = ''}, 3000);
        this.searchForCustomer();
      }
    })
  }
}

