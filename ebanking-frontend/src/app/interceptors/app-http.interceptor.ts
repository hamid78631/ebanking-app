import { Injectable } from '@angular/core';
import { HttpRequest, HttpHandler, HttpEvent, HttpInterceptor } from '@angular/common/http';
import { Observable, catchError, throwError } from 'rxjs';
import { AuthService } from '../services/auth.service';

@Injectable()
export class AppHttpInterceptor implements HttpInterceptor {

  constructor(private authService: AuthService) {}

  intercept(request: HttpRequest<unknown>, next: HttpHandler): Observable<HttpEvent<unknown>> {
    // On vérifie si la requête est destinée au login ou au chatbot
    // Si c'est le cas, on ne rajoute pas le Header Authorization
    if (!request.url.includes("/login") && !request.url.includes("/chat")) {
      let newRequest = request.clone({
        setHeaders: {
          Authorization: `Bearer ${this.authService.accessToken}`
        }
      });

      return next.handle(newRequest).pipe(
        catchError(err => {
          // Si le backend renvoie 401 malgré le token, on déconnecte l'utilisateur
          if (err.status == 401) {
            this.authService.logout();
          }
          return throwError(() => err);
        })
      );
    }

    // Pour /login et /chat, on laisse passer la requête telle quelle
    return next.handle(request);
  }
}
