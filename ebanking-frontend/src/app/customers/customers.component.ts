import { Component, OnInit } from '@angular/core';
import { CustomerService } from '../services/customer.service';
// CORRECTION : Ajout de catchError et throwError dans les imports de rxjs
import { catchError, Observable, throwError } from 'rxjs';
import { Customer } from '../model/customer.model';

@Component({
  selector: 'app-customers',
  templateUrl: './customers.component.html',
  styleUrls: ['./customers.component.css']
})
export class CustomersComponent implements OnInit {

  // Utilisation du signe ! pour indiquer que l'observable sera initialisé dans ngOnInit
  customers!: Observable<Array<Customer>>;
  errorMessage!: string;

  constructor(private customerService: CustomerService) { }

  ngOnInit(): void {
    // CORRECTION : Utilisation de .pipe() pour intercepter l'erreur avant l'affichage
    this.customers = this.customerService.getCustomers().pipe(
      catchError(err => {
        this.errorMessage = err.message;
        // Syntaxe RxJS 7+ : throwError attend une fonction d'usine (factory function)
        return throwError(() => err);
      })
    );
  }
}
