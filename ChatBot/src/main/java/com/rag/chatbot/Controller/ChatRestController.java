package com.rag.chatbot.Controller;

import com.rag.chatbot.Service.ChatAiService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/chat")
public class ChatRestController {
    private ChatAiService chatAiService;

    public ChatRestController(ChatAiService chatAiService) {
        this.chatAiService=chatAiService;
    }
    @GetMapping("/ask")
    public String ask(String request) {
        System.out.println(request);
        return chatAiService.ragChat(request);
    }

}
