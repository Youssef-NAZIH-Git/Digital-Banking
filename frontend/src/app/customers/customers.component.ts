import { Component, OnInit } from '@angular/core';
import { catchError, delay, map, Observable, throwError } from 'rxjs';
import { Customer } from '../model/customer';
import { CustomerService } from '../services/customer.service';
import { FormBuilder, FormGroup, ReactiveFormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-customers',
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './customers.component.html',
  styleUrl: './customers.component.css'
})
export class CustomersComponent implements OnInit {
  customers! : Observable<Array<Customer>>;
  errorMessage! : string;
  searchFormGroup! : FormGroup

  constructor(private customerService : CustomerService, private fb : FormBuilder) { }

  ngOnInit(): void {
    this.searchFormGroup = this.fb.group({
      keyword: this.fb.control("")
    })

    this.customers = this.customerService.getCustomers().pipe(
      delay(1000), // Hada gha pour simuler delay a monsieur
      catchError(err => {
        this.errorMessage = err.message;
        return throwError(err);
      })
    )
  }

  searchForCustomer(){
    this.searchFormGroup.get
  }
}

