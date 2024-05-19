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

public class DetailPane {

    @FXML
    private Label DonGia;

    @FXML
    private Label SoLuong;

    @FXML
    private Label TenSach;

    public void setData(CtPhieunhapsach PNS) {
        SoLuong.setText(PNS.getSoLuongNhap().toString());
        DonGia.setText(PNS.getDonGiaNhap().toString());
        try{
            Connection conn = DatabaseUtil.getConnection();
            if (conn != null) {
                List<Sach> SachList = DatabaseUtil.getAllSach(conn);
                for (Sach sach : SachList) {
                    if(sach.getMaSach().equals(PNS.getMaSach())){
                        TenSach.setText(sach.getTenSach());
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
