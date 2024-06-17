package com.example.carrentalapp;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.hibernate.Session;
import org.hibernate.Transaction;

public class AddNewRentModalController {

    @FXML
    private TextField rentedDateColumnTextField;

    @FXML
    private TextField rentedFromColumnTextField;

    @FXML
    private TextField rentedUntilColumnTextField;

    private Stage stage;


    public void setStage(Stage stage) {
        this.stage = stage;
    }

    @FXML
    private void initialize() {
    }

    @FXML
    private void handleSubmitButtonAction() {
        String renteddatecolumn = getRenteddatecolumn();
        String rentedfromcolumn = getRentedfromcolumn();
        String renteduntilcolumn = getRenteduntilcolumn();

        if (renteddatecolumn.isEmpty() || rentedfromcolumn.isEmpty() || renteduntilcolumn.isEmpty()) {
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

    public String getRenteddatecolumn() {
        return rentedDateColumnTextField.getText();
    }

    public String getRentedfromcolumn() {return rentedFromColumnTextField.getText();}

    public String getRenteduntilcolumn() {
        return rentedUntilColumnTextField.getText();
    }
}
