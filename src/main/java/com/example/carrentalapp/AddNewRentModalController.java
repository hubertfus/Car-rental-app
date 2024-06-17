package com.example.carrentalapp;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

public class AddNewRentModalController {

    @FXML
    private DatePicker rentedDateDatePicker;

    @FXML
    private DatePicker rentedFromDatePicker;

    @FXML
    private DatePicker rentedUntilDatePicker;

    @FXML
    private ChoiceBox clientChoiceBox;

    @FXML
    private ChoiceBox carChoiceBox;

    private Stage stage;


    public void setStage(Stage stage) {
        this.stage = stage;
    }

    @FXML
    private void initialize() {
        clientChoiceBox.setItems(ClientsController.getClients());
        carChoiceBox.setItems(AvailableCarsController.getCars());
    }

    @FXML
    private void handleSubmitButtonAction() {
        String rentedDate = getRentedDate();
        String rentedFromDate = getRentedFrom();
        String rentedUntilDate = getRentedUntil();



        if (rentedDate.isEmpty()  || rentedFromDate.isEmpty() || rentedUntilDate.isEmpty() || clientChoiceBox.getValue() == null || carChoiceBox.getValue() == null) {
            showAlert(Alert.AlertType.ERROR, "Błąd walidacji", "Wszystkie pola muszą być wypełnione.");
            return;
        }
        Car car = AvailableCarsController
                .getCarByCarID(Integer.parseInt(carChoiceBox.getValue().toString().split(" ")[0]));
        Client client = ClientsController
                .getClientByClientID(Integer.parseInt(clientChoiceBox.getValue().toString().split(" ")[0]));
        RentedCar rentedCar = new RentedCar(client,car,rentedDate,rentedFromDate,rentedUntilDate);
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            session.persist(rentedCar);
            session.flush();
            transaction.commit();
            RentedCarsController.addRentedCar(rentedCar);
            showAlert(Alert.AlertType.INFORMATION, "Sukces", "Samochód został pomyślnie wypożyczony.");
        }
        stage.close();
    }

    private void showAlert(Alert.AlertType alertType,String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public String getRentedDate() {
        LocalDate x = rentedDateDatePicker.getValue();
        if(x == null) {
            return "";
        }
        return x.toString();
    }

    public String getRentedFrom() {
        LocalDate x = rentedFromDatePicker.getValue();
        if(x == null) {
            return "";
        }
        return x.toString();
    }

    public String getRentedUntil() {
        LocalDate x = rentedUntilDatePicker.getValue();
        if(x == null) {
            return "";
        }
        return x.toString();
    }
}
