package application.bookstoremanager.controller.ContactWindow.CustomerWindow;

import application.bookstoremanager.DatabaseUtil;
import application.bookstoremanager.classdb.Hoadon;
import application.bookstoremanager.classdb.Khachhang;
import application.bookstoremanager.controller.ContactWindow.BillWindow.BillRowTable;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.net.URL;
import java.sql.Connection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;

import static application.bookstoremanager.controller.ContactWindow.BillWindow.BillAdd.showErrorDialog;
import static application.bookstoremanager.controller.ContactWindow.BookWindow.BookSearchWindow.removeDiacritics;

public class EditCustomer implements Initializable {

    @FXML
    private TextField SDTKH;

    @FXML
    private TextField TenKH;

    @FXML
    private Button btnHuy;

    @FXML
    private Button btnSua;

    private Khachhang kh;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        SDTKH.setEditable(false);
    }

    public void SetInitData(int id) {
        try{
            Connection conn = DatabaseUtil.getConnection();
            if (conn != null) {
                kh = DatabaseUtil.getKhachhangById(conn, id);
                SDTKH.setText(kh.getSoDienThoai());
                TenKH.setText(kh.getHoTen());
            }
            assert conn != null;
            conn.close();
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    @FXML
    private void setBtnSuaOnAction(ActionEvent event) {
        if(TenKH.getText() == null || Objects.equals(TenKH.getText(), "")) {
            showErrorDialog("Thông tin không hợp lệ", "Vui lòng không để trống tên");
            return;
        }
        try{
            Connection conn = DatabaseUtil.getConnection();
            if (conn != null) {
                Khachhang newkh = new Khachhang(kh.getMaKhachHang(), TenKH.getText(), kh.getSoDienThoai(), kh.getTichDiem());
                DatabaseUtil.updateKhachhang(conn,newkh);
            }
            assert conn != null;
            conn.close();
        }catch (Exception e){
            e.printStackTrace();
        }
        Stage stage = (Stage) btnSua.getScene().getWindow();
        stage.close();
    }
    @FXML
    private void setBtnhuyOnAction(ActionEvent event) {
        Stage stage = (Stage) btnSua.getScene().getWindow();
        stage.close();
    }
}
