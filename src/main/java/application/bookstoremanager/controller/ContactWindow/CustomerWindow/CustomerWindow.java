package application.bookstoremanager.controller.ContactWindow.CustomerWindow;

import application.bookstoremanager.DatabaseUtil;
import application.bookstoremanager.classdb.Khachhang;
import application.bookstoremanager.classdb.Sach;
import application.bookstoremanager.controller.ContactWindow.BookWindow.BookTableRow;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;

import static application.bookstoremanager.controller.ContactWindow.BookWindow.BookSearchWindow.removeDiacritics;

public class CustomerWindow implements Initializable {

    @FXML
    private VBox CustomerContainer;

    @FXML
    private Button btnTimKiem;

    @FXML
    private Button ThemKh;

    @FXML
    private TextField searchTKH;

    @FXML
    private TextField searchSDT;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        LoadData("","");
    }
    public void LoadData(String searchTen, String searchSDT) {
        try{
            Connection conn = DatabaseUtil.getConnection();
            if (conn != null) {
                int stt = 0;
                List<Khachhang> khList = DatabaseUtil.getAllKhachhang(conn);
                CustomerContainer.getChildren().clear();
                for(Khachhang kh : khList) {
                    if(!searchTen.isEmpty() && !removeDiacritics(kh.getHoTen().toLowerCase()).contains(removeDiacritics(searchTen.toLowerCase()))) continue;
                    if(!searchSDT.isEmpty() && !kh.getSoDienThoai().contains(searchSDT)) continue;
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/UI/ContactWindow/CustomerWindow/CustomerTableRow/CustomerTableRow.fxml"));
                    Parent newContent3 = loader.load();
                    CustomerTableRow book = loader.getController();
                    book.setData(kh);
                    CustomerContainer.getChildren().add(newContent3);
                }
            }
            assert conn != null;
            conn.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @FXML
    void btnTimKiemOnAction(ActionEvent event) {
        LoadData(searchTKH.getText(),searchSDT.getText());
    }

    @FXML
    void btnThemKhOnAction(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/UI/ContactWindow/CustomerWindow/AddCustomerWindow/AddCustomerWindow.fxml"));
            Parent parent = loader.load();
            Stage stage = new Stage();
            Scene scene = new Scene(parent);
            stage.setScene(scene);
            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner((Stage) ThemKh.getScene().getWindow());
            stage.showAndWait();
            System.out.println("load data");
            LoadData(searchTKH.getText(),searchSDT.getText());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
