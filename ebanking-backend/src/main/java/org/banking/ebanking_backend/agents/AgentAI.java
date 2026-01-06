package org.banking.ebanking_backend.agents;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

@Component
public class AgentAI {
    private final ChatClient chatClient;

    // Le builder va automatiquement récupérer la config 'rest' du properties
    public AgentAI(ChatClient.Builder builder) {
        this.chatClient = builder
                .defaultSystem("Tu es l'assistant de eBanking App.")
                .build();
    }

    public Flux<String> chat(String message) {
        return chatClient.prompt()
                .user(message)
                .stream()
                .content();
    }
}