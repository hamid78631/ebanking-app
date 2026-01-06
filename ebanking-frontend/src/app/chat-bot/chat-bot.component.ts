import { Component } from '@angular/core';
import { ChatService } from '../services/chat.service';

@Component({
  selector: 'app-chat-bot',
  templateUrl: './chat-bot.component.html',
  styleUrls: ['./chat-bot.component.css']
})
export class ChatBotComponent {
  messages: {text: string, isUser: boolean}[] = [];
  userInput: string = '';
  isTyping: boolean = false;
  public isOpen: boolean = false;

  constructor(private chatService: ChatService) {}

toggleChat() {
    this.isOpen = !this.isOpen;
  }
  sendMessage() {
    if (!this.userInput.trim()) return;


    this.messages.push({ text: this.userInput, isUser: true });


    const aiMessage = { text: '', isUser: false };
    this.messages.push(aiMessage);

    this.isTyping = true;
    const messageToSend = this.userInput;
    this.userInput = '';


    this.chatService.getAiResponse(messageToSend).subscribe({
      next: (chunk) => {
        aiMessage.text += chunk;
      },
      complete: () => {
        this.isTyping = false;
      },
      error: () => {
        aiMessage.text = "Erreur de connexion avec l'IA.";
        this.isTyping = false;
      }
    });
  }
}
