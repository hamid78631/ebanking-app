import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  isAuthenticated: boolean = true;
  roles: string = 'USER ADMIN';
  username: string = 'Administrateur';

  login() {}
  logout() {}
  hasRole(role: string): boolean { return true; }
  loadProfile(data: any) {}
  loadToken() {}
}