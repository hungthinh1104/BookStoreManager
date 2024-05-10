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
    requires java.sql;

    opens application.bookstoremanager to javafx.fxml;
    exports application.bookstoremanager;
    exports application.bookstoremanager.controller.LoginWindow;
    opens application.bookstoremanager.controller.LoginWindow to javafx.fxml;
    exports application.bookstoremanager.controller.Sidebar;
    opens application.bookstoremanager.controller.Sidebar to javafx.fxml;
}