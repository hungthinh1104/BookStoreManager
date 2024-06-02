package application.bookstoremanager.controller.ContactWindow.CustomerWindow;

import application.bookstoremanager.classdb.Khachhang;
import application.bookstoremanager.classdb.Sach;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.kordamp.ikonli.javafx.FontIcon;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class CustomerTableRow implements Initializable {

    @FXML
    private Label MaKhachHang;

    @FXML
    private Label SoDienThoai;

    @FXML
    private Label TenKhachHang;

    @FXML
    private  Label TichDiem;

    @FXML
    private FontIcon btnedit;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        btnedit.setOnMouseClicked(event -> {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/UI/ContactWindow/CustomerWindow/ModifyCustomerWindow/ModifyCustomerWindow.fxml"));
                Parent parent = loader.load();
                EditCustomer customer = loader.getController();
                customer.SetInitData(Integer.parseInt(MaKhachHang.getText()));
                Stage stage = new Stage();
                Scene scene = new Scene(parent);
                stage.setScene(scene);
                stage.initModality(Modality.WINDOW_MODAL);
                stage.initOwner((Stage)btnedit.getScene().getWindow());
                stage.showAndWait();
                System.out.println("load data");
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    public void setData(Khachhang khachhang) {
        MaKhachHang.setText(khachhang.getMaKhachHang().toString());
        SoDienThoai.setText(khachhang.getSoDienThoai());
        TenKhachHang.setText(khachhang.getHoTen());
        TichDiem.setText(khachhang.getTichDiem().toString());
    }
}
