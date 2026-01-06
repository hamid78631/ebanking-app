package org.banking.ebanking_backend.web;

import org.banking.ebanking_backend.agents.AgentAI;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
@CrossOrigin("*")
public class ChatController {

    private final AgentAI agentAI;

    public ChatController(AgentAI agentAI){
        this.agentAI = agentAI;
    }

    @GetMapping(value = "/chat", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<String> chat(@RequestParam(name = "message") String message){
        return agentAI.chat(message);
    }
}