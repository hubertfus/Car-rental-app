package com.example.carrentalapp;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.hibernate.Session;
import org.hibernate.query.Query;

import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.time.ZoneId;
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

    private ObservableList<RentedCar> rentedCarsList = FXCollections.observableArrayList();

    @FXML
    private void initialize() {
        rentedCarsTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        idRentColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        idClientColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getIdclient().getId()));
        idCarColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getCar().getId()));

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").withZone(ZoneId.systemDefault());
        rentedDateColumn.setCellValueFactory(cellData -> new SimpleStringProperty(formatter.format(cellData.getValue().getRentedDate())));
        rentedFromColumn.setCellValueFactory(cellData -> new SimpleStringProperty(formatter.format(cellData.getValue().getRentedFrom())));
        rentedUntilColumn.setCellValueFactory(cellData -> new SimpleStringProperty(formatter.format(cellData.getValue().getRentedUntil())));
        rentedCarsTable.setItems(rentedCarsList);
        loadRentedCars();

    }

    private void loadRentedCars() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            String hql = "SELECT c FROM RentedCar c";
            Query<RentedCar> query = session.createQuery(hql, RentedCar.class);
            List<RentedCar> resultList = query.getResultList();
            for (RentedCar car : resultList) {
                System.out.println(car.getIdclient());
            }
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
}
