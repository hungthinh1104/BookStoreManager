package application.bookstoremanager.controller.ContactWindow.BillWindow;

import application.bookstoremanager.DatabaseUtil;
import application.bookstoremanager.classdb.Hoadon;
import application.bookstoremanager.classdb.Khachhang;
import application.bookstoremanager.classdb.Sach;
import application.bookstoremanager.controller.ContactWindow.BookWindow.BookTableRow;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.image.Image;

import java.io.ByteArrayInputStream;
import java.sql.Connection;
import java.util.List;
import java.util.Objects;

import static application.bookstoremanager.controller.ContactWindow.BookWindow.BookTableRow.formatCurrency;

public class BillRowTable {

    @FXML
    private Label MaHD;

    @FXML
    private Label NgayLap;

    @FXML
    private Label SDT;

    @FXML
    private Label TenKH;

    @FXML
    private Label TongTien;

    public void setData(Hoadon bill) {
        MaHD.setText(bill.getMaHoaDon().toString());
        NgayLap.setText(bill.getNgayLap().toString());
        TongTien.setText(formatCurrency(bill.getTongTien()));
        Khachhang kh = null;
        try{
            Connection conn = DatabaseUtil.getConnection();
            if (conn != null) {
                kh = DatabaseUtil.getKhachhangById(conn,bill.getMaKhachHang());
            }
            assert conn != null;
            conn.close();
        }catch (Exception e){
            e.printStackTrace();
        }
        if(kh != null) {
            SDT.setText(kh.getSoDienThoai());
            TenKH.setText(kh.getHoTen());
        }
        else {
            SDT.setText("");
            TenKH.setText("");
        }

    }

}
