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
    private TextField searchTextField;

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

    private static ObservableList<RentedCar> rentedCarsList = FXCollections.observableArrayList();

    @FXML
    private void initialize() {
        rentedCarsTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        idRentColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        idClientColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getClient().getId()));
        idCarColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getCar().getId()));

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd").withZone(ZoneId.systemDefault());
        rentedDateColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getRentedDate()));
        rentedFromColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getRentedFrom()));
        rentedUntilColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getRentedUntil()));
        rentedCarsTable.setItems(rentedCarsList);
        loadRentedCars();
        setupRowClickListener();
    }

    private void loadRentedCars() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            String hql = "SELECT c FROM RentedCar c";
            Query<RentedCar> query = session.createQuery(hql, RentedCar.class);
            List<RentedCar> resultList = query.getResultList();
            rentedCarsList.addAll(resultList);
        }
    }

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

    static void addRentedCar(RentedCar car){
        rentedCarsList.add(car);
    }

    static void updateRentedCar(RentedCar rentedCar) {
        for (int i = 0; i < rentedCarsList.size(); i++) {
            if (rentedCarsList.get(i).getId() == rentedCar.getId()) {
                rentedCarsList.set(i, rentedCar);
                break;
            }
        }
    }

    static void deleteRentedCar(int RentedId) {
        rentedCarsList.removeIf(car -> car.getId() == RentedId);
    }
}
