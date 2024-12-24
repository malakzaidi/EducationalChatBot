package com.rag.chatbotui;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = "com.rag.chatbotui")
public class ChatBotUiApplication {
    public static void main(String[] args) {
        // Start the application through the Launcher
        Launcher.main(args);
    }
}
