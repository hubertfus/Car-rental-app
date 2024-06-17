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

/**
 * Controller class for the 'Add New Car' modal.
 */
public class AddNewCarModalController {

    // FXML annotations to inject components from FXML
    @FXML
    private TextField brandTextField;

    @FXML
    private TextField modelTextField;

    @FXML
    private TextField priceTextField;

    @FXML
    private ChoiceBox<String> engineChoiceBox;

    // Stage reference to control the modal window
    private Stage stage;

    // Static list to hold engine options
    private static ObservableList<Engine> engines = FXCollections.observableArrayList();

    /**
     * Initializes the controller class.
     * This method is automatically called after the fxml file has been loaded.
     */
    @FXML
    private void initialize() {
        // Clear existing engine choices
        engines.clear();

        // Fetch engine data from database and add to engines list
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            String hql = "FROM Engine";
            Query<Engine> query = session.createQuery(hql, Engine.class);
            List<Engine> resultList = query.getResultList();
            engines.addAll(FXCollections.observableArrayList(resultList));
        }

        // Update the engine choice box with available engines
        updateEngineChoiceBox();
    }

    /**
     * Handles the action for the 'New Engine' button.
     * Opens a modal to add a new engine to the database.
     *
     * @throws IOException if loading the FXML resource fails.
     */
    @FXML
    private void handleNewEngineButtonAction() throws IOException {
        // Load the 'Add New Engine' modal FXML and create a new scene
        FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("AddNewEngineModal.fxml"));
        Scene scene = new Scene(fxmlLoader.load());

        // Get the controller for the 'Add New Engine' modal
        AddNewEngineModalController controller = fxmlLoader.getController();

        // Set up and display the new engine stage as a modal dialog
        Stage engineStage = new Stage();
        engineStage.setScene(scene);
        engineStage.setTitle("Nowy silnik");
        engineStage.initStyle(StageStyle.DECORATED);
        engineStage.initModality(Modality.APPLICATION_MODAL);
        engineStage.setResizable(false);

        // Pass the stage reference to the controller
        controller.setStage(engineStage);

        // Show the modal and wait for it to close
        engineStage.showAndWait();

        // Update the engine choice box with any new engines added
        updateEngineChoiceBox();
    }

    /**
     * Handles the submit button action.
     * Validates input fields and adds a new car to the database.
     */
    @FXML
    private void handleSubmitButtonAction() {
        // Retrieve input values from text fields
        String brand = getBrand();
        String model = getModel();
        String price = getPrice();
        String engineStr = getEngine();

        // Validate input fields are not empty and engine is selected
        if (brand.isEmpty() || model.isEmpty() || price.isEmpty() || engineStr == null) {
            showAlert(Alert.AlertType.ERROR, "Validation Error", "All fields must be filled.");
            return;
        }

        // Attempt to parse price as BigDecimal, show error if invalid
        BigDecimal priceValue;
        try {
            priceValue = new BigDecimal(price);
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Validation Error", "Price must be a number.");
            return;
        }

        // Find selected engine by string, show error if not found
        Engine selectedEngine = getEngineByString(engineStr);
        if (selectedEngine == null) {
            showAlert(Alert.AlertType.ERROR, "Validation Error", "Selected engine does not exist.");
            return;
        }

        // Create new car object with input values
        Car newCar = new Car(brand, model, selectedEngine, priceValue);

        // Persist new car object to database and add to available cars list
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

    /**
     * Displays an alert dialog to the user.
     *
     * @param alertType The type of alert (e.g., INFORMATION, ERROR).
     * @param title     The title of the alert dialog.
     * @param message   The message to display in the alert dialog.
     */
    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    /**
     * Adds a new engine to the static list of engines.
     *
     * @param engine The engine to add.
     */
    public static void addEngine(Engine engine) {
        engines.add(engine);
    }

    /**
     * Updates the engine choice box with details from the engines list.
     */
    private void updateEngineChoiceBox() {
        // Convert engine details to a string list for the choice box
        List<String> engineDetails = engines.stream()
                .map(engine -> engine.getId() + " " + engine.getName() + " " + engine.getPower() + " " + engine.getFueltype())
                .collect(Collectors.toList());

        // Set items in the choice box with the new engine details
        engineChoiceBox.setItems(FXCollections.observableArrayList(engineDetails));
    }

    // Getter methods for retrieving text from text fields
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

    /**
     * Retrieves an Engine object by its string representation.
     *
     * @param str The string containing the engine ID.
     * @return The corresponding Engine object or null if not found.
     */
    private Engine getEngineByString(String str) {
        // Extract ID from string and find matching engine
        String idStr = str.split(" ")[0];
        Integer id = Integer.parseInt(idStr);
        for (Engine engine : engines) {
            if (engine.getId().equals(id)) {
                return engine;
            }
        }
        return null;
    }

    /**
     * Sets the stage reference for this controller.
     *
     * @param stage The stage to set.
     */
    public void setStage(Stage stage) {
        this.stage = stage;
    }
}
