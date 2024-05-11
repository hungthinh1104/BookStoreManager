package application.bookstoremanager.controller.ContactWindow.MainWindow;

import application.bookstoremanager.controller.ContactWindow.MainWindow.BookCard.Card1;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

public class MainWindow implements Initializable {

    @FXML
    private HBox newFeedArea;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
//        try {
//            Parent newContent = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/UI/ContactWindow/MainWindow/BookCard/Card1/Card.fxml")));
//            AnchorPane newItem = (AnchorPane) newContent;
//            newFeedArea.getChildren().add(newItem);
//            Parent newContent2 = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/UI/ContactWindow/MainWindow/BookCard/Card1/Card.fxml")));
//            AnchorPane newItem2 = (AnchorPane) newContent2;
//            newFeedArea.getChildren().add(newItem2);
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
        for(Integer i = 0; i <= 10; i++){
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/UI/ContactWindow/MainWindow/BookCard/Card1/Card.fxml"));
                Parent newContent3 = loader.load();
                Card1 card = loader.getController();
                card.setRate(i.toString());
                newFeedArea.getChildren().add(newContent3);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
