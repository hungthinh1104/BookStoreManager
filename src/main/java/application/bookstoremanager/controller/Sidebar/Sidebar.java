package application.bookstoremanager.controller.Sidebar;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

public class Sidebar implements Initializable {

    @FXML
    private HBox MainContainer;

    @FXML
    private AnchorPane btnBill;

    @FXML
    private AnchorPane btnBook;

    @FXML
    private AnchorPane btnCustomer;

    @FXML
    private AnchorPane btnHome;

    @FXML
    private AnchorPane btnOrder;

    @FXML
    private AnchorPane btnSetting;

    @FXML
    private AnchorPane btnThongKe;

    private AnchorPane preButton;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        preButton = btnHome;
        try {
            Parent newContent = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/UI/ContactWindow/MainWindow/MainWindow.fxml")));
            MainContainer.getChildren().add(newContent);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    protected void btnHomeOnClick() {
        try {
            Parent newContent = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/UI/ContactWindow/MainWindow/MainWindow.fxml")));
            MainContainer.getChildren().removeLast();
            MainContainer.getChildren().add(newContent);
            preButton.setStyle("-fx-background-color: white; -fx-background-radius: 6 ;");
            btnHome.setStyle("-fx-background-color: #F3F4F6; -fx-background-radius: 6 ;");
            preButton = btnHome;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    protected void btnBookOnClick() {
        try {
            Parent newContent = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/UI/ContactWindow/BookWindow/SearchBookWindow/SearchBookWindow.fxml")));
            MainContainer.getChildren().removeLast();
            MainContainer.getChildren().add(newContent);
            preButton.setStyle("-fx-background-color: white; -fx-background-radius: 6 ;");
            btnBook.setStyle("-fx-background-color: #F3F4F6; -fx-background-radius: 6 ;");
            preButton = btnBook;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    protected void btnCustomerOnClick() {
        try {
            Parent newContent = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/UI/ContactWindow/CustomerWindow/CustomerWindow.fxml")));
            MainContainer.getChildren().removeLast();
            MainContainer.getChildren().add(newContent);
            preButton.setStyle("-fx-background-color: white; -fx-background-radius: 6 ;");
            btnCustomer.setStyle("-fx-background-color: #F3F4F6; -fx-background-radius: 6 ;");
            preButton = btnCustomer;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    protected void btnBillOnClick() {
        try {
            Parent newContent = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/UI/ContactWindow/BillWindow/BillWindow.fxml")));
            MainContainer.getChildren().removeLast();
            MainContainer.getChildren().add(newContent);
            preButton.setStyle("-fx-background-color: white; -fx-background-radius: 6 ;");
            btnBill.setStyle("-fx-background-color: #F3F4F6; -fx-background-radius: 6 ;");
            preButton = btnBill;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    protected void btnThongKeOnClick() {
        try {
            Parent newContent = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/UI/ContactWindow/ReportWindow/ReportWindow.fxml")));
            MainContainer.getChildren().removeLast();
            MainContainer.getChildren().add(newContent);
            preButton.setStyle("-fx-background-color: white; -fx-background-radius: 6 ;");
            btnThongKe.setStyle("-fx-background-color: #F3F4F6; -fx-background-radius: 6 ;");
            preButton = btnThongKe;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    protected void btnOrderOnClick() {
        try {
            Parent newContent = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/UI/ContactWindow/OrderWindow/OrderWindow.fxml")));
            MainContainer.getChildren().removeLast();
            MainContainer.getChildren().add(newContent);
            preButton.setStyle("-fx-background-color: white; -fx-background-radius: 6 ;");
            btnOrder.setStyle("-fx-background-color: #F3F4F6; -fx-background-radius: 6 ;");
            preButton = btnOrder;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    protected void btnSettingOnClick() {
        try {
            Parent newContent = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/UI/ContactWindow/SettingWindow/SettingWindow/SettingWindow.fxml")));
            MainContainer.getChildren().removeLast();
            MainContainer.getChildren().add(newContent);
            preButton.setStyle("-fx-background-color: white; -fx-background-radius: 6 ;");
            btnSetting.setStyle("-fx-background-color: #F3F4F6; -fx-background-radius: 6 ;");
            preButton = btnSetting;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
