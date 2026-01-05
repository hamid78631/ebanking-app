import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup } from "@angular/forms";
import { AccountsService } from "../services/accounts.service";
import { catchError, Observable, throwError } from "rxjs";
import { AccountDetails } from "../model/account.model";
import { AuthService } from "../services/auth.service"; // 1. Import de l'AuthService

@Component({
  selector: 'app-accounts',
  templateUrl: './accounts.component.html',
  styleUrls: ['./accounts.component.css']
})
export class AccountsComponent implements OnInit {
  accountFormGroup! : FormGroup;
  currentPage : number = 0;
  pageSize : number = 5;
  accountObservable! : Observable<AccountDetails>
  operationFromGroup! : FormGroup;
  errorMessage! : string;

  // 2. Injection de l'AuthService dans le constructeur
  constructor(private fb : FormBuilder,
              private accountService : AccountsService,
              public authService : AuthService) { }

  ngOnInit(): void {
    this.accountFormGroup = this.fb.group({
      accountId : this.fb.control('')
    });
    this.operationFromGroup = this.fb.group({
      operationType : this.fb.control(null),
      amount : this.fb.control(0),
      description : this.fb.control(null),
      accountDestination : this.fb.control(null)
    })
  }

  handleSearchAccount() {
    let accountId : string = this.accountFormGroup.value.accountId;
    this.accountObservable = this.accountService.getAccount(accountId, this.currentPage, this.pageSize).pipe(
      catchError(err => {
        this.errorMessage = err.message;
        return throwError(err);
      })
    );
  }

  gotoPage(page: number) {
    this.currentPage = page;
    this.handleSearchAccount();
  }

  handleAccountOperation() {

    if (!this.authService.roles.includes("ADMIN")) {
      alert("Vous n'avez pas les droits pour effectuer cette opération.");
      return;
    }

    let accountId : string = this.accountFormGroup.value.accountId;
    let operationType = this.operationFromGroup.value.operationType;
    let amount : number = this.operationFromGroup.value.amount;
    let description : string = this.operationFromGroup.value.description;
    let accountDestination : string = this.operationFromGroup.value.accountDestination;

    if(operationType == 'DEBIT'){
      this.accountService.debit(accountId, amount, description).subscribe({
        next : (data) => {
          alert("Success Debit");
          this.operationFromGroup.reset();
          this.handleSearchAccount();
        },
        error : (err) => console.log(err)
      });
    } else if(operationType == 'CREDIT'){
      this.accountService.credit(accountId, amount, description).subscribe({
        next : (data) => {
          alert("Success Credit");
          this.operationFromGroup.reset();
          this.handleSearchAccount();
        },
        error : (err) => console.log(err)
      });
    } else if(operationType == 'TRANSFER'){
      this.accountService.transfer(accountId, accountDestination, amount, description).subscribe({
        next : (data) => {
          alert("Success Transfer");
          this.operationFromGroup.reset();
          this.handleSearchAccount();
        },
        error : (err) => console.log(err)
      });
    }
  }
}
