import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router'; // Import manquant
import { CustomersComponent } from './customers/customers.component';
import { AccountsComponent } from './accounts/accounts.component';
import { NewCustomerComponent } from './new-customer/new-customer.component';
import { LoginComponent } from './login/login.component';
import { AdminTemplateComponent } from './admin-template/admin-template.component';
import { AuthenticationGuard } from './guards/authentication.guard';
import { AuthorizationGuard } from './guards/authorization.guard';
import { NotAuthorizedComponent } from './not-authorized/not-authorized.component';

const routes: Routes = [
  { path: "login", component: LoginComponent },
  { path: "", redirectTo: "/login", pathMatch: "full" },
  {
    path: "admin", component: AdminTemplateComponent, canActivate: [AuthenticationGuard],
    children: [
      { path: "accounts", component: AccountsComponent },
      { path: "new-customer", component: NewCustomerComponent, canActivate: [AuthorizationGuard] },
      { path: "customers", component: CustomersComponent },
      { path: "notAuthorized", component: NotAuthorizedComponent }
    ]
  }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
