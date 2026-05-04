import { Injectable } from '@angular/core';
import { HttpClient } from "@angular/common/http";
import { environment } from "../../environments/environment";
import { Observable } from "rxjs";
import { AccountDetails, BankAccount } from "../model/account.model";

@Injectable({ providedIn: 'root' })
export class AccountsService {

  constructor(private http: HttpClient) {}

  getAccount(accountId: string, page: number, size: number): Observable<AccountDetails> {
    return this.http.get<AccountDetails>(
      `${environment.backendHost}/accounts/${accountId}/pageOperations?page=${page}&size=${size}`
    );
  }

  getBankAccounts(): Observable<BankAccount[]> {
    return this.http.get<BankAccount[]>(`${environment.backendHost}/accounts`);
  }

  createCurrentAccount(customerId: number, initialBalance: number, overdraft: number): Observable<BankAccount> {
    return this.http.post<BankAccount>(`${environment.backendHost}/accounts/current`,
      { customerId, initialBalance, overdraft });
  }

  createSavingAccount(customerId: number, initialBalance: number, interestRate: number): Observable<BankAccount> {
    return this.http.post<BankAccount>(`${environment.backendHost}/accounts/saving`,
      { customerId, initialBalance, interestRate });
  }

  debit(accountId: string, amount: number, description: string) {
    return this.http.post(`${environment.backendHost}/accounts/debit`,
      { accountId, amount, description });
  }

  credit(accountId: string, amount: number, description: string) {
    return this.http.post(`${environment.backendHost}/accounts/credit`,
      { accountId, amount, description });
  }

  transfer(accountSource: string, accountDestination: string, amount: number, description: string) {
    return this.http.post(`${environment.backendHost}/accounts/transfer`,
      { accountSource, accountDestination, amount, description });
  }
}