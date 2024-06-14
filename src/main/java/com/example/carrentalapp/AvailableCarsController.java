package com.example.carrentalapp;

import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import org.hibernate.Session;
import org.hibernate.query.Query;

import java.util.List;

public class AvailableCarsController {

    @FXML
    private TableView<Car> availableCarsTable;

    @FXML
    private TableColumn<Car, Integer> idCarColumn;

    @FXML
    private TableColumn<Car, String> brandColumn;

    @FXML
    private TableColumn<Car, String> modelColumn;

    @FXML
    private TableColumn<Car, Integer> idEngineColumn;

    @FXML
    private TableColumn<Car, String> nameColumn;

    @FXML
    private TableColumn<Car, Integer> powerColumn;

    @FXML
    private TableColumn<Car, String> fuelTypeColumn;

    @FXML
    private TableColumn<Car, Double> priceColumn;

    private ObservableList<Car> cars = FXCollections.observableArrayList();

    @FXML
    private void initialize() {
        initializeTable();
        loadAvailableCars();
    }

    private void initializeTable() {
        idCarColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        brandColumn.setCellValueFactory(new PropertyValueFactory<>("brand"));
        modelColumn.setCellValueFactory(new PropertyValueFactory<>("model"));

        idEngineColumn.setCellValueFactory(cellData -> {
            SimpleObjectProperty<Integer> property = new SimpleObjectProperty<>();
            property.setValue(cellData.getValue().getEngine().getId());
            return property;
        });

        nameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getEngine().getName()));
        powerColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getEngine().getPower()));
        fuelTypeColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getEngine().getFueltype()));

        priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));

        availableCarsTable.setItems(cars);
    }

    private void loadAvailableCars() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            String hql = "SELECT c FROM Car c INNER JOIN FETCH c.engine WHERE NOT EXISTS (" +
                    "  SELECT 1 FROM RentedCar rc WHERE rc.car = c" +
                    ")";
            Query<Car> query = session.createQuery(hql, Car.class);
            List<Car> resultList = query.getResultList();
            cars.addAll(resultList);
        }
    }

    // Optional: Method for handling new car addition button click
    @FXML
    private void handleAddNewCar() {
        // Logic for adding a new car
    }
}
