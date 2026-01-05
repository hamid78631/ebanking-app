import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { jwtDecode } from "jwt-decode";
import { Router } from "@angular/router";

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  isAuthenticated: boolean = false;
  roles: any;
  username: any;
  accessToken!: string;

  constructor(private http: HttpClient, private router: Router) {
    this.loadToken();
  }

  public login(username: string, password: string): Observable<any> {
    let options = {
      headers: new HttpHeaders().set("Content-Type", "application/x-www-form-urlencoded")
    };
    let params = new HttpParams().set("username", username).set("password", password);
    return this.http.post("http://localhost:8086/login", params, options);
  }

  loadProfile(data: any) {
    this.isAuthenticated = true;
    this.accessToken = data['access-token'];
    let decodedJwt: any = jwtDecode(this.accessToken);
    this.username = decodedJwt.sub;
    this.roles = decodedJwt.scope;
    window.localStorage.setItem("jwt-token", this.accessToken);
  }

  logout() {
    this.isAuthenticated = false;
    this.accessToken = undefined!;
    this.username = undefined;
    this.roles = undefined;
    window.localStorage.removeItem("jwt-token");
    this.router.navigateByUrl("/login");
  }

  public hasRole(role: string): boolean {
    return (this.roles && this.roles.includes(role));
  }

  loadToken() {
    let token = window.localStorage.getItem("jwt-token");
    if (token) {
      this.loadProfile({ "access-token": token });
    }
  }
}
