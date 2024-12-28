package com.rag.chatbotui.scenes;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import org.springframework.stereotype.Component;
import javafx.concurrent.Task;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

@Component
public class PdfUploadScene {
    private VBox filePreviewContainer;
    private ProgressIndicator progressIndicator;
    private Label statusLabel;
    private Button uploadButton;
    private ScrollPane previewScrollPane;
    private AtomicReference<File> currentFile = new AtomicReference<>();
    private TabPane adminToolsTabPane;

    public Scene createScene(String token) {
        BorderPane mainLayout = new BorderPane();
        mainLayout.setStyle("-fx-background-color: #ADD8E6;");


        HBox header = createHeader();
        mainLayout.setTop(header);


        VBox sidebar = createAdminSidebar();
        mainLayout.setLeft(sidebar);


        TabPane centerContent = createCenterContent();
        mainLayout.setCenter(centerContent);

        Scene scene = new Scene(mainLayout, 1024, 768);
        scene.getStylesheets().add(getClass().getResource("/styles/pdfupload-style.css").toExternalForm());

        uploadButton.setOnAction(e -> handleFileUpload(token));

        return scene;
    }

    private HBox createHeader() {
        HBox header = new HBox();
        header.setAlignment(Pos.CENTER_LEFT);
        header.setPadding(new Insets(15));
        header.setStyle("-fx-background-color: #ADD8E6;");

        Label titleLabel = new Label("Admin Dashboard");
        titleLabel.setFont(Font.font("Roboto", FontWeight.EXTRA_BOLD, 30));
        titleLabel.setTextFill(Color.WHITE);

        Label adminLabel = new Label(" (Admin Mode)");
        adminLabel.setFont(Font.font("Roboto", FontWeight.NORMAL, 16));
        adminLabel.setTextFill(Color.LIGHTGRAY);

        header.getChildren().addAll(titleLabel, adminLabel);
        return header;
    }

    private VBox createAdminSidebar() {
        VBox sidebar = new VBox(15);
        sidebar.setPadding(new Insets(20, 10, 20, 10));
        sidebar.setPrefWidth(220);
        sidebar.setStyle("-fx-background-color: #ADD8E6;");

        Button usersButton = new Button("Manage Users");
        Button logsButton = new Button("View Logs");
        Button settingsButton = new Button("Settings");
        Button analyticsButton = new Button("Analytics");


        String buttonStyle = "-fx-background-color: #4682B4; -fx-text-fill: white; -fx-font-size: 14px; -fx-min-width: 180;";
        usersButton.setStyle(buttonStyle);
        logsButton.setStyle(buttonStyle);
        settingsButton.setStyle(buttonStyle);
        analyticsButton.setStyle(buttonStyle);

        addHoverEffect(usersButton);
        addHoverEffect(logsButton);
        addHoverEffect(settingsButton);
        addHoverEffect(analyticsButton);


        usersButton.setOnAction(e -> showUserManagement());
        logsButton.setOnAction(e -> showSystemLogs());
        settingsButton.setOnAction(e -> showSettings());
        analyticsButton.setOnAction(e -> showAnalytics());

        sidebar.getChildren().addAll(
                new Label("Admin Tools") {{ setTextFill(Color.WHITE); setFont(Font.font("Roboto", FontWeight.BOLD, 18)); }},
                usersButton,
                logsButton,
                settingsButton,
                analyticsButton
        );

        return sidebar;
    }

    private void addHoverEffect(Button button) {
        String buttonStyle = "-fx-background-color: #4682B4; -fx-text-fill: white; -fx-font-size: 14px; -fx-min-width: 180;";
        button.setOnMouseEntered(e -> button.setStyle("-fx-background-color:#5F9EA0 ; -fx-text-fill: white;"));
        button.setOnMouseExited(e -> button.setStyle("-fx-background-color: #4682B4; -fx-text-fill: white;"));
    }

    private TabPane createCenterContent() {
        TabPane tabPane = new TabPane();
        tabPane.setStyle("-fx-background-color: #FFFFFF;");


        Tab pdfTab = new Tab("PDF Upload");
        pdfTab.setContent(createPdfUploadContent());
        pdfTab.setClosable(false);


        Tab statusTab = new Tab("System Status");
        statusTab.setContent(createSystemStatusContent());
        statusTab.setClosable(false);

        tabPane.getTabs().addAll(pdfTab, statusTab);
        return tabPane;
    }

    private VBox createPdfUploadContent() {
        VBox content = new VBox(25);
        content.setAlignment(Pos.TOP_CENTER);
        content.setPadding(new Insets(20));
        content.setStyle("-fx-background-color: #F4F4F9;");

        VBox uploadSection = createUploadSection();
        VBox previewSection = createPreviewSection();

        content.getChildren().addAll(uploadSection, previewSection);
        return content;
    }

    private VBox createSystemStatusContent() {
        VBox statusContent = new VBox(20);
        statusContent.setPadding(new Insets(20));
        statusContent.setStyle("-fx-background-color: #F4F4F9;");


        Label metricsTitle = new Label("System Metrics");
        metricsTitle.setFont(Font.font("Roboto", FontWeight.BOLD, 18));

        GridPane metrics = new GridPane();
        metrics.setHgap(15);
        metrics.setVgap(10);
        metrics.addRow(0, new Label("CPU Usage:"), new Label("45%"));
        metrics.addRow(1, new Label("Memory Usage:"), new Label("2.1 GB / 4 GB"));
        metrics.addRow(2, new Label("Storage:"), new Label("156 GB free"));
        metrics.addRow(3, new Label("Active Users:"), new Label("23"));

        statusContent.getChildren().addAll(metricsTitle, metrics);
        return statusContent;
    }

    private VBox createUploadSection() {
        VBox uploadSection = new VBox(20);
        uploadSection.setAlignment(Pos.CENTER);
        uploadSection.getStyleClass().add("upload-section");

        uploadButton = new Button("Choose PDF File");
        uploadButton.getStyleClass().add("upload-button");

        progressIndicator = new ProgressIndicator();
        progressIndicator.setVisible(false);
        progressIndicator.setPrefSize(40, 40);
        progressIndicator.getStyleClass().add("progress-indicator");

        statusLabel = new Label("");
        statusLabel.getStyleClass().add("status-label");

        uploadSection.getChildren().addAll(uploadButton, progressIndicator, statusLabel);
        return uploadSection;
    }

    private VBox createPreviewSection() {
        VBox previewSection = new VBox(15);
        previewSection.getStyleClass().add("preview-section");

        Label previewLabel = new Label("File Preview");
        previewLabel.setFont(Font.font("Roboto", FontWeight.BOLD, 18));
        previewLabel.setTextFill(Color.BLACK);

        filePreviewContainer = new VBox(10);
        filePreviewContainer.setAlignment(Pos.CENTER);
        filePreviewContainer.getStyleClass().add("file-preview-container");

        previewScrollPane = new ScrollPane(filePreviewContainer);
        previewScrollPane.setFitToWidth(true);
        previewScrollPane.setPrefViewportHeight(400);
        previewScrollPane.getStyleClass().add("preview-scroll-pane");

        previewSection.getChildren().addAll(previewLabel, previewScrollPane);
        return previewSection;
    }

    private void showUserManagement() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("User Management");
        alert.setHeaderText("User Management System");
        alert.setContentText("User management functionality will be implemented here.");
        alert.show();
    }

    private void showSystemLogs() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("System Logs");
        alert.setHeaderText("System Logs Viewer");
        alert.setContentText("System logs viewer will be implemented here.");
        alert.show();
    }

    private void showSettings() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Settings");
        alert.setHeaderText("System Settings");
        alert.setContentText("Settings panel will be implemented here.");
        alert.show();
    }

    private void showAnalytics() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Analytics");
        alert.setHeaderText("System Analytics");
        alert.setContentText("Analytics dashboard will be implemented here.");
        alert.show();
    }

    private void handleFileUpload(String token) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choose PDF to Upload");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PDF Files", "*.pdf"));

        File selectedFile = fileChooser.showOpenDialog(null);
        if (selectedFile != null) {
            currentFile.set(selectedFile);
            updateFilePreview(selectedFile);
            uploadFile(selectedFile, token);
        }
    }

    private void updateFilePreview(File file) {
        filePreviewContainer.getChildren().clear();

        VBox fileInfo = new VBox(5);
        fileInfo.getStyleClass().add("file-info");

        Label nameLabel = new Label("File: " + file.getName());
        Label sizeLabel = new Label("Size: " + formatFileSize(file.length()));

        fileInfo.getChildren().addAll(nameLabel, sizeLabel);
        filePreviewContainer.getChildren().add(fileInfo);
    }

    private String formatFileSize(long bytes) {
        if (bytes < 1024) return bytes + " B";
        int exp = (int) (Math.log(bytes) / Math.log(1024));
        String pre = "KMGTPE".charAt(exp-1) + "";
        return String.format("%.1f %sB", bytes / Math.pow(1024, exp), pre);
    }

    private void uploadFile(File file, String token) {
        // Keep existing implementation...
        Task<Void> uploadTask = new Task<>() {
            @Override
            protected Void call() throws Exception {
                uploadButton.setDisable(true);
                progressIndicator.setVisible(true);
                statusLabel.setText("Uploading...");

                try {
                    URL url = new URL("http://localhost:9090/api/vector-store/upload-pdf");
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();

                    connection.setRequestMethod("POST");
                    connection.setRequestProperty("Content-Type", "multipart/form-data; boundary=boundary");
                    connection.setDoOutput(true);

                    String boundary = "boundary";
                    String tokenHeader = "--" + boundary + "\r\nContent-Disposition: form-data; name=\"token\"\r\n\r\n" + token + "\r\n";
                    String fileHeader = "--" + boundary + "\r\nContent-Disposition: form-data; name=\"file\"; filename=\"" + file.getName() + "\"\r\nContent-Type: application/pdf\r\n\r\n";
                    String fileFooter = "\r\n--" + boundary + "--\r\n";

                    connection.getOutputStream().write(tokenHeader.getBytes());
                    connection.getOutputStream().write(fileHeader.getBytes());
                    connection.getOutputStream().write(Files.readAllBytes(file.toPath()));
                    connection.getOutputStream().write(fileFooter.getBytes());

                    int responseCode = connection.getResponseCode();
                    if (responseCode == HttpURLConnection.HTTP_OK) {
                        updateStatus("Upload successful!", true);
                    } else {
                        updateStatus("Upload failed: " + connection.getResponseMessage(), false);
                    }
                } catch (IOException e) {
                    updateStatus("Upload failed: " + e.getMessage(), false);
                }

                return null;
            }
        };

        uploadTask.setOnSucceeded(e -> {
            uploadButton.setDisable(false);
            progressIndicator.setVisible(false);
        });

        uploadTask.setOnFailed(e -> {
            uploadButton.setDisable(false);
            progressIndicator.setVisible(false);
            statusLabel.setText("Upload failed: " + e.getSource().getException().getMessage());
            statusLabel.setTextFill(Color.RED);
        });

        new Thread(uploadTask).start();
    }

    private void updateStatus(String message, boolean success) {
        javafx.application.Platform.runLater(() -> {
            statusLabel.setText(message);
            statusLabel.setTextFill(success ? Color.GREEN : Color.RED);
        });
    }
}




