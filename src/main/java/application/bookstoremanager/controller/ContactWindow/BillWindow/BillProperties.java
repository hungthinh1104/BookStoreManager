package application.bookstoremanager.controller.ContactWindow.BillWindow;

import application.bookstoremanager.DatabaseUtil;
import application.bookstoremanager.classdb.CtHoadon;
import application.bookstoremanager.classdb.Hoadon;
import application.bookstoremanager.classdb.Khachhang;
import application.bookstoremanager.classdb.Sach;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.net.URL;
import java.sql.Connection;
import java.util.List;
import java.util.ResourceBundle;

import static application.bookstoremanager.controller.ContactWindow.BookWindow.BookSearchWindow.removeDiacritics;
import static application.bookstoremanager.controller.ContactWindow.BookWindow.BookTableRow.formatCurrency;
import static application.bookstoremanager.controller.ContactWindow.BookWindow.BookTableRow.parseCurrency;

public class BillProperties implements Initializable {

    @FXML
    private Button btnExit;

    @FXML
    private VBox BillDTContainer;

    @FXML
    private Label GiamGia;

    @FXML
    private Label MaHD;

    @FXML
    private Label NgayLap;

    @FXML
    private Label SDT;

    @FXML
    private Label TenKH;

    @FXML
    private Label ThanhToan;

    @FXML
    private Label TongTien;

    private Hoadon HD;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
    }

    public void LoadData(int id) {
        try{
            Connection conn = DatabaseUtil.getConnection();
            if (conn != null) {
                List<CtHoadon> ctList = DatabaseUtil.getCtHoaDonByIdHoadon(conn, id);
                for(CtHoadon ct : ctList) {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/UI/ContactWindow/BillWindow/BillPropertiesWindow/BillInformationWindow/BillInformationWindow.fxml"));
                    Parent newContent3 = loader.load();
                    BillInformation billdt = loader.getController();
                    billdt.setData(ct);
                    BillDTContainer.getChildren().add(newContent3);
                }
            }
            assert conn != null;
            conn.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void setData(int maHD) {
        try{
            Connection conn = DatabaseUtil.getConnection();
            if (conn != null) {
                HD = DatabaseUtil.getHoadonById(conn, maHD);
                MaHD.setText(HD.getMaHoaDon().toString());
                NgayLap.setText(HD.getNgayLap().toString());
                Khachhang khachhang = DatabaseUtil.getKhachhangById(conn, HD.getMaKhachHang());
                TenKH.setText(khachhang == null ? "" : khachhang.getHoTen());
                SDT.setText(khachhang == null ? "" : khachhang.getSoDienThoai());
                GiamGia.setText(formatCurrency(HD.getGiamGia()));
                TongTien.setText(formatCurrency(HD.getTongTien()));
                ThanhToan.setText(formatCurrency(HD.getTongTien() - HD.getGiamGia()));
                LoadData(maHD);
            }
            assert conn != null;
            conn.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @FXML
    private void btnHuy_OnAction(ActionEvent event) {
        Stage stage = (Stage) btnExit.getScene().getWindow();
        stage.close();
    }
}
