package application.bookstoremanager.controller.ContactWindow.SettingWindow;

import application.bookstoremanager.DatabaseUtil;
import application.bookstoremanager.GlobalVariable;
import application.bookstoremanager.classdb.Nguoidung;
import application.bookstoremanager.classdb.Thamso;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.util.Objects;
import java.util.ResourceBundle;

import static application.bookstoremanager.controller.ContactWindow.BillWindow.BillAdd.showErrorDialog;
import static application.bookstoremanager.controller.ContactWindow.SettingWindow.CreateAccount.HashPassword;

public class SettingWindow implements Initializable {

    @FXML
    private PasswordField MKCu;

    @FXML
    private PasswordField MKMoi;

    @FXML
    private VBox MainContainer;

    @FXML
    private PasswordField NLMKMoi;

    @FXML
    private Button SuaDoi;

    @FXML
    private Button XacNhan;

    @FXML
    private Button btnThemTaiKhoan;

    private boolean isEdit = true;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        SuaDoi.setStyle("-fx-background-color: #1e77fc;");
        MKCu.setEditable(false);
        MKMoi.setEditable(false);
        NLMKMoi.setEditable(false);
        XacNhan.setVisible(false);
        btnThemTaiKhoan.setVisible(GlobalVariable.User.getMaPhanQuyen() == 1);
    }

    @FXML
    private void btnEdit_OnAction() {
        if(isEdit) {
            MKCu.setEditable(true);
            MKMoi.setEditable(true);
            NLMKMoi.setEditable(true);
            isEdit = false;
            SuaDoi.setText("Hủy");
            XacNhan.setVisible(true);
            SuaDoi.setStyle("-fx-background-color: #f55442;");
        }
        else {
            SuaDoi.setText("Cập nhật");
            SuaDoi.setStyle("-fx-background-color: #1e77fc;");
            isEdit = true;
            MKCu.setEditable(false);
            MKMoi.setEditable(false);
            NLMKMoi.setEditable(false);
            XacNhan.setVisible(false);
        }
    }

    @FXML
    protected void btnChangeToOtherPage_OnAction() {
        try {
            Parent newContent = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/UI/ContactWindow/SettingWindow/RegulationWindow/RegulationWindow.fxml")));
            Parent pane = MainContainer.getParent();
            HBox anchorPane = (HBox) pane;
            anchorPane.getChildren().removeLast();
            anchorPane.getChildren().add(newContent);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @FXML
    private void btnconfirmOnAction() {
        if(!isEdit) {
            if(MKCu.getText().isEmpty() || MKCu.getText() == null) {
                showErrorDialog("Thông tin không hợp lệ", "Vui lòng nhập mật khẩu cũ");
                return;
            }
            if(MKMoi.getText().isEmpty() || MKMoi.getText() == null) {
                showErrorDialog("Thông tin không hợp lệ", "Vui lòng nhập mật khẩu mới");
                return;
            }
            if(NLMKMoi.getText().isEmpty() || NLMKMoi.getText() == null) {
                showErrorDialog("Thông tin không hợp lệ", "Vui lòng nhập lại mật khẩu mới");
                return;
            }
            if(!MKMoi.getText().equals(NLMKMoi.getText())) {
                showErrorDialog("Thông tin không hợp lệ", "Mật khẩu nhập lại không khớp");
                return;
            }
            try{
                Connection conn = DatabaseUtil.getConnection();
                if (conn != null) {
                    if(!Objects.equals(HashPassword(MKCu.getText()), GlobalVariable.User.getMatKhau())) {
                        showErrorDialog("Thông tin không hợp lệ", "Mật khẩu cũ không trùng khớp");
                        return;
                    }
                    Nguoidung user = GlobalVariable.User;
                    Nguoidung newND = new Nguoidung(user.getTenDangNhap(), HashPassword(MKMoi.getText()), user.getMaPhanQuyen(), user.getPhanQuyen(),user.getHoTen());
                    DatabaseUtil.updateNguoidung(conn,newND);
                }
                assert conn != null;
                conn.close();
            }catch (Exception e){
                e.printStackTrace();
            }
            MKCu.setEditable(false);
            MKMoi.setEditable(false);
            NLMKMoi.setEditable(false);
            XacNhan.setVisible(false);
            SuaDoi.setText("Đổi mật khẩu");
            SuaDoi.setStyle("-fx-background-color: #1e77fc;");
            MKCu.setText("");
            MKMoi.setText("");
            NLMKMoi.setText("");
            isEdit = true;
        }

    }

    @FXML
    private void btnTaoTaiKhoan_OnAction(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/UI/ContactWindow/SettingWindow/SettingWindow/AddAccountWindow/AddAccountWindow.fxml"));
            Parent parent = loader.load();
            Stage stage = new Stage();
            Scene scene = new Scene(parent);
            stage.setScene(scene);
            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner((Stage) btnThemTaiKhoan.getScene().getWindow());
            stage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
