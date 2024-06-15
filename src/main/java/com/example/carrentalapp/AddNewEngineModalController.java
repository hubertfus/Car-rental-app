package com.example.carrentalapp;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.hibernate.Session;
import org.hibernate.Transaction;

public class AddNewEngineModalController {

    @FXML
    private TextField nameTextField;

    @FXML
    private TextField powerTextField;

    @FXML
    private TextField fueltypeTextField;

    private Stage stage;


    public void setStage(Stage stage) {
        this.stage = stage;
    }

    @FXML
    private void initialize() {
    }

    @FXML
    private void handleSubmitButtonAction() {
        String name = getName();
        String power = getPower();
        String fueltype = getFueltype();

        if (name.isEmpty() || power.isEmpty() || fueltype.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Błąd walidacji", "Wszystkie pola muszą być wypełnione.");
            return;
        }

        int powerNumber;
        try {
            powerNumber = Integer.parseInt(power);
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Błąd walidacji", "Moc silnika musi być liczbą.");
            return;
        }

        Engine engine = new Engine(name, powerNumber, fueltype);
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            session.persist(engine);
            session.flush();
            transaction.commit();
            AddNewCarModalController.addEngine(engine);
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

    public String getName() {
        return nameTextField.getText();
    }

    public String getPower() {
        return powerTextField.getText();
    }

    public String getFueltype() {
        return fueltypeTextField.getText();
    }
}
