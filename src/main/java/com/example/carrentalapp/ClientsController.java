package com.example.carrentalapp;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.hibernate.Session;
import org.hibernate.query.Query;

import java.io.IOException;
import java.util.List;

public class ClientsController {

    @FXML
    private TextField searchField;

    @FXML
    private TableView<Client> tableView;

    @FXML
    private TableColumn<Client, Integer> idColumn;

    @FXML
    private TableColumn<Client, String> firstNameColumn;

    @FXML
    private TableColumn<Client, String> lastNameColumn;

    @FXML
    private TableColumn<Client, String> emailColumn;

    @FXML
    private Button addButton;
    private ObservableList<Client> clients = FXCollections.observableArrayList();

    @FXML
    private void initialize() {
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        firstNameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getFirstname()));
        lastNameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getLastname()));
        emailColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getEmail()));
        tableView.setItems(clients);
        loadClients();
    }
    private void loadClients(){
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            String hql = "SELECT c FROM Client c";
            Query<Client> query = session.createQuery(hql, Client.class);
            List<Client> resultList = query.getResultList();
            clients.addAll(resultList);
        }
    }

    @FXML
    private void handleAddNewClient() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("AddNewClientModal.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        AddNewClientModalController controller = fxmlLoader.getController();
        Stage stage = new Stage();
        controller.setStage(stage);
        stage.setScene(scene);
        stage.setTitle("Nowy klient");
        stage.initStyle(StageStyle.DECORATED);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setResizable(false);
        stage.show();
    }

}
