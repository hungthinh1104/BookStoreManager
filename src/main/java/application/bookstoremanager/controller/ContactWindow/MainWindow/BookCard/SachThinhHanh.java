package application.bookstoremanager.controller.ContactWindow.MainWindow.BookCard;

import application.bookstoremanager.classdb.Sach;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.ByteArrayInputStream;

import static application.bookstoremanager.controller.ContactWindow.BookWindow.BookTableRow.formatCurrency;

public class SachThinhHanh {

    @FXML
    private ImageView AnhSach;

    @FXML
    private Label DaBan;

    @FXML
    private Label GiaBan;

    @FXML
    private Label TenSach;

    public void setData(Sach book, String SoLuongBan) {
        byte[] imageBytes = book.getHinhAnh();
        if (imageBytes != null && imageBytes.length > 0) {
            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(imageBytes);
            Image img = new Image(byteArrayInputStream);
            AnhSach.setImage(img);
        } else {
            System.err.println("Error: Image data is null or empty.");
        }
        GiaBan.setText("Giá bán: " + formatCurrency(book.getDonGia()));
        TenSach.setText(book.getTenSach());
        DaBan.setText("Bán được: " + SoLuongBan);
    }
}
