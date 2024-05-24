package application.bookstoremanager.controller.ContactWindow.OrderWindow;

import application.bookstoremanager.DatabaseUtil;
import application.bookstoremanager.classdb.CtDondathang;
import application.bookstoremanager.classdb.Dondathang;
import application.bookstoremanager.classdb.Hoadon;
import application.bookstoremanager.classdb.Khachhang;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

import java.sql.Connection;
import java.util.List;
import java.util.Objects;

import static application.bookstoremanager.controller.ContactWindow.BookWindow.BookTableRow.formatCurrency;

public class OrderRow {

    @FXML
    private Label MaDH;

    @FXML
    private Label NgayLap;

    @FXML
    private Label SDT;

    @FXML
    private Label TenKH;

    @FXML
    private Label TienCoc;

    @FXML
    private Label TrangThai;

    public void setData(Dondathang order) {
        MaDH.setText(order.getMaDonHang().toString());
        NgayLap.setText(order.getNgayLap().toString());
        TienCoc.setText(formatCurrency(order.getTongTienCoc()));
        SDT.setText(order.getKhachHang().getSoDienThoai());
        TenKH.setText(order.getKhachHang().getHoTen());
        TrangThai.setText(order.getTrangThai());
        if(Objects.equals(order.getTrangThai(), "Chưa đủ")) {
            TrangThai.setStyle("-fx-background-color: #f7603e;");
        }
        else if(Objects.equals(order.getTrangThai(), "Đã đủ")) {
            TrangThai.setStyle("-fx-background-color: #46bf34;");
        }
        else if(Objects.equals(order.getTrangThai(), "Đã thanh toán")) {
            TrangThai.setStyle("-fx-background-color: #287a02;");
        }
        else {
            TrangThai.setStyle("-fx-background-color: #b00d02;");
        }


    }

}
