package application.bookstoremanager.controller.ContactWindow.CustomerWindow;

import application.bookstoremanager.classdb.Khachhang;
import application.bookstoremanager.classdb.Sach;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;

import java.io.ByteArrayInputStream;

public class CustomerTableRow {

    @FXML
    private Label MaKhachHang;

    @FXML
    private Label SoDienThoai;

    @FXML
    private Label TenKhachHang;

    @FXML
    private  Label TichDiem;

    public void setData(Khachhang khachhang) {
        MaKhachHang.setText(khachhang.getMaKhachHang().toString());
        SoDienThoai.setText(khachhang.getSoDienThoai());
        TenKhachHang.setText(khachhang.getHoTen());
        TichDiem.setText(khachhang.getTichDiem().toString());
    }
}
