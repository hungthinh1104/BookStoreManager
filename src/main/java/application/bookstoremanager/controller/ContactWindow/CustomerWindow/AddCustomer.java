package application.bookstoremanager.controller.ContactWindow.CustomerWindow;

import application.bookstoremanager.DatabaseUtil;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.stage.Stage;

import java.net.URL;
import java.sql.Connection;
import java.util.Objects;
import java.util.ResourceBundle;

import static application.bookstoremanager.controller.ContactWindow.BillWindow.BillAdd.showErrorDialog;


public class AddCustomer implements Initializable {

    @FXML
    private TextField SDT;

    @FXML
    private TextField TenKH;

    @FXML
    private Button btnThem;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        SDT.setTextFormatter(new TextFormatter<>(change -> {
            String newText = change.getControlNewText();
            if (newText.matches("\\d*")) {
                return change; // If the new text is numeric, accept the change
            }
            return null; // Otherwise, reject the change
        }));

    }

    @FXML
    private void btnThemOnAction(ActionEvent event) {
        if(Objects.equals(TenKH.getText(), "") || TenKH == null) {
            showErrorDialog("Thông tin không hợp lệ", "Vui lòng nhập tên khách hàng");
            return;
        }
        if(Objects.equals(SDT.getText(), "") || SDT == null) {
            showErrorDialog("Thông tin không hợp lệ", "Vui lòng nhập số điện thoại khách hàng");
            return;
        }
        Connection conn = DatabaseUtil.getConnection();
        try {
            if (conn != null && !Objects.equals(TenKH.getText(), "") && !Objects.equals(SDT.getText(), "")) {
                DatabaseUtil.createKhachhang(conn, TenKH.getText(), SDT.getText(), 0);
            } else {
                System.out.println("Có giá trị null");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (conn != null) conn.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        Stage stage = (Stage) TenKH.getScene().getWindow();
        stage.close();
    }

    @FXML
    private void btnHuyOnAction(ActionEvent event) {
        Stage stage = (Stage) TenKH.getScene().getWindow();
        stage.close();
    }
}
