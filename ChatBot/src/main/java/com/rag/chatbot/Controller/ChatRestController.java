package com.rag.chatbot.Controller;

import com.rag.chatbot.Service.ChatAiService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth/chat")
@CrossOrigin(origins = "*")
public class ChatRestController {
    private final ChatAiService chatAiService;

    public ChatRestController(ChatAiService chatAiService) {
        this.chatAiService = chatAiService;
        System.out.println("ChatRestController initialized");
    }

    @GetMapping("/ask")
    public ResponseEntity<String> ask(@RequestParam("request") String request) {
        try {
            System.out.println("Received chat request: " + request);
            String response = chatAiService.ragChat(request);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }
}