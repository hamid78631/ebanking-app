import { Injectable, NgZone } from '@angular/core';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class ChatService {
  // L'URL de ton contrôleur Spring Boot
  private backendUrl = 'http://localhost:8086/chat';

  constructor(private zone: NgZone) {}

  getAiResponse(message: string): Observable<string> {
    return new Observable(observer => {
      // EventSource permet de lire le flux mot par mot envoyé par Gemini
      const eventSource = new EventSource(`${this.backendUrl}?message=${encodeURIComponent(message)}`);

      eventSource.onmessage = (event) => {
        this.zone.run(() => {
          observer.next(event.data);
        });
      };

      eventSource.onerror = (error) => {
        this.zone.run(() => {
          observer.error(error);
          eventSource.close();
        });
      };

      return () => eventSource.close();
    });
  }
}
