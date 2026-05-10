import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { Customer } from '../../../models/customer';
import { delay } from 'rxjs';
import { Router } from '@angular/router';
import { CommonModule } from '@angular/common';
import { CustomerService } from '../../../services/customer/customer.service';

@Component({
  selector: 'app-new-customer',
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './new-customer.component.html',
  styleUrl: './new-customer.component.css'
})
export class NewCustomerComponent implements OnInit {
  addFormGroup! : FormGroup;
  successMessage! : string;
  errorMessage! : string;

  constructor(private customerService: CustomerService, private fb: FormBuilder, private router: Router) {};
  
  ngOnInit(): void {
    this.addFormGroup = this.fb.group({
      name: this.fb.control(null, [Validators.required, Validators.minLength(3)]),
      email: this.fb.control(null, [Validators.required, Validators.email])
    })
  }

  addCustomer(){
    let customer : Customer = this.addFormGroup.value;
    this.customerService.saveCustomer(customer).subscribe({
      next : data => {
        this.successMessage = 'Customer successfully added, redirecting in 2 seconds'
        setTimeout(() => this.router.navigateByUrl('/customers'), 2000);
      },
      error : err => {
        this.errorMessage = 'Error: ' + err.message;
      }
    })
  }
}
