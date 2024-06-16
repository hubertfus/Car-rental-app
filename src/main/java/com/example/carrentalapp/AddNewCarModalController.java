package com.example.carrentalapp;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

public class AddNewCarModalController {

    @FXML
    private TextField brandTextField;

    @FXML
    private TextField modelTextField;

    @FXML
    private TextField priceTextField;

    @FXML
    private ChoiceBox<String> engineChoiceBox;

    private Stage stage;

    private static ObservableList<Engine> engines = FXCollections.observableArrayList();

    @FXML
    private void initialize() {
        engines.clear();
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            String hql = "FROM Engine";
            Query<Engine> query = session.createQuery(hql, Engine.class);
            List<Engine> resultList = query.getResultList();
            engines.addAll(FXCollections.observableArrayList(resultList));
        }

        updateEngineChoiceBox();
    }

    @FXML
    private void handleNewEngineButtonAction() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("AddNewEngineModal.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        AddNewEngineModalController controller = fxmlLoader.getController();

        Stage engineStage = new Stage();
        engineStage.setScene(scene);
        engineStage.setTitle("Nowy silnik");
        engineStage.initStyle(StageStyle.DECORATED);
        engineStage.initModality(Modality.APPLICATION_MODAL);
        engineStage.setResizable(false);

        controller.setStage(engineStage);

        engineStage.showAndWait();

        updateEngineChoiceBox();
    }

    @FXML
    private void handleSubmitButtonAction() {
        String brand = getBrand();
        String model = getModel();
        String price = getPrice();
        String engineStr = getEngine();

        if (brand.isEmpty() || model.isEmpty() || price.isEmpty() || engineStr == null) {
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

        Engine selectedEngine = getEngineByString(engineStr);
        if (selectedEngine == null) {
            showAlert(Alert.AlertType.ERROR, "Błąd walidacji", "Wybrany silnik nie istnieje.");
            return;
        }

        Car newCar = new Car(brand, model, selectedEngine, priceValue);

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            session.persist(newCar);
            session.flush();
            transaction.commit();
            AvailableCarsController.addCar(newCar);
        }

        showAlert(Alert.AlertType.INFORMATION, "Sukces", "Dane zostały pomyślnie dodane.");

        stage.close();
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public static void addEngine(Engine engine) {
        engines.add(engine);
    }

    private void updateEngineChoiceBox() {
        List<String> engineDetails = engines.stream()
                .map(engine -> engine.getId() + " " + engine.getName() + " " + engine.getPower() + " " + engine.getFueltype())
                .collect(Collectors.toList());

        engineChoiceBox.setItems(FXCollections.observableArrayList(engineDetails));
    }

    public String getBrand() {
        return brandTextField.getText();
    }

    public String getModel() {
        return modelTextField.getText();
    }

    public String getPrice() {
        return priceTextField.getText();
    }

    public String getEngine() {
        return engineChoiceBox.getValue();
    }

    private Engine getEngineByString(String str) {
        String idStr = str.split(" ")[0];
        Integer id = Integer.parseInt(idStr);
        for (Engine engine : engines) {
            if (engine.getId().equals(id)) {
                return engine;
            }
        }
        return null;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }


}
