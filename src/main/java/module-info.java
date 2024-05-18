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
    requires java.desktop;
    requires javafx.swing;

    opens application.bookstoremanager to javafx.fxml;
    exports application.bookstoremanager;
    exports application.bookstoremanager.controller.LoginWindow;
    opens application.bookstoremanager.controller.LoginWindow to javafx.fxml;
    exports application.bookstoremanager.controller.Sidebar;
    opens application.bookstoremanager.controller.Sidebar to javafx.fxml;
    exports application.bookstoremanager.controller.ContactWindow.MainWindow;
    opens application.bookstoremanager.controller.ContactWindow.MainWindow to javafx.fxml;
    exports application.bookstoremanager.controller.ContactWindow.MainWindow.BookCard;
    opens application.bookstoremanager.controller.ContactWindow.MainWindow.BookCard to javafx.fxml;
    exports application.bookstoremanager.controller.ContactWindow.BookWindow;
    opens application.bookstoremanager.controller.ContactWindow.BookWindow to javafx.fxml;
    exports application.bookstoremanager.classdb;
    opens application.bookstoremanager.classdb to javafx.fxml;
}