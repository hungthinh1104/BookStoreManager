package application.bookstoremanager.controller.ContactWindow.CustomerWindow;

import application.bookstoremanager.DatabaseUtil;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.sql.Connection;
import java.util.Objects;

public class AddCustomer {

    @FXML
    private TextField SDT;

    @FXML
    private TextField TenKH;

    @FXML
    private Button btnThem;

    @FXML
    private void btnThemOnAction(ActionEvent event) {
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
}
