package application.bookstoremanager.controller.ContactWindow.BookWindow.ReceiptBook;

import application.bookstoremanager.DatabaseUtil;
import application.bookstoremanager.classdb.CtPhieunhapsach;
import application.bookstoremanager.classdb.Phieunhapsach;
import application.bookstoremanager.classdb.Sach;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Label;

import java.sql.Connection;
import java.util.List;

import static application.bookstoremanager.controller.ContactWindow.BookWindow.BookTableRow.formatCurrency;

public class DetailPaneForMain {

    @FXML
    private Label DonGia;

    @FXML
    private Label SoLuong;

    @FXML
    private Label TenSach;

    @FXML
    private Label TacGia;

    @FXML
    private Label TheLoai;

    public void setData(CtPhieunhapsach PNS) {
        SoLuong.setText(PNS.getSoLuongNhap().toString());
        DonGia.setText(formatCurrency(PNS.getDonGiaNhap()));
        try{
            Connection conn = DatabaseUtil.getConnection();
            if (conn != null) {
                List<Sach> SachList = DatabaseUtil.getAllSach(conn);
                for (Sach sach : SachList) {
                    if(sach.getMaSach().equals(PNS.getMaSach())){
                        TenSach.setText(sach.getTenSach());
                        TacGia.setText(sach.getTacGia());
                        TheLoai.setText(sach.getTheLoai().getTenTheLoai());
                        break;
                    }
                }
            }
            assert conn != null;
            conn.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
