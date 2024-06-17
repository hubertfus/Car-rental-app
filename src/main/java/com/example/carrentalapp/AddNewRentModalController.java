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

/**
 * Controller class for handling the addition of a new rental record.
 */
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

    /**
     * Sets the stage for this controller, which is used to manage the modal window.
     *
     * @param stage The stage of the modal window.
     */
    public void setStage(Stage stage) {
        this.stage = stage;
    }

    /**
     * Initializes the controller. Sets the items for client and car choice boxes.
     */
    @FXML
    private void initialize() {
        clientChoiceBox.setItems(ClientsController.getClients());
        carChoiceBox.setItems(AvailableCarsController.getCars());
    }

    /**
     * Handles the action when the submit button is clicked. Validates input fields and saves the rental record.
     * Shows appropriate alerts for success or validation errors.
     */
    @FXML
    private void handleSubmitButtonAction() {
        String rentedDate = getRentedDate();
        String rentedFromDate = getRentedFrom();
        String rentedUntilDate = getRentedUntil();

        if (rentedDate.isEmpty()  || rentedFromDate.isEmpty() || rentedUntilDate.isEmpty() || clientChoiceBox.getValue() == null || carChoiceBox.getValue() == null) {
            showAlert(Alert.AlertType.ERROR, "Validation Error", "All fields must be filled.");
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
            showAlert(Alert.AlertType.INFORMATION, "Success", "Car successfully rented.");
        }

        stage.close();
    }

    /**
     * Shows an alert dialog with the given type, title, and message.
     *
     * @param alertType The type of the alert (e.g., ERROR, INFORMATION).
     * @param title The title of the alert.
     * @param message The message content of the alert.
     */
    private void showAlert(Alert.AlertType alertType,String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    /**
     * Retrieves the rented date from the date picker as a string.
     *
     * @return A string representation of the rented date.
     */
    public String getRentedDate() {
        LocalDate x = rentedDateDatePicker.getValue();
        if(x == null) {
            return "";
        }
        return x.toString();
    }

    /**
     * Retrieves the rented from date from the date picker as a string.
     *
     * @return A string representation of the rented from date.
     */
    public String getRentedFrom() {
        LocalDate x = rentedFromDatePicker.getValue();
        if(x == null) {
            return "";
        }
        return x.toString();
    }

    /**
     * Retrieves the rented until date from the date picker as a string.
     *
     * @return A string representation of the rented until date.
     */
    public String getRentedUntil() {
        LocalDate x = rentedUntilDatePicker.getValue();
        if(x == null) {
            return "";
        }
        return x.toString();
    }
}