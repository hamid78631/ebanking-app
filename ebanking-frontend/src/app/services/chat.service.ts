import { Injectable, NgZone } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from "../../environments/environment";

@Injectable({
  providedIn: 'root'
})
export class ChatService {
  private backendUrl = `${environment.backendHost}/chat`;

  constructor(private zone: NgZone) {}

  getAiResponse(message: string): Observable<string> {
    return new Observable(observer => {
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