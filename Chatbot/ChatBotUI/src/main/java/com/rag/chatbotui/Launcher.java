package com.rag.chatbotui;

import javafx.application.Application;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;

public class Launcher {
    public static ConfigurableApplicationContext springContext;

    public static void main(String[] args) {
        // Set a system property to make Spring not start up a web server
        System.setProperty("javafx.platform", "desktop");
        System.setProperty("spring.main.web-application-type", "none");

        // Launch the JavaFX application
        Application.launch(JavaFxApplication.class, args);
    }

    public static ConfigurableApplicationContext initializeSpringContext(String[] args) {
        // Start the Spring application context with headless mode disabled
        return new SpringApplicationBuilder(ChatBotUiApplication.class)
                .headless(false)
                .run(args);
    }
}