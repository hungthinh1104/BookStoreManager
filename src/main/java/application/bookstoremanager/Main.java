package application.bookstoremanager;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Main  extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/UI/LoginWindow/LoginWindow.fxml"));
            Scene scene = new Scene(loader.load());
            stage.setTitle("Bookstore Manager Login");
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            // Handle FXML loading exception
            System.err.println("Error loading FXML file: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        launch();
    }
}
