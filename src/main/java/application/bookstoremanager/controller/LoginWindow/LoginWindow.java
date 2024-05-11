package application.bookstoremanager.controller.LoginWindow;

import application.bookstoremanager.DatabaseUtil;
import application.bookstoremanager.classdb.Nguoidung;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.util.Base64;
import java.util.List;
import java.util.ResourceBundle;

public class LoginWindow implements Initializable {

    @FXML
    private Button btnlogin;

    @FXML
    private Label messageError;

    @FXML
    private PasswordField password;

    @FXML
    private TextField username;

    @FXML
    private AnchorPane MainPane;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        password.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                Login();
            }
        });
        username.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                Login();
            }
        });
    }

    @FXML
    protected String HashPassword(String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = digest.digest(password.getBytes());
            return Base64.getEncoder().encodeToString(hashBytes);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }

    @FXML
    protected void LoginSuccess() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/UI/Sidebar/Sidebar.fxml"));
            Scene scene = new Scene(loader.load());
            Stage stage = (Stage) MainPane.getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            // Handle FXML loading exception
            System.err.println("Error loading FXML file: " + e.getMessage());
        }
    }

    @FXML
    protected void CheckCorrectAccount(String username, String password) {
        try {
            Connection conn = DatabaseUtil.getConnection();
            if (conn != null) {
                List<Nguoidung> users = DatabaseUtil.getAllNguoidung(conn);
                boolean flag = users.stream().anyMatch(user -> user.getTenDangNhap().equals(username) && user.getMatKhau().equals(password));
                if (flag) {
                    messageError.setText("Đăng nhập thành công");
                    LoginSuccess();
                } else {
                    messageError.setText("Tài khoản hoặc mât khẩu không chính xác");
                }
            } else {
                messageError.setText("Lỗi Database");
            }
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    @FXML
    protected void Login() {
        String usernamelg = username.getText();
        String passwordlg = password.getText();
        if (usernamelg != null && !usernamelg.isEmpty()) {
            if (passwordlg != null && !passwordlg.isEmpty()) {
                CheckCorrectAccount(usernamelg, passwordlg);
            } else {
                messageError.setText("Vui lòng nhập mật khẩu!");
            }
        } else {
            if (passwordlg != null && !passwordlg.isEmpty()) {
                messageError.setText("Vui lòng nhập tài khoản!");
            } else {
                messageError.setText("Vui lòng nhập tài khoản và mật khẩu!");

            }
        }
    }

}