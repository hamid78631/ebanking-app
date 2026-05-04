import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { forkJoin } from 'rxjs';
import { CustomerService } from '../services/customer.service';
import { AccountsService } from '../services/accounts.service';
import { Customer } from '../model/customer.model';
import { BankAccount } from '../model/account.model';

@Component({
  selector: 'app-customers',
  templateUrl: './customers.component.html',
  styleUrls: ['./customers.component.css']
})
export class CustomersComponent implements OnInit {
  customers: Customer[] = [];
  allAccounts: BankAccount[] = [];
  selectedCustomer: Customer | null = null;
  customerAccounts: BankAccount[] = [];
  errorMessage = '';
  loading = true;
  searchFormGroup: FormGroup;
  createAccountForm: FormGroup;
  showCreateForm = false;
  creatingAccount = false;
  createError = '';

  constructor(
    private customerService: CustomerService,
    private accountsService: AccountsService,
    private fb: FormBuilder,
    private route: ActivatedRoute,
    private router: Router
  ) {
    this.searchFormGroup = this.fb.group({ keyword: '' });
    this.createAccountForm = this.fb.group({
      accountType: ['current', Validators.required],
      initialBalance: [0, [Validators.required, Validators.min(0)]],
      overdraft: [500],
      interestRate: [3.5]
    });
  }

  ngOnInit(): void {
    this.loadData();
  }

  loadData() {
    this.loading = true;
    const kw = this.searchFormGroup.value.keyword || '';
    forkJoin({
      customers: this.customerService.searchCustomers(kw),
      accounts: this.accountsService.getBankAccounts()
    }).subscribe({
      next: ({ customers, accounts }) => {
        this.customers = customers;
        this.allAccounts = accounts;
        this.loading = false;
        const selectId = this.route.snapshot.queryParamMap.get('select');
        if (selectId) {
          const found = customers.find(c => c.id === +selectId);
          if (found) this.selectCustomer(found);
        }
      },
      error: err => { this.errorMessage = err.message; this.loading = false; }
    });
  }

  handleSearchCustomers() {
    this.loading = true;
    const kw = this.searchFormGroup.value.keyword || '';
    this.customerService.searchCustomers(kw).subscribe({
      next: data => { this.customers = data; this.loading = false; },
      error: err => { this.errorMessage = err.message; this.loading = false; }
    });
  }

  selectCustomer(c: Customer) {
    this.selectedCustomer = c;
    this.customerAccounts = this.allAccounts.filter(a => a.customerDTO?.id === c.id);
    this.showCreateForm = false;
    this.createError = '';
  }

  closeDetail() {
    this.selectedCustomer = null;
    this.showCreateForm = false;
  }

  handleDeleteCustomer(c: Customer) {
    if (!confirm(`Supprimer le client "${c.name}" ? Cette action est irréversible.`)) return;
    this.customerService.deleteCustomer(c.id).subscribe({
      next: () => {
        if (this.selectedCustomer?.id === c.id) this.closeDetail();
        this.loadData();
      },
      error: err => console.log(err)
    });
  }

  handleCreateAccount() {
    if (!this.selectedCustomer || this.createAccountForm.invalid) return;
    this.creatingAccount = true;
    this.createError = '';
    const { accountType, initialBalance, overdraft, interestRate } = this.createAccountForm.value;
    const obs = accountType === 'current'
      ? this.accountsService.createCurrentAccount(this.selectedCustomer.id, initialBalance, overdraft)
      : this.accountsService.createSavingAccount(this.selectedCustomer.id, initialBalance, interestRate);
    obs.subscribe({
      next: acc => {
        this.allAccounts.push(acc);
        this.customerAccounts = this.allAccounts.filter(a => a.customerDTO?.id === this.selectedCustomer!.id);
        this.showCreateForm = false;
        this.createAccountForm.reset({ accountType: 'current', initialBalance: 0, overdraft: 500, interestRate: 3.5 });
        this.creatingAccount = false;
      },
      error: err => {
        this.createError = err.error?.message || err.message;
        this.creatingAccount = false;
      }
    });
  }

  goToAccount(id: string) {
    this.router.navigate(['/admin/accounts'], { queryParams: { id } });
  }

  editCustomer(c: Customer) {
    this.router.navigate(['/admin/edit-customer', c.id]);
  }
}