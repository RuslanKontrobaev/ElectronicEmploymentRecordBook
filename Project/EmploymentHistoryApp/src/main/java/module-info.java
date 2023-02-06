module ru.istu.employmenthistoryapp {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;
    requires java.sql;
    requires java.desktop;

    opens ru.istu.employmenthistoryapp to javafx.fxml;
    exports ru.istu.employmenthistoryapp;
    exports ru.istu.employmenthistoryapp.controllers;
    opens ru.istu.employmenthistoryapp.controllers to javafx.fxml;
}