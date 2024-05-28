package application.bookstoremanager.controller.ContactWindow.OrderWindow;

import application.bookstoremanager.DatabaseUtil;
import application.bookstoremanager.classdb.CtDondathang;
import application.bookstoremanager.classdb.CtHoadon;
import application.bookstoremanager.classdb.Sach;
import application.bookstoremanager.classdb.Theloai;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

import java.sql.Connection;

import static application.bookstoremanager.controller.ContactWindow.BookWindow.BookTableRow.formatCurrency;

public class OrderInfor {

    @FXML
    private Label SoLuong;

    @FXML
    private Label TacGia;

    @FXML
    private Label TenSach;

    @FXML
    private Label TheLoai;

    @FXML
    private Label TrangThai;

    public void setData(CtDondathang ctorder) {
        TenSach.setText(ctorder.getSach().getTenSach());
        TacGia.setText(ctorder.getSach().getTacGia());
        SoLuong.setText(ctorder.getSoLuong().toString());
        TheLoai.setText(ctorder.getSach().getTheLoai().getTenTheLoai());
        TrangThai.setText(ctorder.getTrangThai());
        switch (ctorder.getTrangThai()) {
            case "Chưa đủ" -> TrangThai.setStyle("-fx-background-color: #f7603e;");
            case "Đã đủ" -> TrangThai.setStyle("-fx-background-color: #46bf34;");
            case "Đã thanh toán" -> TrangThai.setStyle("-fx-background-color: #287a02;");
            case null, default -> TrangThai.setStyle("-fx-background-color: #b00d02;");
        }
    }
}
