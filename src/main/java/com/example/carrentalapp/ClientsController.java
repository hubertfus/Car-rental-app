package com.example.carrentalapp;

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

/**
 * Controller class for managing the list of clients in the car rental application.
 */
public class ClientsController {


    @FXML
    private TableView<Client> tableView; // TableView to display clients.

    @FXML
    private TableColumn<Client, Integer> idColumn; // TableColumn for client IDs.

    @FXML
    private TableColumn<Client, String> firstNameColumn; // TableColumn for client first names.

    @FXML
    private TableColumn<Client, String> lastNameColumn; // TableColumn for client last names.

    @FXML
    private TableColumn<Client, String> emailColumn; // TableColumn for client emails.

    @FXML
    private Button addButton; // Button to add a new client.

    private static ObservableList<Client> clients = FXCollections.observableArrayList(); // List of clients.


    /**
     * Initializes the controller class.
     * Sets up the table columns and loads the clients into the table view.
     */
    @FXML
    private void initialize() {
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        firstNameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getFirstname()));
        lastNameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getLastname()));
        emailColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getEmail()));

        loadClients(); // Load clients from the database.
        setupRowClickListener(); // Set up click listener for table rows.
        tableView.setItems(clients); // Set items in the table view.
    }

    /**
     * Loads clients from the database and adds them to the observable list.
     */
    private void loadClients(){
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            String hql = "SELECT c FROM Client c";
            Query<Client> query = session.createQuery(hql, Client.class);
            List<Client> resultList = query.getResultList();
            clients.addAll(resultList);
        }
    }

    /**
     * Sets up a click listener for each row in the table view.
     * On double-click, opens the client details modal.
     */
    private void setupRowClickListener() {
        tableView.setRowFactory(tv -> {
            TableRow<Client> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (!row.isEmpty())) {
                    Client rowData = row.getItem();
                    try {
                        openClientDetails(rowData);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            });
            return row;
        });
    }

    /**
     * Opens the client details modal for a given client.
     *
     * @param client The client whose details are to be edited.
     * @throws IOException If loading the FXML fails.
     */
    private void openClientDetails(Client client) throws IOException {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("ClientDetailsModal.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            ClientDetailsModalController controller = fxmlLoader.getController();
            controller.setClient(client);
            Stage stage = new Stage();
            controller.setStage(stage);
            stage.setScene(scene);
            stage.setTitle("Client Details");
            stage.initStyle(StageStyle.DECORATED);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setResizable(false);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Handles adding a new client.
     * Opens the add new client modal.
     *
     * @throws IOException If loading the FXML fails.
     */
    @FXML
    private void handleAddNewClient() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("AddNewClientModal.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        AddNewClientModalController controller = fxmlLoader.getController();
        Stage stage = new Stage();
        controller.setStage(stage);
        stage.setScene(scene);
        stage.setTitle("Add New Client");
        stage.initStyle(StageStyle.DECORATED);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setResizable(false);
        stage.show();
    }

    /**
     * Adds a new client to the observable list and updates the table view.
     *
     * @param client The new client to add.
     */
    public static void addClient(Client client){
        clients.add(client);
    }

    /**
     * Updates an existing client's details in the observable list and table view.
     *
     * @param client The client with updated details.
     */
    static void updateClient(Client client) {
        for (int i = 0; i < clients.size(); i++) {
            if (clients.get(i).getId() == client.getId()) {
                clients.set(i, client);
                break;
            }
        }
    }

    /**
     * Deletes a client from the observable list and updates the table view.
     *
     * @param clientId The ID of the client to delete.
     */
    static void deleteClient(int clientId) {
        clients.removeIf(client -> client.getId() == clientId);
    }

    /**
     * Retrieves the observable list of clients.
     *
     * @return An ObservableList containing the list of clients.
     */

    public static Client getClientByClientID(int clientID){
        for (Client client : clients) {
            if (client.getId() == clientID) {
                return client;
            }
        }
        return null;
    }

    public static ObservableList getClients() {
        return clients; // Returns the static list of clients.
    }
}
