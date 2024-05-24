package application.bookstoremanager.controller.ContactWindow.OrderWindow;

import application.bookstoremanager.DatabaseUtil;
import application.bookstoremanager.classdb.*;
import application.bookstoremanager.controller.ContactWindow.BillWindow.BillAdd;
import application.bookstoremanager.controller.ContactWindow.BillWindow.BillInformation;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.util.List;
import java.util.ResourceBundle;

import static application.bookstoremanager.controller.ContactWindow.BookWindow.BookTableRow.formatCurrency;

public class OrderProperties implements Initializable {

    @FXML
    private Label MaDH;

    @FXML
    private Label NgayLap;

    @FXML
    private VBox OrderDTContainer;

    @FXML
    private Label SDT;

    @FXML
    private Label TenKH;

    @FXML
    private Label TienCoc;

    @FXML
    private Label TrangThai;

    @FXML
    private Button btnHuyDon;

    @FXML
    private Button btnThanhToan;

    @FXML
    private Button btnThoat;

    private Dondathang DH;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
    public void LoadData(int id) {
        try{
            Connection conn = DatabaseUtil.getConnection();
            if (conn != null) {
                List<CtDondathang> ctList = DatabaseUtil.getCtDondathangByIdDondathang(conn, id);
                for(CtDondathang ct : ctList) {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/UI/ContactWindow/OrderWindow/OrderPropertiesWindow/OrderInformationWindow/OrderInformationWindow.fxml"));
                    Parent newContent3 = loader.load();
                    OrderInfor orderdt = loader.getController();
                    orderdt.setData(ct);
                    OrderDTContainer.getChildren().add(newContent3);
                }
            }
            assert conn != null;
            conn.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    public void setData(int maDH) {
        try{
            Connection conn = DatabaseUtil.getConnection();
            if (conn != null) {
                DH = DatabaseUtil.getDondathangById(conn, maDH);
                System.out.println("Don dat hang: " + DH.getMaDonHang());
                System.out.println("Ngay lap: " + DH.getNgayLap().toString());
                MaDH.setText(String.valueOf(DH.getMaDonHang()));
                TenKH.setText(DH.getKhachHang().getHoTen());
                SDT.setText(DH.getKhachHang().getSoDienThoai());
                NgayLap.setText(DH.getNgayLap().toString());
                TienCoc.setText(formatCurrency(DH.getTongTienCoc()));
                TrangThai.setText(DH.getTrangThai());
                switch (DH.getTrangThai()) {
                    case "Chưa đủ" -> {
                        TrangThai.setStyle("-fx-background-color: #f7603e;");
                        btnHuyDon.setDisable(false);
                        btnThanhToan.setDisable(true);
                    }
                    case "Đã đủ" -> {
                        TrangThai.setStyle("-fx-background-color: #46bf34;");
                        btnHuyDon.setDisable(false);
                        btnThanhToan.setDisable(false);
                    }
                    case "Đã thanh toán" -> {
                        TrangThai.setStyle("-fx-background-color: #287a02;");
                        btnHuyDon.setDisable(true);
                        btnThanhToan.setDisable(true);
                    }
                    case null, default -> {
                        TrangThai.setStyle("-fx-background-color: #b00d02;");
                        btnHuyDon.setDisable(true);
                        btnThanhToan.setDisable(true);
                    }
                }
                LoadData(maDH);
            }
            assert conn != null;
            conn.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @FXML
    private void btnThanhToanOnAction() {
        try {
            try{
                Connection conn = DatabaseUtil.getConnection();
                if (conn != null) {
                   List<CtDondathang> ctList = DatabaseUtil.getCtDondathangByIdDondathang(conn, DH.getMaDonHang());
                   for(CtDondathang ct : ctList) {
                       Sach sach = ct.getSach();
                       Sach newSach = new Sach(sach.getMaSach(), sach.getTenSach(), sach.getMaTheLoai(), sach.getTacGia(), sach.getSoLuongTon() + ct.getSoLuong(), sach.getDonGia(), sach.getTheLoai(), sach.getHinhAnh());
                       DatabaseUtil.updateSach(conn, newSach);
                   }
                }
                assert conn != null;
                conn.close();
            }catch (Exception e){
                e.printStackTrace();
            }

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/UI/ContactWindow/BillWindow/BillAddedWindow/BillAddedWindow.fxml"));
            Parent parent = loader.load();
            Stage stage = new Stage();
            Scene scene = new Scene(parent);
            BillAdd bill = loader.getController();
            bill.LoadInitData(DH.getMaDonHang());
            stage.setScene(scene);
            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner((Stage) btnThanhToan.getScene().getWindow());
            stage.showAndWait();
            stage.setOnCloseRequest(event -> {
                System.out.println("Window closed by: " + bill.getCloseReason());
            });
            System.out.println("Window closed by: " + bill.getCloseReason());
            if(bill.getCloseReason() == 0) {
                Connection conn = DatabaseUtil.getConnection();
                if (conn != null) {
                    List<CtDondathang> ctList = DatabaseUtil.getCtDondathangByIdDondathang(conn, DH.getMaDonHang());
                    for(CtDondathang ct : ctList) {
                        Sach sach = ct.getSach();
                        Sach newSach = new Sach(sach.getMaSach(), sach.getTenSach(), sach.getMaTheLoai(), sach.getTacGia(), sach.getSoLuongTon() - ct.getSoLuong(), sach.getDonGia(), sach.getTheLoai(), sach.getHinhAnh());
                        DatabaseUtil.updateSach(conn, newSach);
                    }
                }
            }
            else {
                try{
                    Connection conn = DatabaseUtil.getConnection();
                    if (conn != null) {
                        DatabaseUtil.updateDonHang(conn, DH.getMaDonHang(), "Đã thanh toán");
                    }
                    assert conn != null;
                    conn.close();
                }catch (Exception e){
                    e.printStackTrace();
                }
                FXMLLoader loader2 = new FXMLLoader(getClass().getResource("/UI/ContactWindow/OrderWindow/OrderPropertiesWindow/OrderPropertiesWindow.fxml"));
                Parent parent2 = loader2.load();
                OrderProperties order = loader2.getController();
                order.setData((DH.getMaDonHang()));
                Scene scene2 = new Scene(parent2);
                Stage stage2 = (Stage) btnThanhToan.getScene().getWindow();
                stage2.setScene(scene2);
            }
            System.out.println("load data");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void btnHuyDonOnAction() {
        try{
            Connection conn = DatabaseUtil.getConnection();
            if (conn != null) {
                List<CtDondathang> ctList = DatabaseUtil.getCtDondathangByIdDondathang(conn, DH.getMaDonHang());
                for(CtDondathang ct : ctList) {
                    Sach sach = ct.getSach();
                    if(ct.getTrangThai().equals("Đã đủ")) {
                        Sach newSach = new Sach(sach.getMaSach(), sach.getTenSach(), sach.getMaTheLoai(), sach.getTacGia(), sach.getSoLuongTon() + ct.getSoLuong(), sach.getDonGia(), sach.getTheLoai(), sach.getHinhAnh());
                        DatabaseUtil.updateSach(conn, newSach);
                    }
                }
                DatabaseUtil.updateDonHang(conn, DH.getMaDonHang(), "Đã huỷ");
                FXMLLoader loader2 = new FXMLLoader(getClass().getResource("/UI/ContactWindow/OrderWindow/OrderPropertiesWindow/OrderPropertiesWindow.fxml"));
                Parent parent2 = loader2.load();
                OrderProperties order = loader2.getController();
                order.setData((DH.getMaDonHang()));
                Scene scene2 = new Scene(parent2);
                Stage stage2 = (Stage) btnThanhToan.getScene().getWindow();
                stage2.setScene(scene2);
            }
            assert conn != null;
            conn.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @FXML
    private void btnThoat_OnAction(ActionEvent event) {
        Stage stage = (Stage) btnThoat.getScene().getWindow();
        stage.close();
    }
}
