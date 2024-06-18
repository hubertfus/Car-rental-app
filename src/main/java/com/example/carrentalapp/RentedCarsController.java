/**
 * Controller class for managing rented cars view and operations.
 * This controller initializes the table with rented cars, handles addition,
 * deletion, and updating of rented car entries. It also supports
 * double-click functionality to view details of a selected rented car.
 */
package com.example.carrentalapp;

import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.hibernate.Session;
import org.hibernate.query.Query;

import java.io.IOException;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class RentedCarsController {

    @FXML
    private TableView<RentedCar> rentedCarsTable;

    @FXML
    private TableColumn<RentedCar, Integer> idRentColumn;

    @FXML
    private TableColumn<RentedCar, Integer> idClientColumn;

    @FXML
    private TableColumn<RentedCar, Integer> idCarColumn;

    @FXML
    private TableColumn<RentedCar, String> rentedDateColumn;

    @FXML
    private TableColumn<RentedCar, String> rentedFromColumn;

    @FXML
    private TableColumn<RentedCar, String> rentedUntilColumn;

    // Observable list to hold rented cars data
    private static ObservableList<RentedCar> rentedCarsList = FXCollections.observableArrayList();

    /**
     * Initializes the controller class. Sets up table columns and loads
     * rented car data from the database.
     */
    @FXML
    private void initialize() {
        rentedCarsTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        idRentColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        idClientColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getClient().getId()));
        idCarColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getCar().getId()));

        rentedDateColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getRentedDate()));
        rentedFromColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getRentedFrom()));
        rentedUntilColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getRentedUntil()));
        rentedCarsTable.setItems(rentedCarsList);
        loadRentedCars();
        setupRowClickListener();
    }

    /**
     * Loads rented car data from the database using Hibernate.
     */
    private void loadRentedCars() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            String hql = "SELECT c FROM RentedCar c";
            Query<RentedCar> query = session.createQuery(hql, RentedCar.class);
            List<RentedCar> resultList = query.getResultList();
            rentedCarsList.addAll(resultList);
        }
    }

    /**
     * Handles adding a new rent entry. Opens a modal window for adding
     * a new rented car record.
     *
     * @throws IOException If there is an error loading the FXML file.
     */
    @FXML
    private void handleAddNewRent() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("AddNewRentModal.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        AddNewRentModalController controller = fxmlLoader.getController();
        Stage stage = new Stage();
        controller.setStage(stage);
        stage.setScene(scene);
        stage.setTitle("Nowy wynajem");
        stage.initStyle(StageStyle.DECORATED);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setResizable(false);
        stage.show();
    }

    /**
     * Sets up a double-click listener on rows of the rented cars table.
     * Opens details of the selected rented car in a modal window.
     */
    private void setupRowClickListener() {
        rentedCarsTable.setRowFactory(tv -> {
            TableRow<RentedCar> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (!row.isEmpty())) {
                    RentedCar rowData = row.getItem();
                    try {
                        openRentedCarDetails(rowData);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            });
            return row;
        });
    }

    /**
     * Opens a modal window to display details of a rented car.
     *
     * @param rentedCar The rented car object whose details are to be displayed.
     * @throws IOException If there is an error loading the FXML file.
     */
    private void openRentedCarDetails(RentedCar rentedCar) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("RentedCarDetailsModal.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        RentedCarDetailsModalController controller = fxmlLoader.getController();
        Stage stage = new Stage();
        controller.setRentedCar(rentedCar);
        controller.setStage(stage);
        stage.setScene(scene);
        stage.setTitle("Szczegóły wynajmu");
        stage.initStyle(StageStyle.DECORATED);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setResizable(false);
        stage.show();
    }

    /**
     * Adds a rented car to the observable list.
     *
     * @param car The rented car to be added.
     */
    static void addRentedCar(RentedCar car){
        rentedCarsList.add(car);
    }

    /**
     * Updates a rented car in the observable list.
     *
     * @param rentedCar The rented car to be updated.
     */
    static void updateRentedCar(RentedCar rentedCar) {
        for (int i = 0; i < rentedCarsList.size(); i++) {
            if (rentedCarsList.get(i).getId() == rentedCar.getId()) {
                rentedCarsList.set(i, rentedCar);
                break;
            }
        }
    }

    /**
     * Deletes a rented car from the observable list.
     *
     * @param RentedId The ID of the rented car to be deleted.
     */
    static void deleteRentedCar(int RentedId) {
        rentedCarsList.removeIf(car -> car.getId() == RentedId);
    }
}
