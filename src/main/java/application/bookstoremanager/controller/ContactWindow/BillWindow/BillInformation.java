package application.bookstoremanager.controller.ContactWindow.BillWindow;

import application.bookstoremanager.DatabaseUtil;
import application.bookstoremanager.classdb.*;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

import java.sql.Connection;

import static application.bookstoremanager.controller.ContactWindow.BookWindow.BookTableRow.formatCurrency;

public class BillInformation {

    @FXML
    private Label DonGia;

    @FXML
    private Label SoLuong;

    @FXML
    private Label TacGia;

    @FXML
    private Label TenSach;

    @FXML
    private Label ThanhTien;

    @FXML
    private Label TheLoai;

    public void setData(CtHoadon ctbill) {
        try{
            Connection conn = DatabaseUtil.getConnection();
            if (conn != null) {
                Sach sach = DatabaseUtil.getSachById(conn,ctbill.getMaSach());
                TenSach.setText(sach.getTenSach());
                TacGia.setText(sach.getTacGia());
                TheLoai.setText(sach.getTheLoai().getTenTheLoai());
                ThanhTien.setText(formatCurrency(ctbill.getThanhTien()));
                SoLuong.setText(ctbill.getSoLuong().toString());
                DonGia.setText(formatCurrency(ctbill.getDonGiaBan()));
            }
            assert conn != null;
            conn.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
