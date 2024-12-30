package com.rag.chatbotui;

import com.rag.chatbotui.scenes.AdminLoginScene;
import com.rag.chatbotui.scenes.ChatScene;
import com.rag.chatbotui.scenes.LoginScene;
import com.rag.chatbotui.scenes.RegisterScene;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Component;

@Component
public class JavaFxApplication extends Application {
    private static ConfigurableApplicationContext applicationContext;
    private static Stage primaryStage;
    private static volatile boolean isInitialized = false;

    public JavaFxApplication() {
    }

    @Override
    public void init() {
        if (applicationContext == null) {
            applicationContext = Launcher.initializeSpringContext(getParameters().getRaw().toArray(new String[0]));
        }
    }

    public static void setApplicationContext(ConfigurableApplicationContext context) {
        applicationContext = context;
    }

    @Override
    public void start(Stage stage) {
        primaryStage = stage;
        isInitialized = true;
        System.out.println("JavaFxApplication started, primaryStage initialized.");
        Platform.runLater(() -> {
            try {
                showLoginScene();
            } catch (Exception e) {
                System.err.println("Error showing login scene: " + e.getMessage());
                e.printStackTrace();
            }
        });
    }

    @Override
    public void stop() {
        if (applicationContext != null) {
            applicationContext.close();
        }
        Platform.exit();
    }

    // Scene management methods
    public void showLoginScene() {
        ensureInitialized();
        Platform.runLater(() -> {
            try {
                LoginScene loginScene = applicationContext.getBean(LoginScene.class);
                setScene(loginScene.getScene(), "Login", 600, 400);
            } catch (Exception e) {
                handleSceneError("login", e);
            }
        });
    }

    public void showRegisterScene() {
        ensureInitialized();
        Platform.runLater(() -> {
            try {
                RegisterScene registerScene = applicationContext.getBean(RegisterScene.class);
                setScene(registerScene.getScene(), "Register", 600, 400);
            } catch (Exception e) {
                handleSceneError("register", e);
            }
        });
    }

    public void showChatScene() {
        ensureInitialized();
        Platform.runLater(() -> {
            try {
                ChatScene chatScene = applicationContext.getBean(ChatScene.class);
                setScene(chatScene.getScene(), "Chat", 1200, 800);
            } catch (Exception e) {
                handleSceneError("chat", e);
            }
        });
    }

    public void showLoginAdminScene() {
        ensureInitialized(); // Ensure that any necessary initialization is done
        Platform.runLater(() -> {
            try {
                // Retrieve the AdminLoginScene bean from the application context
                AdminLoginScene adminLoginScene = applicationContext.getBean(AdminLoginScene.class);

                // Set the scene in the Stage (replace 'setScene' with your method for setting the scene)
                setScene(adminLoginScene.getScene(), "Admin Login", 600, 400);
            } catch (Exception e) {
                // Handle any error that might occur when retrieving or setting the scene
                handleSceneError("admin login", e);
            }
        });
    }


    private void setScene(Scene scene, String title, int width, int height) {
        ensureInitialized();
        if (scene == null) {
            throw new IllegalArgumentException("Scene cannot be null");
        }

        primaryStage.setTitle(title);
        primaryStage.setScene(scene);
        primaryStage.setWidth(width);
        primaryStage.setHeight(height);
        primaryStage.centerOnScreen();

        if (!primaryStage.isShowing()) {
            primaryStage.show();
        }
    }

    private void ensureInitialized() {
        if (!isInitialized || primaryStage == null) {
            throw new IllegalStateException("JavaFX Application is not properly initialized");
        }
    }

    private void handleSceneError(String sceneName, Exception e) {
        System.err.println("Error switching to " + sceneName + " scene: " + e.getMessage());
        e.printStackTrace();
        // You could add additional error handling here, such as showing an error dialog
    }

    public static ConfigurableApplicationContext getApplicationContext() {
        return applicationContext;
    }

    public static Stage getPrimaryStage() {
        return primaryStage;
    }
}










