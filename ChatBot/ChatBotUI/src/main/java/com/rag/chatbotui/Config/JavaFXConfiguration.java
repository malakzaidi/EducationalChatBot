package com.rag.chatbotui.Config;

import com.rag.chatbotui.scenes.ChatScene;
import javafx.application.Platform;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicReference;
@Configuration
public class JavaFXConfiguration {

    @Bean
    public ChatScene chatScene() {
        // Ensure we're on JavaFX thread
        if (!Platform.isFxApplicationThread()) {
            try {
                AtomicReference<ChatScene> scene = new AtomicReference<>();
                CountDownLatch latch = new CountDownLatch(1);

                Platform.runLater(() -> {
                    scene.set(new ChatScene());
                    latch.countDown();
                });

                latch.await();
                return scene.get();
            } catch (InterruptedException e) {
                throw new RuntimeException("Failed to create ChatScene", e);
            }
        }
        return new ChatScene();
    }
}