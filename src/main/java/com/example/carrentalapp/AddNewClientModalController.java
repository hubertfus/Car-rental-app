package com.example.carrentalapp;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.hibernate.Session;
import org.hibernate.Transaction;

/**
 * Controller class for the 'Add New Client' modal.
 */
public class AddNewClientModalController {

    // FXML annotations to inject components from FXML
    @FXML
    private TextField firstNameTextField;

    @FXML
    private TextField lastNameTextField;

    @FXML
    private TextField emailTextField;

    // Stage reference to control the modal window
    private Stage stage;

    /**
     * Sets the stage of this modal.
     *
     * @param stage The stage window.
     */
    public void setStage(Stage stage) {
        this.stage = stage;
    }


    /**
     * Handles the submit button action.
     * Validates input fields and adds a new client to the database.
     */
    @FXML
    private void handleSubmitButtonAction() {
        String firstname = getFirstname();
        String lastname = getLastname();
        String email = getEmail();

        // Validate input fields are not empty
        if (firstname.isEmpty() || lastname.isEmpty() || email.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Błąd walidacji", "Wszystkie pola muszą być wypełnione.");
            return;
        }

        // Show success message
        showAlert(Alert.AlertType.INFORMATION, "Sukces", "Klient został pomyślnie dodany.");

        // Create new client object and persist it to the database
        Client client = new Client(firstname, lastname, email);
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            session.persist(client);
            session.flush();
            transaction.commit();
            ClientsController.addClient(client);
        }

        // Close the modal window
        stage.close();
    }

    /**
     * Shows an alert dialog to the user.
     *
     * @param alertType The type of alert.
     * @param title The title of the alert dialog.
     * @param message The message to display in the alert dialog.
     */
    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    /**
     * Retrieves the first name from the first name text field.
     *
     * @return The entered first name.
     */
    public String getFirstname() {
        return firstNameTextField.getText();
    }

    /**
     * Retrieves the last name from the last name text field.
     *
     * @return The entered last name.
     */
    public String getLastname() {
        return lastNameTextField.getText();
    }

    /**
     * Retrieves the email from the email text field.
     *
     * @return The entered email address.
     */
    public String getEmail() {
        return emailTextField.getText();
    }
}
