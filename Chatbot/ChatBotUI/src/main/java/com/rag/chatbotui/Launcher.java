package com.rag.chatbotui;

import javafx.application.Application;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;

public class Launcher {
    public static void main(String[] args) {
        Application.launch(JavaFxApplication.class, args);
    }

    public static ConfigurableApplicationContext springContext;

    public static ConfigurableApplicationContext initializeSpringContext(String[] args) {
        return new SpringApplicationBuilder(ChatBotUiApplication.class).run(args);
    }
}
