package application.bookstoremanager.controller.ContactWindow.MainWindow.BookCard;

import application.bookstoremanager.DatabaseUtil;
import application.bookstoremanager.classdb.Sach;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;

import java.io.ByteArrayInputStream;
import java.net.URL;
import java.sql.Connection;
import java.util.ResourceBundle;

import static application.bookstoremanager.controller.ContactWindow.BookWindow.BookTableRow.formatCurrency;

public class SachThinhHanh implements Initializable {

    @FXML
    private ImageView AnhSach;

    @FXML
    private Label DaBan;

    @FXML
    private Label GiaBan;

    @FXML
    private AnchorPane MainAnchorPane;

    @FXML
    private Label TenSach;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    public void setData(Sach book, String SoLuongBan) {
        byte[] imageBytes = book.getHinhAnh();
        if (imageBytes != null && imageBytes.length > 0) {
            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(imageBytes);
            Image img = new Image(byteArrayInputStream);
            AnhSach.setImage(img);
        } else {
            System.err.println("Error: Image data is null or empty.");
        }
        try{
            Connection conn = DatabaseUtil.getConnection();
            if (conn != null) {
                GiaBan.setText("Giá bán: " + formatCurrency(book.getDonGia() * DatabaseUtil.getThamso(conn).getTiLeTinhDonGiaBan()));
            }
            assert conn != null;
            conn.close();
        }catch (Exception e){
            e.printStackTrace();
        }
        TenSach.setText(book.getTenSach());
        DaBan.setText("Bán được: " + SoLuongBan);
    }
}
