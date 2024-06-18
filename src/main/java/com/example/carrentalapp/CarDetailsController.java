package com.example.carrentalapp;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.math.BigDecimal;

/**
 * Controller class for managing car details editing functionality.
 */
public class CarDetailsController {

    @FXML
    private Label carIdLabel;

    @FXML
    private TextField brandTextField;

    @FXML
    private TextField modelTextField;

    @FXML
    private Label engineIdLabel;

    @FXML
    private TextField engineNameTextField;

    @FXML
    private TextField powerTextField;

    @FXML
    private TextField fuelTypeTextField;

    @FXML
    private TextField priceTextField;

    private Stage stage;

    private Car car;
    private Engine engine;

    /**
     * Sets the initial data for the form based on the provided car entity.
     *
     * @param car The car entity to populate the form with.
     */
    public void setCar(Car car) {
        carIdLabel.setText(String.valueOf(car.getId()));
        brandTextField.setText(car.getBrand());
        modelTextField.setText(car.getModel());
        engineIdLabel.setText(String.valueOf(car.getEngine().getId()));
        engineNameTextField.setText(car.getEngine().getName());
        powerTextField.setText(String.valueOf(car.getEngine().getPower()));
        fuelTypeTextField.setText(car.getEngine().getFueltype());
        priceTextField.setText(String.valueOf(car.getPrice()));
        this.car = car;
        engine = car.getEngine();
    }

    /**
     * Sets the stage (window) for this controller.
     *
     * @param stage The stage to set.
     */
    public void setStage(Stage stage) {
        this.stage = stage;
    }

    /**
     * Handles the cancel button action to close the window/stage.
     */
    public void handleCancelButtonAction() {
        stage.close();
    }

    /**
     * Handles the save button action to validate and save changes to the car and its engine.
     * Shows alerts for validation errors or successful save.
     */
    public void handleSaveButtonAction() {
        String brand = getBrand();
        String model = getModel();
        String price = getPrice();

        if (brand.isEmpty() || model.isEmpty() || price.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Błąd walidacji", "Wszystkie pola muszą być wypełnione.");
            return;
        }

        BigDecimal priceValue;
        try {
            priceValue = new BigDecimal(price);
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Błąd walidacji", "Cena musi być liczbą.");
            return;
        }

        String name = getEngineName();
        String power = getPower();
        String fueltype = getFuelType();

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

        car.setBrand(brand);
        car.setModel(model);
        car.setPrice(priceValue);
        engine.setName(name);
        engine.setFueltype(fueltype);
        engine.setPower(powerNumber);
        car.setEngine(engine);

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            session.merge(car);
            session.merge(engine);
            session.flush();
            transaction.commit();
            AvailableCarsController.updateCar(car);
        }

        showAlert(Alert.AlertType.INFORMATION, "Sukces", "Dane zostały pomyślnie zaktualizowane.");

        stage.close();
    }

    /**
     * Handles the delete button action to delete the current car entity.
     * Shows alert for successful deletion.
     */
    public void handleDeleteButtonAction() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            session.remove(car);
            session.flush();
            transaction.commit();
            AvailableCarsController.deleteCar(car.getId());
        }

        showAlert(Alert.AlertType.INFORMATION, "Sukces", "Dane zostały pomyślnie usunięte.");

        stage.close();
    }

    /**
     * Retrieves the brand from the brand text field.
     *
     * @return The brand string.
     */
    public String getBrand() {
        return brandTextField.getText();
    }

    /**
     * Retrieves the model from the model text field.
     *
     * @return The model string.
     */
    public String getModel() {
        return modelTextField.getText();
    }

    /**
     * Retrieves the engine name from the engine name text field.
     *
     * @return The engine name string.
     */
    public String getEngineName() {
        return engineNameTextField.getText();
    }

    /**
     * Retrieves the engine power from the power text field.
     *
     * @return The engine power string.
     */
    public String getPower() {
        return powerTextField.getText();
    }

    /**
     * Retrieves the fuel type from the fuel type text field.
     *
     * @return The fuel type string.
     */
    public String getFuelType() {
        return fuelTypeTextField.getText();
    }

    /**
     * Retrieves the price from the price text field.
     *
     * @return The price string.
     */
    public String getPrice() {
        return priceTextField.getText();
    }

    /**
     * Displays an alert dialog with the specified type, title, and message.
     *
     * @param alertType The type of the alert (e.g., information, error).
     * @param title     The title of the alert.
     * @param message   The message content of the alert.
     */
    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
