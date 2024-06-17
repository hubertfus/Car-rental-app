package com.example.carrentalapp;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.stage.Stage;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

public class RentedCarDetailsModalController {
    @FXML
    private DatePicker rentedDateDatePicker;

    @FXML
    private DatePicker rentedFromDatePicker;

    @FXML
    private DatePicker rentedUntilDatePicker;

    @FXML
    private ChoiceBox<String> clientChoiceBox;

    @FXML
    private ChoiceBox<Car> carChoiceBox;

    private Stage stage;

    RentedCar rentedCar;

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    @FXML
    private void initialize() {
        // Default initialization if needed
    }

    public void setRentedCar(RentedCar rentedCar) {
        this.rentedCar = rentedCar;
        if (rentedCar != null) {
            ObservableList<Car> cars = FXCollections.observableArrayList();

            carChoiceBox.setItems(cars);
            ObservableList<String> clientNames = FXCollections.observableArrayList();
            for (Object client : ClientsController.getClients()) {
                Client temp = (Client) client;
                clientNames.add(temp.getId() + " " + temp.getFirstname() + " " + temp.getLastname()  + " " + temp.getEmail());
            }
            clientChoiceBox.setItems(clientNames);
            clientChoiceBox.setItems(clientNames);
            rentedDateDatePicker.setValue(LocalDate.parse(rentedCar.getRentedDate()));
            rentedFromDatePicker.setValue(LocalDate.parse(rentedCar.getRentedFrom()));
            rentedUntilDatePicker.setValue(LocalDate.parse(rentedCar.getRentedUntil()));

            for (int i = 0; i < ClientsController.getClients().size(); i++) {
                System.out.println(ClientsController.getClients().get(i));
                if(Objects.equals(ClientsController.getClients().get(i).toString().split(" ")[0], rentedCar.getClient().getId().toString())){
                    clientChoiceBox.getSelectionModel().select(i);
                    break;
                }
            }

            try (Session session = HibernateUtil.getSessionFactory().openSession()) {
                String hql = "from Car c join fetch c.engine where c.id = " + rentedCar.getCar().getId();
                Query<Car> query = session.createQuery(hql, Car.class);
                List<Car> resultList = query.getResultList();
                cars.add(resultList.getFirst());
            }
            cars.addAll(AvailableCarsController.getCars());
            carChoiceBox.getSelectionModel().select(0);
        }
    }



    public void handleSaveButtonAction() {
        String rentedDate = getRentedDate();
        String rentedFromDate = getRentedFrom();
        String rentedUntilDate = getRentedUntil();

        if (rentedDate.isEmpty() || rentedFromDate.isEmpty() || rentedUntilDate.isEmpty() || clientChoiceBox.getValue() == null || carChoiceBox.getValue() == null) {
            showAlert(Alert.AlertType.ERROR, "Validation Error", "All fields must be filled.");
            return;
        }

        Car car = carChoiceBox.getValue();
        String selectedClientId = clientChoiceBox.getValue().split(" ")[0]; // Assuming it's formatted as "id firstname lastname"
        Client client = ClientsController.getClientByClientID(Integer.parseInt(selectedClientId));

        showAlert(Alert.AlertType.INFORMATION, "Success", "Car successfully rented.");
        rentedCar.setRentedDate(rentedDate);
        rentedCar.setRentedFrom(rentedFromDate);
        rentedCar.setRentedUntil(rentedUntilDate);
        rentedCar.setClient(client);
        rentedCar.setCar(car);

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            session.merge(rentedCar);
            session.flush();
            transaction.commit();
            RentedCarsController.updateRentedCar(rentedCar);
        } catch (Exception e) {
            e.printStackTrace();
        }

        stage.close();
    }


    public void handleDeleteButtonAction() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            session.remove(rentedCar);
            session.flush();
            transaction.commit();
            RentedCarsController.deleteRentedCar(rentedCar.getId());
        }

        showAlert(Alert.AlertType.INFORMATION, "Success", "Details have been successfully deleted.");

        stage.close();
    }

    public void handleCancelButtonAction() {
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
