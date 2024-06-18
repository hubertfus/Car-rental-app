package com.example.carrentalapp;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import org.hibernate.Session;
import org.hibernate.Transaction;

/**
 * Controller class for managing client details in the car rental application.
 */
public class ClientDetailsModalController {

    @FXML
    private Label clientIdLabel; // Label to display the client's ID.

    @FXML
    private TextField firstNameTextField; // TextField for inputting the client's first name.

    @FXML
    private TextField lastNameTextField; // TextField for inputting the client's last name.

    @FXML
    private TextField emailTextField; // TextField for inputting the client's email.

    private Client client; // The current client being edited.

    private Stage stage; // The stage for this modal dialog.

    @FXML
    private void initialize() {
        // Initialization logic (if needed) goes here.
    }

    /**
     * Handles the save button action.
     * Updates the client's details with the entered values.
     */
    @FXML
    private void handleSaveButtonAction() {
        String firstName = getFirstName();
        String lastName =  getLastName();
        String email = getEmail();

        // Validate that all fields are filled.
        if (firstName.isEmpty() || lastName.isEmpty() || email.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Błąd walidacji", "Wszystkie pola muszą być wypełnione.");
            return;
        }

        // Update client details.
        client.setFirstname(firstName);
        client.setLastname(lastName);
        client.setEmail(email);

        // Save changes to the database.
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            session.merge(client);
            session.flush();
            transaction.commit();
            ClientsController.updateClient(client);
        }

        // Show success message and close the dialog.
        showAlert(Alert.AlertType.INFORMATION, "Sukces", "Szczegóły zostały pomyślnie zmienione.");







        stage.close();
    }

    /**
     * Shows an alert dialog with a given type, title, and message.
     *
     * @param alertType The type of alert to show.
     * @param title     The title of the alert dialog.
     * @param message   The message to display in the alert dialog.
     */
    private void showAlert(Alert.AlertType alertType,String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    /**
     * Handles the cancel button action.
     * Closes the modal dialog without saving changes.
     */
    @FXML
    private void handleCancelButtonAction() {
        stage.close();
    }

    /**
     * Handles the delete button action.
     * Deletes the current client from the database.
     */
    @FXML
    private void handleDeleteButtonAction() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            session.remove(client);
            session.flush();
            transaction.commit();
            ClientsController.deleteClient(client.getId());
        }

        showAlert(Alert.AlertType.INFORMATION, "Sukces", "Szczegóły zostały pomyślnie usunięte.");

        stage.close();
    }

    /**
     * Sets the stage of this dialog.
     *
     * @param stage The stage to set.
     */
    void setStage(Stage stage){
        this.stage = stage;
    }

    /**
     * Sets the client to be edited in this dialog.
     *
     * @param client The client to set.
     */
    void setClient(Client client) {
        this.client = client;

        // Populate fields with existing client details.
        clientIdLabel.setText(client.getId().toString());
        firstNameTextField.setText(client.getFirstname());
        lastNameTextField.setText(client.getLastname());
        emailTextField.setText(client.getEmail());
    }

    /**
     * Retrieves the entered first name from the firstNameTextField.
     *
     * @return The entered first name.
     */
    public String getFirstName() {
        return firstNameTextField.getText();
    }

    /**
     * Retrieves the entered last name from the lastNameTextField.
     *
     * @return The entered last name.
     */
    public String getLastName() {
        return lastNameTextField.getText();
    }

    /**
     * Retrieves the entered email from the emailTextField.
     *
     * @return The entered email address.
     */
    public String getEmail() {
        return emailTextField.getText();
    }
}
