package com.example.carrentalapp;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.hibernate.Session;
import org.hibernate.Transaction;

public class AddNewClientModalController {

    @FXML
    private TextField firstNameTextField;

    @FXML
    private TextField lastNameTextField;

    @FXML
    private TextField emailTextField;

    private Stage stage;


    public void setStage(Stage stage) {
        this.stage = stage;
    }

    @FXML
    private void initialize() {
    }

    @FXML
    private void handleSubmitButtonAction() {
        String firstname = getFirstname();
        String lastname = getLastname();
        String email = getEmail();

        if (firstname.isEmpty() || lastname.isEmpty() || email.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Błąd walidacji", "Wszystkie pola muszą być wypełnione.");
            return;
        }
        showAlert(Alert.AlertType.INFORMATION, "Sukces", "Silnik został pomyślnie dodany.");

        stage.close();
    }

    private void showAlert(Alert.AlertType alertType,String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public String getFirstname() {
        return firstNameTextField.getText();
    }

    public String getLastname() {return lastNameTextField.getText();}

    public String getEmail() {
        return emailTextField.getText();
    }
}
