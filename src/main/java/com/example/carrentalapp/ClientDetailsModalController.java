package com.example.carrentalapp;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import org.hibernate.Session;
import org.hibernate.Transaction;

public class ClientDetailsModalController {

    @FXML
    private Label clientIdLabel;

    @FXML
    private TextField firstNameTextField;

    @FXML
    private TextField lastNameTextField;

    @FXML
    private TextField emailTextField;

    private Client client;

    private Stage stage;

    @FXML
    private void initialize() {

    }

    @FXML
    private void handleSaveButtonAction() {
        String firstName = getFirstName();
        String lastName =  getLastName();
        String email = getEmail();

        if (firstName.isEmpty() || lastName.isEmpty() || email.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Błąd walidacji", "Wszystkie pola muszą być wypełnione.");
            return;
        }

        client.setFirstname(firstName);
        client.setLastname(lastName);
        client.setEmail(email);


        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            session.merge(client);
            session.flush();
            transaction.commit();
            ClientsController.updateClient(client);
        }

        showAlert(Alert.AlertType.INFORMATION, "Sukces", "Dane zostały pomyślnie zmienione.");

        stage.close();
    }

    private void showAlert(Alert.AlertType alertType,String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    private void handleCancelButtonAction() {
        stage.close();
    }

    @FXML
    private void handleDeleteButtonAction() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            session.remove(client);
            session.flush();
            transaction.commit();
            ClientsController.deleteClient(client.getId());
        }

        ClientsController.deleteClient(client.getId());
        showAlert(Alert.AlertType.INFORMATION, "Sukces", "Dane zostały pomyślnie usunięte.");

        stage.close();
    }

    void setStage(Stage stage){
        this.stage = stage;
    }


    void setClient(Client client) {
        this.client = client;
        clientIdLabel.setText(client.getId().toString());
        firstNameTextField.setText(client.getFirstname());
        lastNameTextField.setText(client.getLastname());
        emailTextField.setText(client.getEmail());
    }

    public String getFirstName() {
        return firstNameTextField.getText();
    }

    public String getLastName() {
        return lastNameTextField.getText();
    }

    public String getEmail() {
        return emailTextField.getText();
    }
}
