package com.rag.chatbotui;

import javafx.application.Application;
import javafx.application.Platform;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextClosedEvent;

public class Launcher {
    private static ConfigurableApplicationContext applicationContext;

    public static void main(String[] args) {
        try {
            System.setProperty("javafx.platform", "desktop");
            System.setProperty("spring.main.web-application-type", "none");
            System.setProperty("prism.lcdtext", "false");
            System.setProperty("prism.text", "t2k");


            Application.launch(JavaFxApplication.class, args);
        } catch (Exception e) {
            System.err.println("Failed to launch application: " + e.getMessage());
            e.printStackTrace();
            Platform.exit();
        }
    }

    public static ConfigurableApplicationContext initializeSpringContext(String[] args) {
        if (applicationContext == null) {
            try {

                SpringApplicationBuilder builder = new SpringApplicationBuilder(ChatBotUiApplication.class)
                        .headless(false)
                        .properties("spring.jmx.enabled=false")
                        .properties("spring.main.lazy-initialization=true");


                builder.listeners((ApplicationListener<ContextClosedEvent>) event -> {
                    Platform.runLater(() -> {
                        Platform.exit();
                    });
                });


                applicationContext = builder.run(args);

                Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                    if (applicationContext != null && applicationContext.isActive()) {
                        applicationContext.close();
                    }
                }));

            } catch (Exception e) {
                System.err.println("Failed to initialize Spring context: " + e.getMessage());
                e.printStackTrace();
                Platform.exit();
            }
        }
        return applicationContext;
    }


    public static ConfigurableApplicationContext getApplicationContext() {
        return applicationContext;
    }
}