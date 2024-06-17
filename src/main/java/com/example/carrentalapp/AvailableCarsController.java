package com.example.carrentalapp;

import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.hibernate.Session;
import org.hibernate.query.Query;

import java.io.IOException;
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

    private static ObservableList<Car> cars = FXCollections.observableArrayList();

    @FXML
    private void initialize() {
        initializeTable();
        loadAvailableCars();
        setupRowClickListener();
    }

    private void initializeTable() {
        idCarColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getId()));
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
                    "  SELECT 1 FROM RentedCar rc WHERE rc.car = c " +
                    ")ORDER BY c.id ASC";
            Query<Car> query = session.createQuery(hql, Car.class);
            List<Car> resultList = query.getResultList();
            cars.addAll(resultList);
        }
    }

    private void setupRowClickListener() {
        availableCarsTable.setRowFactory(tv -> {
            TableRow<Car> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (!row.isEmpty())) {
                    Car rowData = row.getItem();
                    openCarDetails(rowData);
                }
            });
            return row;
        });
    }

    private void openCarDetails(Car car) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("CarDetailsView.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            CarDetailsController controller = fxmlLoader.getController();
            Stage stage = new Stage();
            controller.setCar(car);
            controller.setStage(stage);
            stage.setScene(scene);
            stage.setTitle("Szczegóły samochódu");
            stage.initStyle(StageStyle.DECORATED);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setResizable(false);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static void addCar(Car car) {
        cars.add(car);
    }

    static void updateCar(Car car) {
        for (int i = 0; i < cars.size(); i++) {
            if (cars.get(i).getId() == car.getId()) {
                cars.set(i, car);
                break;
            }
        }
    }

    static void deleteCar(int carId) {
        cars.removeIf(car -> car.getId() == carId);
    }

    static Car getCarByCarID(int carId){
        for (Car car : cars) {
            if (car.getId() == carId) {
                return car;
            }
        }
        return null;
    }

    @FXML
    private void handleAddNewCar() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("AddNewCarModal.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        AddNewCarModalController controller = fxmlLoader.getController();
        Stage stage = new Stage();
        controller.setStage(stage);
        stage.setScene(scene);
        stage.setTitle("Nowy samochód");
        stage.initStyle(StageStyle.DECORATED);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setResizable(false);
        stage.show();
    }
    static ObservableList getCars(){
        return cars;
    }
}
