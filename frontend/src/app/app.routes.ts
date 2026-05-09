import { Routes } from '@angular/router';
import { CustomersComponent } from './components/customers/customers.component';
import { AccountsComponent } from './components/accounts/accounts.component';
import { NewCustomerComponent } from './components/customers/new-customer/new-customer.component';

export const routes: Routes = [
    {path: "customers", component: CustomersComponent},
    {path: "accounts", component: AccountsComponent},
    {path: "new-customer", component: NewCustomerComponent}
];
