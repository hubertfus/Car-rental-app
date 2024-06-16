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

    Car car;
    Engine engine;
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
    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void handleCancelButtonAction() {
        stage.close();
    }

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
    public void handleDeleteButtonAction() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            session.remove(car);
            session.flush();
            transaction.commit();
            AvailableCarsController.deleteCar(car.getId());
        }

        AvailableCarsController.deleteCar(car.getId());
        showAlert(Alert.AlertType.INFORMATION, "Sukces", "Dane zostały pomyślnie usunięte.");

        stage.close();
    }

    public String  getBrand() {
        return brandTextField.getText();
    }

    public String getModel() {
        return modelTextField.getText();
    }

    public String getEngineName() {
        return engineNameTextField.getText();
    }

    public String getPower() {
        return powerTextField.getText();
    }

    public String getFuelType() {
        return fuelTypeTextField.getText();
    }

    public String getPrice() {
        return priceTextField.getText();
    }
}
