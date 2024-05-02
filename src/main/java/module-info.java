module com.bookstoremanager {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;

    opens application.bookstoremanager to javafx.fxml;
    exports application.bookstoremanager;
    exports application.bookstoremanager.controller;
    opens application.bookstoremanager.controller to javafx.fxml;
}