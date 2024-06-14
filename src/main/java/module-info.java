module com.example.carrental {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.hibernate.orm.core;
    requires org.postgresql.jdbc;
    requires jakarta.persistence;
    requires java.naming;

    exports com.example.carrentalapp;
    opens com.example.carrentalapp to javafx.fxml, org.hibernate.orm.core;
}