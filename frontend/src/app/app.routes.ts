import { Routes } from '@angular/router';
import { CustomersComponent } from './components/customers/customers.component';
import { AccountsComponent } from './components/accounts/accounts.component';

export const routes: Routes = [
    {path: "customers", component: CustomersComponent},
    {path: "accounts", component: AccountsComponent}
];
