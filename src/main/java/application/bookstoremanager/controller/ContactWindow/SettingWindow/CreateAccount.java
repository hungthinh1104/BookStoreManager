package application.bookstoremanager.controller.ContactWindow.SettingWindow;

import application.bookstoremanager.DatabaseUtil;
import application.bookstoremanager.GlobalVariable;
import application.bookstoremanager.classdb.Khachhang;
import application.bookstoremanager.controller.ContactWindow.BillWindow.DetailPaneBill;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.util.Base64;
import java.util.Objects;
import java.util.ResourceBundle;

import static application.bookstoremanager.controller.ContactWindow.BillWindow.BillAdd.showErrorDialog;
import static application.bookstoremanager.controller.ContactWindow.BookWindow.BookTableRow.parseCurrency;

public class CreateAccount implements Initializable {

    @FXML
    private TextField HoTen;

    @FXML
    private PasswordField MK;

    @FXML
    private PasswordField NhapLaiMK;

    @FXML
    private TextField TenDN;

    @FXML
    private ComboBox<String> Vaitro;

    @FXML
    private Button btnHuy;

    @FXML
    private Button btnThemTaiKhoan;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Vaitro.getItems().add("Quản lý");
        Vaitro.getItems().add("Nhân viên");
    }

    @FXML
    void btnThemTaiKhoanOnAction(ActionEvent event) {
        if(TenDN.getText().isEmpty() || TenDN.getText() == null) {
            showErrorDialog("Thông tin không hợp lệ", "Vui lòng nhập tên tài khoản");
            return;
        }
        if(MK.getText().isEmpty() || MK.getText() == null) {
            showErrorDialog("Thông tin không hợp lệ", "Vui lòng nhập mật khẩu");
            return;
        }
        if(NhapLaiMK.getText().isEmpty() || NhapLaiMK.getText() == null) {
            showErrorDialog("Thông tin không hợp lệ", "Vui lòng nhập lại mật khẩu");
            return;
        }
        if(HoTen.getText().isEmpty() || HoTen.getText() == null) {
            showErrorDialog("Thông tin không hợp lệ", "Vui lòng nhập họ tên");
            return;
        }
        if(Vaitro.getValue().isEmpty() || Vaitro.getValue() == null) {
            showErrorDialog("Thông tin không hợp lệ", "Vui lòng chọn vai trò");
            return;
        }
        if(!MK.getText().equals(NhapLaiMK.getText())) {
            showErrorDialog("Thông tin không hợp lệ", "Mật khẩu nhập lại không khớp");
            return;
        }
        try{
            Connection conn = DatabaseUtil.getConnection();
            if (conn != null) {
                DatabaseUtil.createNguoidung(conn, TenDN.getText(), HashPassword(MK.getText()), HoTen.getText(), Vaitro.getValue().equals("Admin") ? 1 : 2);
            }
            assert conn != null;
            conn.close();
        }catch (Exception e){
            e.printStackTrace();
        }
        Stage stage = (Stage) btnThemTaiKhoan.getScene().getWindow();
        stage.close();
    }

    @FXML
    void btnHuyOnAction(ActionEvent event) {
        Stage stage = (Stage) btnThemTaiKhoan.getScene().getWindow();
        stage.close();
    }

    public static String HashPassword(String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = digest.digest(password.getBytes());
            return Base64.getEncoder().encodeToString(hashBytes);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }
}
