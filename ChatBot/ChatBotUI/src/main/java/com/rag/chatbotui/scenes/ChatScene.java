package com.rag.chatbotui.scenes;

import com.rag.chatbotui.controllers.ChatController;
import javafx.animation.FadeTransition;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.scene.input.KeyCode;
import javafx.util.Duration;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import java.io.File;
import javafx.scene.Node;
import org.springframework.stereotype.Component;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.*;

@Component
public class ChatScene {
    private final ChatController chatController;
    private Scene scene;
    private VBox messageContainer;
    private TextField messageField;
    private ScrollPane scrollPane;
    private HBox loadingIndicator;
    private Map<String, List<ChatMessage>> conversations;
    private String currentConversationId;
    private VBox conversationsList;

    private static class ChatMessage {
        String content;
        boolean isUser;
        String timestamp;
        ChatMessage(String content, boolean isUser) {
            this.content = content;
            this.isUser = isUser;
            this.timestamp = new SimpleDateFormat("HH:mm").format(new Date());
        }
    }

    public ChatScene(ChatController chatController) {
        this.chatController = chatController;

        conversations = new HashMap<>();
        currentConversationId = UUID.randomUUID().toString();
        conversations.put(currentConversationId, new ArrayList<>());

        SplitPane splitPane = new SplitPane();
        splitPane.setStyle("-fx-background-color:#FFFFFF;");

        VBox sidebar = createSidebar();
        BorderPane chatArea = new BorderPane();
        chatArea.setStyle("-fx-background-color: #FFFFFF;");
        HBox.setHgrow(chatArea, Priority.ALWAYS);
        chatArea.getStyleClass().add("conversation-area");

        splitPane.getItems().addAll(sidebar, chatArea);
        splitPane.setDividerPositions(0.2);

        HBox header = createHeader();
        chatArea.setTop(header);

        messageContainer = new VBox(16);
        messageContainer.setPadding(new Insets(20));
        messageContainer.setFillWidth(true);
        VBox.setVgrow(messageContainer, Priority.ALWAYS);
        messageContainer.getStyleClass().add("message-container");

        scrollPane = new ScrollPane(messageContainer);
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background-color: transparent;");

        loadingIndicator = createLoadingIndicator();
        loadingIndicator.setVisible(false);

        VBox centerContainer = new VBox(10);
        centerContainer.getChildren().addAll(scrollPane, loadingIndicator);
        chatArea.setCenter(centerContainer);

        VBox bottomArea = new VBox(10);
        bottomArea.setPadding(new Insets(20));
        HBox inputArea = createInputArea();
        bottomArea.getChildren().add(inputArea);
        chatArea.setBottom(bottomArea);

        scene = new Scene(splitPane, 1200, 800);

        URL lightCssResource = getClass().getResource("/styles/light-chat.css");
        if (lightCssResource != null) {
            scene.getStylesheets().add(lightCssResource.toExternalForm());
        } else {
            System.err.println("Warning: Could not load CSS file: /styles/light-chat.css");
        }

        splitPane.prefWidthProperty().bind(scene.widthProperty());
        splitPane.prefHeightProperty().bind(scene.heightProperty());

        showWelcomeMessage();
    }

    private void showWelcomeMessage() {
        VBox welcomeBox = new VBox(20);
        welcomeBox.setStyle("-fx-background-color: #FFFFFF;");
        welcomeBox.getStyleClass().add("welcome-container");
        welcomeBox.setAlignment(Pos.CENTER);

        Label welcomeLabel = new Label("How can I assist you today?");
        welcomeLabel.getStyleClass().add("welcome-message");
        welcomeLabel.setStyle("-fx-text-fill: #000000;");

        welcomeBox.getChildren().add(welcomeLabel);
        messageContainer.setStyle("-fx-background-color: #FFFFFF;");
        messageContainer.getChildren().add(welcomeBox);
    }

    private VBox createSidebar() {
        VBox sidebar = new VBox(20);
        sidebar.getStyleClass().add("sidebar");
        sidebar.setPadding(new Insets(30));
        sidebar.setPrefWidth(280);

        ImageView logo = new ImageView(new Image(getClass().getResourceAsStream("/images/logo.png")));
        logo.setFitWidth(160);
        logo.setPreserveRatio(true);
        logo.setSmooth(true);

        Button newChatButton = new Button("New chat");
        newChatButton.getStyleClass().add("new-chat-button");
        newChatButton.setMaxWidth(Double.MAX_VALUE);
        newChatButton.setStyle(
                "-fx-background-color: #ADD8E6;" +
                        "-fx-text-fill: black;" +
                        "-fx-font-size: 14px;" +
                        "-fx-padding: 12px 16px;" +
                        "-fx-background-radius: 8px;" +
                        "-fx-cursor: hand;" +
                        "-fx-min-width: 200px;"
        );
        newChatButton.setOnAction(e -> createNewConversation());

        conversationsList = new VBox(10);
        conversationsList.getStyleClass().add("conversations-list");
        ScrollPane conversationsScroll = new ScrollPane(conversationsList);
        conversationsScroll.setFitToWidth(true);
        conversationsScroll.getStyleClass().add("conversations-scroll");

        VBox mainContent = new VBox(20);
        mainContent.getChildren().addAll(conversationsScroll);
        VBox.setVgrow(mainContent, Priority.ALWAYS);

        sidebar.getChildren().addAll(logo, newChatButton, new Separator(), mainContent);
        return sidebar;
    }

    private void createNewConversation() {
        String newId = UUID.randomUUID().toString();
        conversations.put(newId, new ArrayList<>());
        currentConversationId = newId;
        updateConversationsList();
        messageContainer.getChildren().clear();
        showWelcomeMessage();
    }

    private void updateConversationsList() {
        conversationsList.getChildren().clear();
        conversations.forEach((id, messages) -> {
            String title = messages.isEmpty() ? "New chat" :
                    messages.get(0).content.length() > 30 ?
                            messages.get(0).content.substring(0, 30) + "..." :
                            messages.get(0).content;

            Button convButton = new Button(title);
            convButton.getStyleClass().add(id.equals(currentConversationId) ?
                    "conversation-button-active" :
                    "conversation-button");
            convButton.setMaxWidth(Double.MAX_VALUE);
            convButton.setOnAction(e -> loadConversation(id));

            conversationsList.getChildren().add(convButton);
        });
    }

    private void loadConversation(String id) {
        currentConversationId = id;
        messageContainer.getChildren().clear();
        List<ChatMessage> messages = conversations.get(id);
        if (messages.isEmpty()) {
            showWelcomeMessage();
        } else {
            messages.forEach(msg ->
                    addMessage(msg.content, msg.isUser, false)
            );
        }
        updateConversationsList();
    }

    private HBox createHeader() {
        HBox header = new HBox();
        header.setAlignment(Pos.CENTER_LEFT);
        header.setPadding(new Insets(15));
        header.getStyleClass().add("header");

        Label titleLabel = new Label("ChatEnset");
        titleLabel.setFont(Font.font("Marcellus", FontWeight.EXTRA_BOLD, 30));
        titleLabel.setTextFill(Color.BLACK);

        header.getChildren().add(titleLabel);
        return header;
    }

    private HBox createInputArea() {
        HBox inputArea = new HBox(10);
        inputArea.setAlignment(Pos.CENTER);
        inputArea.setPadding(new Insets(15));
        inputArea.getStyleClass().add("input-area");

        Button attachButton = new Button("");
        ImageView attachIcon = new ImageView(new Image(getClass().getResourceAsStream("/images/attachicon.png")));
        attachIcon.setFitWidth(16);
        attachIcon.setFitHeight(16);
        attachButton.setGraphic(attachIcon);
        attachButton.getStyleClass().add("attach-button");

        attachButton.setOnAction(e -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.getExtensionFilters().add(
                    new FileChooser.ExtensionFilter("PDF Files", "*.pdf")
            );
            File selectedFile = fileChooser.showOpenDialog(null);
            if (selectedFile != null) {
                handleFileUpload(selectedFile);
            }
        });

        messageField = new TextField();
        messageField.setPromptText("Message Chat...");
        HBox.setHgrow(messageField, Priority.ALWAYS);
        messageField.getStyleClass().add("message-field");

        messageField.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER && !event.isShiftDown()) {
                sendMessage();
                event.consume();
            }
        });

        Button sendButton = new Button("");
        ImageView sendIcon = new ImageView(new Image(getClass().getResourceAsStream("/images/send.png")));
        sendIcon.setFitWidth(16);
        sendIcon.setFitHeight(16);
        sendButton.setGraphic(sendIcon);
        sendButton.getStyleClass().add("send-button");
        sendButton.setOnAction(e -> sendMessage());

        inputArea.getChildren().addAll(attachButton, messageField, sendButton);
        return inputArea;
    }

    private void handleFileUpload(File file) {
        HBox attachmentBox = new HBox(10);
        attachmentBox.getStyleClass().add("file-attachment");

        ImageView fileIcon = new ImageView(new Image(getClass().getResourceAsStream("/images/pdf-icon.png")));
        fileIcon.setFitWidth(24);
        fileIcon.setFitHeight(24);

        VBox fileInfo = new VBox(2);
        Label fileName = new Label(file.getName());
        Label fileSize = new Label(formatFileSize(file.length()));
        fileName.getStyleClass().add("file-name");
        fileSize.getStyleClass().add("file-size");
        fileInfo.getChildren().addAll(fileName, fileSize);

        attachmentBox.getChildren().addAll(fileIcon, fileInfo);

        addMessage(attachmentBox, true);

        // TODO: Implement file upload handling with ChatController
    }

    private String formatFileSize(long bytes) {
        final String[] units = new String[] { "B", "KB", "MB", "GB" };
        int unitIndex = 0;
        double size = bytes;

        while (size > 1024 && unitIndex < units.length - 1) {
            size /= 1024;
            unitIndex++;
        }

        return String.format("%.1f %s", size, units[unitIndex]);
    }

    private void sendMessage() {
        String message = messageField.getText().trim();
        if (!message.isEmpty()) {
            if (messageContainer.getChildren().size() == 1 &&
                    messageContainer.getChildren().get(0) instanceof VBox) {
                messageContainer.getChildren().clear();
            }
            addMessage(message, true);
            messageField.clear();
            processUserMessage(message);
        }
    }

    private void processUserMessage(String message) {
        showLoadingIndicator(true);

        new Thread(() -> {
            try {
                String response = chatController.sendMessage(message);
                Platform.runLater(() -> {
                    showLoadingIndicator(false);
                    addMessage(response, false);
                });
            } catch (Exception e) {
                e.printStackTrace();
                Platform.runLater(() -> {
                    showLoadingIndicator(false);
                    addMessage("Error: " + e.getMessage(), false);
                });
            }
        }).start();
    }

    private HBox createLoadingIndicator() {
        HBox container = new HBox(10);
        container.setAlignment(Pos.CENTER_LEFT);
        container.setPadding(new Insets(20));
        container.setStyle("-fx-background-color: #ADD8E6; -fx-background-radius: 8px;");

        Label processingLabel = new Label("ChatEnset is processing...");
        processingLabel.setStyle("-fx-text-fill:#000000 ; -fx-font-size: 14px;");

        VBox dotsContainer = new VBox();
        dotsContainer.setAlignment(Pos.CENTER_LEFT);
        dotsContainer.setPadding(new Insets(0, 0, 0, 10));

        HBox dots = new HBox(8);
        dots.setAlignment(Pos.CENTER_LEFT);

        for (int i = 0; i < 3; i++) {
            Circle dot = new Circle(6);
            dot.setFill(Color.web("blue"));

            Timeline timeline = new Timeline(
                    new KeyFrame(Duration.ZERO,
                            new KeyValue(dot.scaleXProperty(), 1.0),
                            new KeyValue(dot.scaleYProperty(), 1.0),
                            new KeyValue(dot.opacityProperty(), 1.0)
                    ),
                    new KeyFrame(Duration.seconds(0.5),
                            new KeyValue(dot.scaleXProperty(), 1.5),
                            new KeyValue(dot.scaleYProperty(), 1.5),
                            new KeyValue(dot.opacityProperty(), 0.3)
                    ),
                    new KeyFrame(Duration.seconds(1.0),
                            new KeyValue(dot.scaleXProperty(), 1.0),
                            new KeyValue(dot.scaleYProperty(), 1.0),
                            new KeyValue(dot.opacityProperty(), 1.0)
                    )
            );

            timeline.setDelay(Duration.seconds(i * 0.2));
            timeline.setCycleCount(Timeline.INDEFINITE);
            timeline.play();

            dots.getChildren().add(dot);
        }

        dotsContainer.getChildren().add(dots);
        container.getChildren().addAll(processingLabel, dotsContainer);

        return container;
    }

    private void showLoadingIndicator(boolean show) {
        Platform.runLater(() -> {
            if (show) {
                loadingIndicator.setOpacity(0);
                loadingIndicator.setVisible(true);
                FadeTransition ft = new FadeTransition(Duration.millis(200), loadingIndicator);
                ft.setFromValue(0);
                ft.setToValue(1);
                ft.play();
            } else {
                FadeTransition ft = new FadeTransition(Duration.millis(200), loadingIndicator);
                ft.setFromValue(1);
                ft.setToValue(0);
                ft.setOnFinished(e -> loadingIndicator.setVisible(false));
                ft.play();
            }
        });
    }


    public void addMessage(String message, boolean isUser) {
        addMessage(message, isUser, true);
    }

    public void addMessage(String message, boolean isUser, boolean saveToConversation) {
        ChatMessage chatMessage = new ChatMessage(message, isUser);
        if (saveToConversation) {
            conversations.get(currentConversationId).add(chatMessage);
            updateConversationsList();
        }

        HBox messageBox = new HBox(12);
        messageBox.setMaxWidth(Double.MAX_VALUE);
        HBox.setHgrow(messageBox, Priority.ALWAYS);
        messageBox.getStyleClass().add("message-box");
        messageBox.setPadding(new Insets(8, 15, 8, 15));

        VBox messageContent = new VBox(4);
        messageContent.getStyleClass().add("message-content");
        messageContent.setMaxWidth(800);

        Label messageLabel = new Label(message);
        messageLabel.setWrapText(true);
        messageLabel.getStyleClass().add(isUser ? "user-message" : "ai-message");
        messageLabel.setStyle(
                "-fx-background-color: " + (isUser ? "#ADD8E6" : "white") + ";" +
                        "-fx-text-fill: " + (isUser ? "black" : "#353740") + ";" +
                        "-fx-padding: 12px 16px;" +
                        "-fx-background-radius: 12px;" +
                        "-fx-font-size: 14px;" +
                        "-fx-max-width: 800px;" +
                        "-fx-wrap-text: true;"
        );
        if (!isUser) {
            messageLabel.setStyle(messageLabel.getStyle() +
                    "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 10, 0, 0, 1);"
            );
        }

        Label timestampLabel = new Label(chatMessage.timestamp);
        timestampLabel.getStyleClass().add("timestamp");
        timestampLabel.setStyle("-fx-text-fill: white; -fx-font-size: 11px;");

        messageContent.getChildren().addAll(messageLabel, timestampLabel);

        if (isUser) {
            messageBox.getChildren().add(messageContent);
            messageBox.setAlignment(Pos.CENTER_RIGHT);
        } else {
            messageBox.getChildren().add(messageContent);
            messageBox.setAlignment(Pos.CENTER_LEFT);
        }

        Platform.runLater(() -> {
            messageContainer.getChildren().add(messageBox);
            scrollPane.setVvalue(1.0);

            FadeTransition ft = new FadeTransition(Duration.millis(200), messageBox);
            ft.setFromValue(0.0);
            ft.setToValue(1.0);
            ft.play();
        });
    }

    public void addMessage(Node content, boolean isUser) {
        HBox messageBox = new HBox(12);
        messageBox.setMaxWidth(Double.MAX_VALUE);
        HBox.setHgrow(messageBox, Priority.ALWAYS);
        messageBox.getStyleClass().add("message-box");
        messageBox.setPadding(new Insets(8, 15, 8, 15));

        if (isUser) {
            messageBox.getChildren().add(content);
            messageBox.setAlignment(Pos.CENTER_RIGHT);
        } else {
            messageBox.getChildren().add(content);
            messageBox.setAlignment(Pos.CENTER_LEFT);
        }

        Platform.runLater(() -> {
            messageContainer.getChildren().add(messageBox);
            scrollPane.setVvalue(1.0);

            FadeTransition ft = new FadeTransition(Duration.millis(200), messageBox);
            ft.setFromValue(0.0);
            ft.setToValue(1.0);
            ft.play();
        });
    }

    public Scene getScene() {
        return scene;
    }
}











