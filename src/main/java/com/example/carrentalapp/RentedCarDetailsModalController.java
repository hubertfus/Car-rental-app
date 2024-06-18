package com.example.carrentalapp;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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

/**
 * Controller class for handling actions and initialization of the rented car details modal.
 */
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

    private RentedCar rentedCar;

    /**
     * Sets the stage for this controller.
     *
     * @param stage The stage to set.
     */
    public void setStage(Stage stage) {
        this.stage = stage;
    }

    /**
     * Initializes the controller with the details of the rented car.
     *
     * @param rentedCar The rented car entity to display and edit.
     */
    public void setRentedCar(RentedCar rentedCar) {
        this.rentedCar = rentedCar;
        if (rentedCar != null) {
            ObservableList<Car> cars = FXCollections.observableArrayList();
            carChoiceBox.setItems(cars);

            ObservableList<String> clientNames = FXCollections.observableArrayList();
            for (Object client : ClientsController.getClients()) {
                Client temp = (Client) client;
                clientNames.add(temp.getId() + " " + temp.getFirstname() + " " + temp.getLastname() + " " + temp.getEmail());
            }
            clientChoiceBox.setItems(clientNames);

            rentedDateDatePicker.setValue(LocalDate.parse(rentedCar.getRentedDate()));
            rentedFromDatePicker.setValue(LocalDate.parse(rentedCar.getRentedFrom()));
            rentedUntilDatePicker.setValue(LocalDate.parse(rentedCar.getRentedUntil()));

            for (int i = 0; i < ClientsController.getClients().size(); i++) {
                if (Objects.equals(ClientsController.getClients().get(i).toString().split(" ")[0], rentedCar.getClient().getId().toString())) {
                    clientChoiceBox.getSelectionModel().select(i);
                    break;
                }
            }

            try (Session session = HibernateUtil.getSessionFactory().openSession()) {
                String hql = "from Car c join fetch c.engine where c.id = " + rentedCar.getCar().getId();
                Query<Car> query = session.createQuery(hql, Car.class);
                List<Car> resultList = query.getResultList();
                cars.add(resultList.get(0)); // Assuming only one car is fetched
            }
            cars.addAll(AvailableCarsController.getCars());
            carChoiceBox.getSelectionModel().select(0); // Select the first car
        }
    }

    /**
     * Handles the action when the save button is clicked.
     * Updates the rented car details in the database.
     */
    public void handleSaveButtonAction() {
        String rentedDate = getRentedDate();
        String rentedFromDate = getRentedFrom();
        String rentedUntilDate = getRentedUntil();

        if (rentedDate.isEmpty() || rentedFromDate.isEmpty() || rentedUntilDate.isEmpty() || clientChoiceBox.getValue() == null || carChoiceBox.getValue() == null) {
            showAlert(Alert.AlertType.ERROR, "Błąd walidacji", "Wszystkie pola muszą być wypełnione.");
            return;
        }

        Car car = carChoiceBox.getValue();
        String selectedClientId = clientChoiceBox.getValue().split(" ")[0]; // Assuming it's formatted as "id firstname lastname"
        Client client = ClientsController.getClientByClientID(Integer.parseInt(selectedClientId));

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
            AvailableCarsController.loadAvailableCars();
            showAlert(Alert.AlertType.INFORMATION, "Sukces", "Samochód został pomyślnie wynajęty.");
            stage.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Handles the action when the delete button is clicked.
     * Deletes the rented car details from the database.
     */
    public void handleDeleteButtonAction() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            session.remove(rentedCar);
            session.flush();
            transaction.commit();
            RentedCarsController.deleteRentedCar(rentedCar.getId());
            AvailableCarsController.loadAvailableCars();
            showAlert(Alert.AlertType.INFORMATION, "Sukces", "Szczegóły zostały pomyślnie usunięte.");

            stage.close();
        }
    }

    /**
     * Handles the action when the cancel button is clicked.
     * Closes the modal window.
     */
    public void handleCancelButtonAction() {
        stage.close();
    }

    /**
     * Displays an alert dialog with the specified type, title, and message.
     *
     * @param alertType The type of the alert (e.g., ERROR, INFORMATION).
     * @param title     The title of the alert.
     * @param message   The message to display in the alert.
     */
    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    /**
     * Retrieves the rented date from the date picker.
     *
     * @return The rented date as a string.
     */
    public String getRentedDate() {
        LocalDate x = rentedDateDatePicker.getValue();
        if (x == null) {
            return "";
        }
        return x.toString();
    }

    /**
     * Retrieves the rented from date from the date picker.
     *
     * @return The rented from date as a string.
     */
    public String getRentedFrom() {
        LocalDate x = rentedFromDatePicker.getValue();
        if (x == null) {
            return "";
        }
        return x.toString();
    }

    /**
     * Retrieves the rented until date from the date picker.
     *
     * @return The rented until date as a string.
     */
    public String getRentedUntil() {
        LocalDate x = rentedUntilDatePicker.getValue();
        if (x == null) {
            return "";
        }
        return x.toString();
    }
}
