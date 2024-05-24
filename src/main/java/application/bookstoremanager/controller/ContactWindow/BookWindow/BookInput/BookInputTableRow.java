package application.bookstoremanager.controller.ContactWindow.BookWindow.BookInput;

import application.bookstoremanager.DatabaseUtil;
import application.bookstoremanager.classdb.Sach;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;

import java.sql.Connection;
import java.util.List;

import static application.bookstoremanager.controller.ContactWindow.BookWindow.BookSearchWindow.removeDiacritics;

public class BookInputTableRow {

    @FXML
    private Label TacGia;

    @FXML
    private Label TenSach;

    @FXML
    private Label TheLoai;

    @FXML
    private AnchorPane rootPane;

    private int id;

    public void setData(Sach book) {
        id = book.getMaSach();
        TenSach.setText(book.getTenSach());
        TacGia.setText(book.getTacGia());
        rootPane.setUserData(id);
        try{
            Connection conn = DatabaseUtil.getConnection();
            if (conn != null) {
                int soLuong = Math.max(0,book.getSoLuongTon() - DatabaseUtil.getThamso(conn).getSoLuongTonToiThieu());
                TheLoai.setText(String.valueOf(soLuong));
            }
            assert conn != null;
            conn.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public int getId() {
        return id;
    }
}
