package application.bookstoremanager;

import application.bookstoremanager.classdb.*;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class Main extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/UI/LoginWindow/LoginWindow.fxml"));
            Scene scene = new Scene(loader.load());
            stage.setTitle("Bookstore Manager");
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error loading FXML file: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        Connection conn = DatabaseUtil.getConnection();
        try{
            String hoten = "Nguyễn Thị Mơ";
            String sdt = "0987652134";
            DatabaseUtil.createKhachhang(conn, hoten, sdt);
        }catch (Exception e){
            e.printStackTrace();
        }
        launch();
    }
}
