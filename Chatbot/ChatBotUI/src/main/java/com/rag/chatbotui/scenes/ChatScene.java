package com.rag.chatbotui.scenes;

import javafx.animation.FadeTransition;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.shape.Circle;
import javafx.stage.FileChooser;
import javafx.scene.input.KeyCode;
import javafx.util.Duration;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.*;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.scene.shape.Rectangle;
import java.io.File;
import javafx.scene.Node;

public class ChatScene {
    private Scene scene;
    private VBox messageContainer;
    private static final String DARK_TEXT = "#ffffff";
    private static final String DARK_SECONDARY_TEXT = "#a3a3a3";
    private TextField messageField;
    private VBox sidebarContent;
    private ScrollPane scrollPane;
    private HBox loadingIndicator;
    private Map<String, List<ChatMessage>> conversations;
    private String currentConversationId;
    private VBox conversationsList;
    private boolean isDarkMode = false;
    private VBox userProfileSection;
    private static final String LIGHT_BG = "#ffffff";
    private static final String LIGHT_SECONDARY = "#f0f0f0";
    private static final String LIGHT_TEXT = "#000000";
    private static final String LIGHT_SECONDARY_TEXT = "#666666";
    private static final String DARK_BG = "#000000";
    private static final String DARK_SECONDARY = "#000000";

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

    public ChatScene() {
        conversations = new HashMap<>();
        currentConversationId = UUID.randomUUID().toString();
        conversations.put(currentConversationId, new ArrayList<>());

        SplitPane splitPane = new SplitPane();
        splitPane.setStyle("-fx-background-color: #000000;");

        VBox sidebar = createSidebar();
        BorderPane chatArea = new BorderPane();
        chatArea.setStyle("-fx-background-color: #000000;");
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
        welcomeBox.setStyle("-fx-background-color: #000000;");
        welcomeBox.getStyleClass().add("welcome-container");
        welcomeBox.setAlignment(Pos.CENTER);

        Label welcomeLabel = new Label("How can I help you?");
        welcomeLabel.getStyleClass().add("welcome-message");
        welcomeLabel.setStyle("-fx-text-fill: #ececf1;");

        welcomeBox.getChildren().add(welcomeLabel);
        messageContainer.getChildren().clear();
        messageContainer.setStyle("-fx-background-color: #000000;");
        messageContainer.getChildren().add(welcomeBox);
    }

    private HBox createLoadingIndicator() {
        HBox container = new HBox(5);
        container.setAlignment(Pos.CENTER_LEFT);
        container.setPadding(new Insets(10, 20, 10, 20));

        for (int i = 0; i < 3; i++) {
            Rectangle dot = new Rectangle(8, 8);
            dot.setArcHeight(8);
            dot.setArcWidth(8);
            dot.setFill(Color.web("#10a37f"));

            Timeline timeline = new Timeline(
                    new KeyFrame(Duration.ZERO, new KeyValue(dot.opacityProperty(), 1.0)),
                    new KeyFrame(Duration.seconds(0.5), new KeyValue(dot.opacityProperty(), 0.3)),
                    new KeyFrame(Duration.seconds(1.0), new KeyValue(dot.opacityProperty(), 1.0))
            );
            timeline.setDelay(Duration.seconds(i * 0.2));
            timeline.setCycleCount(Timeline.INDEFINITE);
            timeline.play();

            container.getChildren().add(dot);
        }

        return container;
    }

    private VBox createSidebar() {
        VBox sidebar = new VBox(20);
        sidebar.getStyleClass().add("sidebar");
        sidebar.setPadding(new Insets(20));
        sidebar.setPrefWidth(260);

        ImageView logo = new ImageView(new Image(getClass().getResourceAsStream("/images/enset.jpg")));
        logo.setFitWidth(180);
        logo.setPreserveRatio(true);

        Button newChatButton = new Button("New chat");
        newChatButton.getStyleClass().add("new-chat-button");
        newChatButton.setMaxWidth(Double.MAX_VALUE);
        newChatButton.setOnAction(e -> createNewConversation());

        conversationsList = new VBox(10);
        conversationsList.getStyleClass().add("conversations-list");
        ScrollPane conversationsScroll = new ScrollPane(conversationsList);
        conversationsScroll.setFitToWidth(true);
        conversationsScroll.getStyleClass().add("conversations-scroll");

        userProfileSection = createUserProfile();

        HBox themeToggle = createThemeToggle();

        VBox mainContent = new VBox(20);
        mainContent.getChildren().addAll(conversationsScroll);
        VBox.setVgrow(mainContent, Priority.ALWAYS);

        VBox footer = new VBox(10);
        footer.getChildren().addAll(new Separator(), userProfileSection, themeToggle);

        sidebar.getChildren().addAll(logo, newChatButton, new Separator(), mainContent, footer);
        return sidebar;
    }

    private VBox createUserProfile() {
        VBox profile = new VBox(10);
        profile.getStyleClass().add("user-profile");

        HBox userInfo = new HBox(12);
        userInfo.setAlignment(Pos.CENTER_LEFT);

        Circle avatar = new Circle(20);
        avatar.setFill(Color.web("#10a37f"));
        Label avatarLabel = new Label("U");
        avatarLabel.setTextFill(Color.WHITE);
        avatarLabel.setFont(Font.font("System", FontWeight.BOLD, 14));
        StackPane avatarPane = new StackPane(avatar, avatarLabel);

        VBox details = new VBox(2);
        Label nameLabel = new Label("User Name");
        Label emailLabel = new Label("user@enset.ma");
        nameLabel.getStyleClass().add("user-name");
        emailLabel.getStyleClass().add("user-email");
        details.getChildren().addAll(nameLabel, emailLabel);

        userInfo.getChildren().addAll(avatarPane, details);
        profile.getChildren().add(userInfo);

        return profile;
    }

    private HBox createThemeToggle() {
        HBox container = new HBox(10);
        container.setAlignment(Pos.CENTER_LEFT);
        container.getStyleClass().add("theme-toggle-container");

        ToggleButton themeToggle = new ToggleButton();
        themeToggle.getStyleClass().add("theme-toggle");
        themeToggle.setSelected(isDarkMode);

        ImageView lightIcon = new ImageView(new Image(getClass().getResourceAsStream("/images/Lighttheme.png")));
        ImageView darkIcon = new ImageView(new Image(getClass().getResourceAsStream("/images/Darktheme.png")));
        lightIcon.setFitWidth(20);
        lightIcon.setFitHeight(20);
        darkIcon.setFitWidth(20);
        darkIcon.setFitHeight(20);

        themeToggle.setGraphic(isDarkMode ? darkIcon : lightIcon);

        themeToggle.setOnAction(e -> {
            isDarkMode = themeToggle.isSelected();
            themeToggle.setGraphic(isDarkMode ? darkIcon : lightIcon);
            updateTheme();
        });

        container.getChildren().add(themeToggle);
        return container;
    }

    private void updateTheme() {
        if (scene != null) {
            URL lightCss = getClass().getResource("/styles/light-chat.css");
            URL darkCss = getClass().getResource("/styles/dark-chat.css");

            scene.getStylesheets().clear();
            if (isDarkMode && darkCss != null) {
                scene.getStylesheets().add(darkCss.toExternalForm());
                scene.getRoot().getStyleClass().add("dark-mode");
            } else if (lightCss != null) {
                scene.getStylesheets().add(lightCss.toExternalForm());
                scene.getRoot().getStyleClass().remove("dark-mode");
            }
        }
        String bgColor = isDarkMode ? DARK_BG : LIGHT_BG;
        String secondaryBg = isDarkMode ? DARK_SECONDARY : LIGHT_SECONDARY;
        String textColor = isDarkMode ? DARK_TEXT : LIGHT_TEXT;
        String secondaryText = isDarkMode ? DARK_SECONDARY_TEXT : LIGHT_SECONDARY_TEXT;

        scene.getRoot().setStyle("-fx-background-color: " + bgColor + ";");
        sidebarContent.setStyle("-fx-background-color: " + secondaryBg + ";");
        messageContainer.setStyle("-fx-background-color: " + bgColor + ";");

        for (Node node : messageContainer.getChildren()) {
            if (node instanceof HBox messageBox) {
                VBox messageContent = (VBox) messageBox.getChildren().stream()
                        .filter(n -> n instanceof VBox)
                        .findFirst()
                        .orElse(null);

                if (messageContent != null) {
                    for (Node child : messageContent.getChildren()) {
                        if (child instanceof Label) {
                            child.setStyle("-fx-text-fill: " + textColor + ";");
                        }
                    }
                }
            }
        }

        messageField.setStyle(
                "-fx-background-color: " + secondaryBg + ";" +
                        "-fx-text-fill: " + textColor + ";" +
                        "-fx-prompt-text-fill: " + secondaryText + ";"
        );

        if (scene.lookup(".header") instanceof HBox header) {
            header.setStyle("-fx-background-color: " + secondaryBg + ";");
            for (Node node : header.getChildren()) {
                if (node instanceof Label) {
                    node.setStyle("-fx-text-fill: " + textColor + ";");
                }
            }
        }

        if (!messageContainer.getChildren().isEmpty() &&
                messageContainer.getChildren().get(0) instanceof VBox welcomeBox) {
            welcomeBox.setStyle("-fx-background-color: " + bgColor + ";");
            for (Node node : welcomeBox.getChildren()) {
                if (node instanceof Label) {
                    node.setStyle("-fx-text-fill: " + textColor + ";");
                }
            }
        }

        userProfileSection.setStyle("-fx-background-color: " + secondaryBg + ";");
        for (Node node : userProfileSection.lookupAll(".user-name, .user-email")) {
            if (node instanceof Label) {
                node.setStyle("-fx-text-fill: " + textColor + ";");
            }
        }
    }

    private void createNewConversation() {
        String newId = UUID.randomUUID().toString();
        conversations.put(newId, new ArrayList<>());
        currentConversationId = newId;
        updateConversationsList();
        messageContainer.getChildren().clear();
        messageContainer.setStyle("-fx-background-color: #000000;");
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
        titleLabel.setFont(Font.font("Marcellus", FontWeight.BOLD, 30));
        titleLabel.setTextFill(Color.WHITE);

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

        showLoadingIndicator(true);
        new Thread(() -> {
            try {
                Thread.sleep(2000);
                Platform.runLater(() -> {
                    addMessage("I've received your document '" + file.getName() + "'. I can now answer questions about its content.", false);
                    showLoadingIndicator(false);
                });
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
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
                Thread.sleep(1500);
                String response = generateResponse(message);
                Platform.runLater(() -> {
                    addMessage(response, false);
                    showLoadingIndicator(false);
                });
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
    }

    private String generateResponse(String message) {
        message = message.toLowerCase();
        if (message.contains("hello") || message.contains("hi")) {
            return "Hello! How can I assist you today?";
        } else if (message.contains("java")) {
            return "Java is a popular object-oriented programming language. What would you like to know about it?";
        } else if (message.contains("javafx")) {
            return "JavaFX is a software platform for creating desktop applications. Do you have any specific questions about JavaFX?";
        } else if (message.contains("modern application development")) {
            return "Modern application development involves various technologies and practices. Is there a particular aspect you're interested in?";
        } else {
            return "I understand your question about '" + message + "'. Could you provide more details so I can better assist you?";
        }
    }

    private void showLoadingIndicator(boolean show) {
        Platform.runLater(() -> {
            loadingIndicator.setVisible(show);
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

        Circle avatar = new Circle(20);
        avatar.setFill(isUser ? Color.web("#10a37f") : Color.web("#19c37d"));
        Label avatarLabel = new Label(isUser ? "U" : "AI");
        avatarLabel.setTextFill(Color.WHITE);
        avatarLabel.setFont(Font.font("System", FontWeight.BOLD, 12));
        StackPane avatarPane = new StackPane(avatar, avatarLabel);

        VBox messageContent = new VBox(4);
        messageContent.getStyleClass().add("message-content");
        messageContent.setMaxWidth(800);

        Label messageLabel = new Label(message);
        messageLabel.setWrapText(true);
        messageLabel.getStyleClass().add(isUser ? "user-message" : "ai-message");

        Label timestampLabel = new Label(chatMessage.timestamp);
        timestampLabel.getStyleClass().add("timestamp");

        messageContent.getChildren().addAll(messageLabel, timestampLabel);

        if (isUser) {
            messageBox.getChildren().addAll(messageContent, avatarPane);
            messageBox.setAlignment(Pos.CENTER_RIGHT);
        } else {
            messageBox.getChildren().addAll(avatarPane, messageContent);
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

        Circle avatar = new Circle(20);
        avatar.setFill(isUser ? Color.web("#10a37f") : Color.web("#19c37d"));
        Label avatarLabel = new Label(isUser ? "U" : "AI");
        avatarLabel.setTextFill(Color.WHITE);
        avatarLabel.setFont(Font.font("System", FontWeight.BOLD, 12));
        StackPane avatarPane = new StackPane(avatar, avatarLabel);

        if (isUser) {
            messageBox.getChildren().addAll(content, avatarPane);
            messageBox.setAlignment(Pos.CENTER_RIGHT);
        } else {
            messageBox.getChildren().addAll(avatarPane, content);
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

