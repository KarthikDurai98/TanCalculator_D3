package com.example.tangentCalc;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.FileHandler;
import java.util.logging.SimpleFormatter;
import java.io.IOException;

/**
 * Main class for the Tangent Calculator application.
 */
public class Main extends Application {

    /**
     * Logger for logging events in the application.
     */
    private static final Logger LOGGER = Logger.getLogger(Main.class.getName());

    /**
     * Main pane for the application.
     */
    private StackPane mainPane;

    /**
     * Observable list for recent calculations.
     */
    private ObservableList<String> recentCalculations =
            FXCollections.observableArrayList();

    /**
     * Width of the application window.
     */
    private static final int WIDTH = 800;

    /**
     * Height of the application window.
     */
    private static final int HEIGHT = 600;

    /**
     * Padding value for layouts.
     */
    private static final int PADDING = 20;

    /**
     * Default constructor for Main class.
     */
    public Main() {
        // Default constructor
    }

    static {
        try {
            FileHandler fileHandler = new FileHandler("tanFunction.log", true);
            fileHandler.setFormatter(new SimpleFormatter());
            LOGGER.addHandler(fileHandler);
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Failed to initialize logger", e);
        }
    }

    /**
     * Main entry point for the application.
     *
     * @param args the command line arguments
     */
    public static void main(final String[] args) {
        runTangentTests();
        launch(args);
    }

    /**
     * Starts the JavaFX application.
     *
     * @param primaryStage the primary stage for this application
     */
    @Override
    public void start(final Stage primaryStage) {
        // Add version info to the title
        primaryStage.setTitle("Tangent Calculator - Version " + VersionUtil.getVersion());


        mainPane = new StackPane();
        mainPane.getChildren().add(createWelcomeScene());

        Scene scene = new Scene(mainPane, WIDTH, HEIGHT);
        // Load CSS
        String css = getClass()
                .getResource("/com/example/tangentCalc/style.css")
                .toExternalForm();
        scene.getStylesheets().add(css);
        primaryStage.setScene(scene);
        primaryStage.setFullScreen(true);
        primaryStage.show();
    }

    /**
     * Creates the welcome scene.
     *
     * @return the VBox containing the welcome scene components
     */
    private VBox createWelcomeScene() {
        VBox welcomeLayout = new VBox(PADDING);
        welcomeLayout.setAlignment(Pos.CENTER);
        welcomeLayout.setPadding(new Insets(PADDING));
        welcomeLayout.getStyleClass().add("root");

        Label welcomeLabel = new Label("Welcome to the Tangent Calculator");
        welcomeLabel.getStyleClass().add("label-welcome");

        Label descriptionLabel = new Label(
                "Calculate the tangent of any angle in radians or "
                        + "degrees effortlessly!"
        );
        descriptionLabel.getStyleClass().add("label-description");

        Button startButton = new Button("Start Calculating");
        startButton.getStyleClass().add("button-compute");
        startButton.setOnAction(e -> switchToCalculatorScene());

        welcomeLayout.getChildren().addAll(
                welcomeLabel, descriptionLabel, startButton);
        return welcomeLayout;
    }

    /**
     * Creates the calculator scene.
     *
     * @return the VBox containing the calculator scene components
     */
    private VBox createCalculatorScene() {
        VBox layout = new VBox(PADDING);
        layout.setPadding(new Insets(PADDING));
        layout.setAlignment(Pos.TOP_CENTER);
        layout.getStyleClass().add("root");

        Label titleLabel = new Label("Tangent Calculator");
        titleLabel.getStyleClass().add("label-title");

        ChoiceBox<String> unitChoiceBox = new ChoiceBox<>(
                FXCollections.observableArrayList("Radians", "Degrees"));
        unitChoiceBox.setValue("Radians");
        unitChoiceBox.getStyleClass().add("choice-box");

        TextField inputField = new TextField();
        inputField.setPromptText("Enter angle");
        inputField.getStyleClass().add("text-field");

        Label validationLabel = new Label();
        validationLabel.getStyleClass().add("label-validation");

        inputField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("-?\\d*(\\.\\d*)?")) {
                validationLabel.setText("Invalid number format. Please enter a valid number "
                        + "(e.g., 45, -3.14).");
            } else {
                validationLabel.setText("");
            }
        });

        Button computeButton = new Button("Compute");
        computeButton.getStyleClass().add("button-compute");

        ListView<String> recentCalculationsList =
                new ListView<>(recentCalculations);
        recentCalculationsList.getStyleClass().add("list-view");

        computeButton.setOnAction(e -> {
            String userInput = inputField.getText();
            String unit = unitChoiceBox.getValue();
            handleComputeButton(userInput, unit, validationLabel);
        });

        layout.getChildren().addAll(titleLabel, unitChoiceBox, inputField,
                validationLabel, computeButton, recentCalculationsList);

        return layout;
    }

    /**
     * Switches the scene to the calculator scene.
     */
    private void switchToCalculatorScene() {
        mainPane.getChildren().clear();
        mainPane.getChildren().add(createCalculatorScene());
    }

    /**
     * Handles the compute button click event.
     *
     * @param userInput the input angle from the user
     * @param unit the unit of the angle (Radians or Degrees)
     * @param validationLabel the label to display validation messages
     */
    private void handleComputeButton(final String userInput, final String unit,
                                     final Label validationLabel) {
        try {
            if (!userInput.matches("-?\\d*(\\.\\d*)?")) {
                validationLabel.setText(
                        "Invalid input. Please enter a valid number (e.g., 45, -3.14)."
                );
            } else {
                double angle = TrigUtil.parseDouble(userInput);
                double originalAngle = angle;
                String originalUnit = unit;

                if (unit.equals("Degrees")) {
                    angle = TrigUtil.toRadians(angle);
                }

                double result = TrigUtil.computeTangent(angle);
                if (Double.isNaN(result)) {
                    validationLabel.setText("The tangent function is undefined for angle = "
                            + userInput + " " + unit + ". Result: NaN");
                    logTangentResult(originalAngle, originalUnit, Double.NaN,
                            "Undefined");
                } else {
                    String resultText = "tan(" + TrigUtil.formatAngle(userInput) + " "
                            + unit + ") = " + TrigUtil.formatResult(result);
                    recentCalculations.add(0, resultText);
                    validationLabel.setText("");
                    logTangentResult(originalAngle, originalUnit, result,
                            "Computed");
                }
            }
        } catch (NumberFormatException ex) {
            validationLabel.setText("Invalid input. Please enter a valid number "
                    + "(e.g., 45, -3.14).");
            LOGGER.log(Level.SEVERE, "Invalid input: " + userInput, ex);
        } catch (Exception ex) {
            validationLabel.setText("An unexpected error occurred. Please try "
                    + "again later.");
            LOGGER.log(Level.SEVERE, "An unexpected error occurred", ex);
        }
    }

    /**
     * Logs the result of the tangent computation.
     *
     * @param angle the input angle
     * @param unit the unit of the angle
     * @param result the result of the tangent computation
     * @param status the status of the computation (e.g., Computed, Undefined)
     */
    private void logTangentResult(final double angle, final String unit,
                                  final double result, final String status) {
        String resultStr = Double.isNaN(result)
                ? "NaN"
                : TrigUtil.formatResult(result);
        LOGGER.log(Level.INFO, String.format("%s tan(%s %s) = %s", status,
                TrigUtil.formatAngle(String.valueOf(angle)), unit, resultStr));
    }

    /**
     * Runs tests for the tangent computation.
     */
    private static void runTangentTests() {
        final double piValue = TrigUtil.pi();
        final double[] testAngles = {
                piValue / 2, 3 * piValue / 2, 5 * piValue / 2, -piValue / 2,
                -3 * piValue / 2
        };
        for (double angle : testAngles) {
            double result = TrigUtil.computeTangent(angle);
            System.out.println("tan(" + angle + ") = " + result);
        }
    }
}
