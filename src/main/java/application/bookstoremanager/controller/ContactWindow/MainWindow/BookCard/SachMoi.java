package application.bookstoremanager.controller.ContactWindow.MainWindow.BookCard;

import application.bookstoremanager.classdb.Sach;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.ByteArrayInputStream;

public class SachMoi {

    @FXML
    private ImageView AnhSach;

    @FXML
    private Label TacGia;

    @FXML
    private Label TenSach;

    public void setData( Sach book) {
        byte[] imageBytes = book.getHinhAnh();
        if (imageBytes != null && imageBytes.length > 0) {
            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(imageBytes);
            Image img = new Image(byteArrayInputStream);
            AnhSach.setImage(img);
        } else {
            System.err.println("Error: Image data is null or empty.");
        }
        TenSach.setText(book.getTenSach());
        TacGia.setText(book.getTacGia());
    }
}
