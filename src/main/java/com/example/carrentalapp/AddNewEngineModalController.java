package com.example.carrentalapp;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.hibernate.Session;
import org.hibernate.Transaction;

/**
 * Controller class for adding a new engine in the car rental application.
 */
public class AddNewEngineModalController {

    @FXML
    private TextField nameTextField; // TextField for inputting the engine's name.

    @FXML
    private TextField powerTextField; // TextField for inputting the engine's power.

    @FXML
    private TextField fueltypeTextField; // TextField for inputting the engine's fuel type.

    private Stage stage; // The stage for this modal dialog.

    /**
     * Sets the stage of this dialog.
     *
     * @param stage The stage to set.
     */
    public void setStage(Stage stage) {
        this.stage = stage;
    }

    @FXML
    private void initialize() {
        // Initialization logic (if needed) goes here.
    }

    /**
     * Handles the submit button action.
     * Validates the input fields and adds a new engine to the database if valid.
     */
    @FXML
    private void handleSubmitButtonAction() {
        String name = getName();
        String power = getPower();
        String fueltype = getFueltype();

        // Validate that all fields are filled.
        if (name.isEmpty() || power.isEmpty() || fueltype.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Błąd walidacji", "Wszystkie pola muszą być wypełnione.");
            return;
        }

        int powerNumber;
        try {
            // Parse the power input as an integer.
            powerNumber = Integer.parseInt(power);
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Błąd walidacji", "Moc silnika musi być liczbą.");
            return;
        }

// Create a new Engine object and save it to the database.
        Engine engine = new Engine(name, powerNumber, fueltype);
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            session.persist(engine);
            session.flush();
            transaction.commit();
            AddNewCarModalController.addEngine(engine);
        }

// Show success message and close the dialog.
        showAlert(Alert.AlertType.INFORMATION, "Sukces", "Silnik został pomyślnie dodany.");

        stage.close();
    }

    /**
     * Shows an alert dialog with a given type, title, and message.
     *
     * @param alertType The type of alert to show.
     * @param title     The title of the alert dialog.
     * @param message   The message to display in the alert dialog.
     */
    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    /**
     * Retrieves the entered name from the nameTextField.
     *
     * @return The entered engine name.
     */
    public String getName() {
        return nameTextField.getText();
    }

    /**
     * Retrieves the entered power from the powerTextField.
     *
     * @return The entered engine power as a String.
     */
    public String getPower() {
        return powerTextField.getText();
    }

    /**
     * Retrieves the entered fuel type from the fueltypeTextField.
     *
     * @return The entered engine fuel type.
     */
    public String getFueltype() {
        return fueltypeTextField.getText();
    }
}
