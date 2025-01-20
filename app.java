import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Modality;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.Node;

public class app extends Application {
    private TextField inputField;
    private ComboBox<String> fromBase;
    private ComboBox<String> toBase;
    private Label resultLabel;
    private TextArea conversionHistoryArea;
    private Stage historyStage;
    private boolean isDarkMode = false;
    private VBox root;
    private Scene scene;

    private static final String LIGHT_BG = "#f5f5f5";
    private static final String LIGHT_CARD_BG = "white";
    private static final String LIGHT_TEXT = "#2c3e50";
    private static final String LIGHT_SECONDARY = "#7f8c8d";
    private static final String LIGHT_INPUT_BG = "#f8f9fa";

    private static final String DARK_BG = "#1a1a1a";
    private static final String DARK_CARD_BG = "#2d2d2d";
    private static final String DARK_TEXT = "#ecf0f1";
    private static final String DARK_SECONDARY = "#bdc3c7";
    private static final String DARK_INPUT_BG = "#3d3d3d";

    @Override
    public void start(Stage primaryStage) {
        // Main container
        root = new VBox(20);
        updateTheme();
        root.setPadding(new Insets(20));

        // Header section with theme toggle
        HBox headerContainer = new HBox(20);
        headerContainer.setAlignment(Pos.CENTER);

        VBox headerBox = createHeader();
        Button themeToggle = createThemeToggleButton();

        headerContainer.getChildren().addAll(headerBox, themeToggle);

        // Main content section
        VBox contentBox = createMainContent();

        // Add all sections to root
        root.getChildren().addAll(headerContainer, contentBox);

        // Initialize history window
        createHistoryWindow();

        scene = new Scene(root, 500, 500);
        primaryStage.setTitle("Advanced Number System Converter");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private VBox createHeader() {
        VBox headerBox = new VBox(10);
        headerBox.setAlignment(Pos.CENTER);

        Label titleLabel = new Label("Number System Converter");
        titleLabel.setFont(Font.font("Segoe UI", FontWeight.BOLD, 24));
        titleLabel.setStyle("-fx-text-fill: #2c3e50;");

        Label subtitleLabel = new Label("Convert between Binary, Decimal, Hexadecimal, and Octal");
        subtitleLabel.setStyle("-fx-text-fill: #7f8c8d;");

        headerBox.getChildren().addAll(titleLabel, subtitleLabel);
        return headerBox;
    }

    private VBox createMainContent() {
        VBox contentBox = new VBox(15);
        contentBox.setStyle("-fx-background-color: white; -fx-padding: 20; " +
                "-fx-background-radius: 10; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 10, 0, 0, 2);");

        // Input section
        inputField = new TextField();
        inputField.setPromptText("Enter number");
        inputField.setStyle("-fx-padding: 10; -fx-background-radius: 5;");
        styleInput(inputField);

        // Conversion controls
        HBox conversionBox = new HBox(15);
        conversionBox.setAlignment(Pos.CENTER);

        // From base selection
        VBox fromBox = new VBox(5);
        Label fromLabel = new Label("From Base");
        fromLabel.setStyle("-fx-text-fill: #2c3e50;");
        fromBase = createStyledComboBox();
        fromBase.getItems().addAll("Binary", "Decimal", "Hexadecimal", "Octal");
        fromBase.setValue("Decimal");
        fromBox.getChildren().addAll(fromLabel, fromBase);

        // Swap button
        Button swapButton = new Button("⇄");
        swapButton.setStyle("-fx-background-radius: 50; -fx-min-width: 40; -fx-min-height: 40; " +
                "-fx-background-color: #3498db; -fx-text-fill: white;");
        swapButton.setOnAction(e -> swapBases());

        // To base selection
        VBox toBox = new VBox(5);
        Label toLabel = new Label("To Base");
        toLabel.setStyle("-fx-text-fill: #2c3e50;");
        toBase = createStyledComboBox();
        toBase.getItems().addAll("Binary", "Decimal", "Hexadecimal", "Octal");
        toBase.setValue("Binary");
        toBox.getChildren().addAll(toLabel, toBase);

        conversionBox.getChildren().addAll(fromBox, swapButton, toBox);

        // Buttons box
        HBox buttonsBox = new HBox(10);
        buttonsBox.setAlignment(Pos.CENTER);

        Button convertButton = createStyledButton("Convert", "#2ecc71");
        Button clearButton = createStyledButton("Clear", "#e74c3c");
        Button historyButton = createStyledButton("Show History", "#3498db");

        buttonsBox.getChildren().addAll(convertButton, clearButton, historyButton);

        convertButton.setOnAction(e -> convertNumber());
        clearButton.setOnAction(e -> clearFields());
        historyButton.setOnAction(e -> showHistory());

        // Result section
        resultLabel = new Label("Result will appear here");
        resultLabel.setStyle("-fx-padding: 10; -fx-background-color: #f8f9fa; " +
                "-fx-background-radius: 5; -fx-text-fill: #2c3e50;");
        resultLabel.setMaxWidth(Double.MAX_VALUE);
        resultLabel.setAlignment(Pos.CENTER);

        contentBox.getChildren().addAll(
                new Label("Input Number:"),
                inputField,
                conversionBox,
                buttonsBox,
                new Separator(),
                resultLabel);

        return contentBox;
    }

    private void createHistoryWindow() {
        historyStage = new Stage();
        historyStage.initModality(Modality.APPLICATION_MODAL);
        historyStage.setTitle("Conversion History");

        VBox historyBox = new VBox(10);
        historyBox.setStyle("-fx-background-color: white; -fx-padding: 20;");
        historyBox.setPadding(new Insets(15));

        Label historyLabel = new Label("Conversion History");
        historyLabel.setFont(Font.font("Segoe UI", FontWeight.BOLD, 14));

        conversionHistoryArea = new TextArea();
        conversionHistoryArea.setEditable(false);
        conversionHistoryArea.setPrefRowCount(10);
        conversionHistoryArea.setPrefColumnCount(30);
        conversionHistoryArea.setStyle("-fx-background-color: #f8f9fa; -fx-background-radius: 5;");

        Button clearHistoryButton = createStyledButton("Clear History", "#95a5a6");
        Button closeButton = createStyledButton("Close", "#34495e");

        HBox buttonBox = new HBox(10);
        buttonBox.setAlignment(Pos.CENTER);
        buttonBox.getChildren().addAll(clearHistoryButton, closeButton);

        clearHistoryButton.setOnAction(e -> conversionHistoryArea.clear());
        closeButton.setOnAction(e -> historyStage.hide());

        historyBox.getChildren().addAll(historyLabel, conversionHistoryArea, buttonBox);

        Scene historyScene = new Scene(historyBox);
        historyStage.setScene(historyScene);
    }

    private ComboBox<String> createStyledComboBox() {
        ComboBox<String> comboBox = new ComboBox<>();
        comboBox.setStyle("-fx-background-radius: 5; -fx-padding: 5;");
        return comboBox;
    }

    private Button createStyledButton(String text, String baseColor) {
        Button button = new Button(text);
        updateButtonStyle(button, baseColor);
        return button;
    }

    private void updateButtonStyle(Button button, String baseColor) {
        button.setStyle(String.format("-fx-background-color: %s; -fx-text-fill: white; " +
                "-fx-padding: 10 20; -fx-background-radius: 5;", baseColor));
    }

    private void styleInput(TextField input) {
        input.setStyle("-fx-background-color: #f8f9fa; -fx-border-color: #dee2e6; " +
                "-fx-border-radius: 5; -fx-background-radius: 5;");
    }

    private void swapBases() {
        String tempBase = fromBase.getValue();
        fromBase.setValue(toBase.getValue());
        toBase.setValue(tempBase);
    }

    private void clearFields() {
        inputField.clear();
        resultLabel.setText("Result will appear here");
        resultLabel.setStyle("-fx-padding: 10; -fx-background-color: #f8f9fa; " +
                "-fx-background-radius: 5; -fx-text-fill: #2c3e50;");
    }

    private void convertNumber() {
        try {
            String input = inputField.getText().trim();
            String from = fromBase.getValue();
            String to = toBase.getValue();
            String result = "";

            // First convert to decimal
            int decimal = 0;
            switch (from) {
                case "Binary":
                    decimal = Integer.parseInt(input, 2);
                    break;
                case "Decimal":
                    decimal = Integer.parseInt(input);
                    break;
                case "Hexadecimal":
                    decimal = Integer.parseInt(input, 16);
                    break;
                case "Octal":
                    decimal = Integer.parseInt(input, 8);
                    break;
            }

            // Then convert from decimal to target base
            switch (to) {
                case "Binary":
                    result = Integer.toBinaryString(decimal);
                    break;
                case "Decimal":
                    result = String.valueOf(decimal);
                    break;
                case "Hexadecimal":
                    result = Integer.toHexString(decimal).toUpperCase();
                    break;
                case "Octal":
                    result = Integer.toOctalString(decimal);
                    break;
            }

            resultLabel.setText("Result: " + result);
            resultLabel.setStyle("-fx-padding: 10; -fx-background-color: #e8f5e9; " +
                    "-fx-background-radius: 5; -fx-text-fill: #2e7d32;");

            // Add to history
            String historyEntry = String.format("%s (%s) → %s (%s)\n",
                    input, from, result, to);
            conversionHistoryArea.appendText(historyEntry);

        } catch (NumberFormatException ex) {
            resultLabel.setText("Invalid input for selected base");
            resultLabel.setStyle("-fx-padding: 10; -fx-background-color: #ffebee; " +
                    "-fx-background-radius: 5; -fx-text-fill: #c62828;");
        }
    }

    private void showHistory() {
        if (historyStage != null) {
            // Center the history window relative to the main window
            if (!historyStage.isShowing()) {
                historyStage.show();
            } else {
                historyStage.requestFocus();
            }
        }
    }

    private Button createThemeToggleButton() {
        Button themeButton = new Button();
        themeButton.setStyle("-fx-background-radius: 50; -fx-min-width: 40; -fx-min-height: 40;");
        updateThemeButtonStyle(themeButton);

        themeButton.setOnAction(e -> {
            isDarkMode = !isDarkMode;
            updateTheme();
            updateThemeButtonStyle(themeButton);
        });

        return themeButton;
    }

    private void updateThemeButtonStyle(Button button) {
        String color = isDarkMode ? "#f1c40f" : "#34495e";
        String text = isDarkMode ? "☀" : "☾";
        button.setText(text);
        button.setStyle(String.format("-fx-background-color: %s; -fx-text-fill: white; " +
                "-fx-background-radius: 50; -fx-min-width: 40; -fx-min-height: 40; " +
                "-fx-font-size: 16px;", color));
    }

    private void updateTheme() {
        String bgColor = isDarkMode ? DARK_BG : LIGHT_BG;
        root.setStyle("-fx-background-color: " + bgColor + ";");

        // Update all styled components
        updateHeaderTheme();
        updateContentTheme();
        if (historyStage != null && historyStage.getScene() != null) {
            updateHistoryTheme();
        }
    }

    private void updateHeaderTheme() {
        for (Node node : root.getChildren()) {
            if (node instanceof HBox) {
                HBox headerContainer = (HBox) node;
                for (Node headerNode : headerContainer.getChildren()) {
                    if (headerNode instanceof VBox) {
                        VBox header = (VBox) headerNode;
                        for (Node label : header.getChildren()) {
                            if (label instanceof Label) {
                                if (((Label) label).getFont().getSize() > 20) {
                                    // Title
                                    label.setStyle("-fx-text-fill: " + (isDarkMode ? DARK_TEXT : LIGHT_TEXT) + ";");
                                } else {
                                    // Subtitle
                                    label.setStyle(
                                            "-fx-text-fill: " + (isDarkMode ? DARK_SECONDARY : LIGHT_SECONDARY) + ";");
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private void updateContentTheme() {
        String cardBg = isDarkMode ? DARK_CARD_BG : LIGHT_CARD_BG;
        String textColor = isDarkMode ? DARK_TEXT : LIGHT_TEXT;
        String inputBg = isDarkMode ? DARK_INPUT_BG : LIGHT_INPUT_BG;

        // Update main content box
        for (Node node : root.getChildren()) {
            if (node instanceof VBox && node != root) {
                node.setStyle(String.format("-fx-background-color: %s; -fx-padding: 20; " +
                        "-fx-background-radius: 10; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.2), 10, 0, 0, 2);",
                        cardBg));

                updateNodeTheme((VBox) node, textColor, inputBg);
            }
        }
    }

    private void updateNodeTheme(VBox container, String textColor, String inputBg) {
        for (Node node : container.getChildren()) {
            if (node instanceof Label) {
                node.setStyle("-fx-text-fill: " + textColor + ";");
            } else if (node instanceof TextField) {
                node.setStyle(String.format("-fx-background-color: %s; -fx-text-fill: %s; " +
                        "-fx-border-color: %s; -fx-border-radius: 5; -fx-background-radius: 5;",
                        inputBg, textColor, isDarkMode ? "#555" : "#dee2e6"));
            } else if (node instanceof ComboBox) {
                ((ComboBox<?>) node).setStyle(String.format("-fx-background-color: %s; -fx-text-fill: %s;",
                        inputBg, textColor));
            }
        }
    }

    private void updateHistoryTheme() {
        VBox historyBox = (VBox) historyStage.getScene().getRoot();
        String cardBg = isDarkMode ? DARK_CARD_BG : LIGHT_CARD_BG;
        String textColor = isDarkMode ? DARK_TEXT : LIGHT_TEXT;
        String inputBg = isDarkMode ? DARK_INPUT_BG : LIGHT_INPUT_BG;

        historyBox.setStyle(String.format("-fx-background-color: %s; -fx-padding: 20;", cardBg));

        for (Node node : historyBox.getChildren()) {
            if (node instanceof Label) {
                node.setStyle("-fx-text-fill: " + textColor + ";");
            } else if (node instanceof TextArea) {
                TextArea textArea = (TextArea) node;
                textArea.setStyle(String.format("-fx-background-color: %s; -fx-text-fill: %s; " +
                        "-fx-control-inner-background: %s;", inputBg, textColor, inputBg));
            }
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
