import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { forkJoin } from 'rxjs';
import { CustomerService } from '../services/customer.service';
import { AccountsService } from '../services/accounts.service';
import { BankAccount } from '../model/account.model';
import { Customer } from '../model/customer.model';

@Component({
  selector: 'app-dashboard',
  templateUrl: './dashboard.component.html',
  styles: []
})
export class DashboardComponent implements OnInit {
  totalCustomers = 0;
  totalAccounts  = 0;
  totalBalance   = 0;
  currentAccounts = 0;
  savingAccounts  = 0;
  recentAccounts: BankAccount[] = [];
  recentCustomers: Customer[]   = [];
  loading = true;

  constructor(
    private customerService: CustomerService,
    private accountsService: AccountsService,
    private router: Router
  ) {}

  ngOnInit(): void {
    forkJoin({
      customers: this.customerService.getCustomers(),
      accounts:  this.accountsService.getBankAccounts()
    }).subscribe({
      next: ({ customers, accounts }) => {
        this.totalCustomers  = customers.length;
        this.totalAccounts   = accounts.length;
        this.totalBalance    = accounts.reduce((s, a) => s + (a.balance || 0), 0);
        this.currentAccounts = accounts.filter(a => a.type === 'CurrentAccount').length;
        this.savingAccounts  = accounts.filter(a => a.type === 'SavingAccount').length;
        this.recentAccounts  = [...accounts].sort((a, b) =>
          new Date(b.createdAt).getTime() - new Date(a.createdAt).getTime()
        ).slice(0, 5);
        this.recentCustomers = customers.slice(0, 5);
        this.loading = false;
      },
      error: () => { this.loading = false; }
    });
  }

  goToAccount(id: string) {
    this.router.navigate(['/admin/accounts'], { queryParams: { id } });
  }

  goToCustomer(id: number) {
    this.router.navigate(['/admin/customers'], { queryParams: { select: id } });
  }
}