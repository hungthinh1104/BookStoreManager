package application.bookstoremanager.controller.ContactWindow.MainWindow.BookCard;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;

import java.net.URL;
import java.util.ResourceBundle;

public class Card1 implements Initializable {

    @FXML
    private Label rate;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    public void setRate(String newRate) {
        rate.setText(newRate);
    }
}
