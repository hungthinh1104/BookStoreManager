package application.bookstoremanager.controller.ContactWindow.BookWindow;

import application.bookstoremanager.classdb.Sach;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import org.kordamp.ikonli.javafx.FontIcon;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URL;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Locale;
import java.util.ResourceBundle;

public class BookTableRow implements Initializable {

    @FXML
    private ImageView AnhSach;

    @FXML
    private TextField DonGia;

    @FXML
    private FontIcon Edit;

    @FXML
    private TextField SoLuong;

    @FXML
    private TextField TacGia;

    @FXML
    private TextField TenSach;

    @FXML
    private TextField TheLoai;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    public void setData(Integer _STT, Sach book) {
        byte[] imageBytes = book.getHinhAnh();
        if (imageBytes != null && imageBytes.length > 0) {
            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(imageBytes);
            Image img = new Image(byteArrayInputStream);
            AnhSach.setImage(img);
        } else {
            System.err.println("Error: Image data is null or empty.");
        }
        DonGia.setText(formatCurrency(book.getDonGia()));
        TenSach.setText(book.getTenSach());
        SoLuong.setText(book.getSoLuongTon().toString());
        TacGia.setText(book.getTacGia());
        TheLoai.setText(book.getTheLoai().getTenTheLoai());
    }

    public static String formatCurrency(double value) {
        NumberFormat numberFormat = NumberFormat.getInstance(new Locale("vi", "VN"));
        numberFormat.setMaximumFractionDigits(1);
        return numberFormat.format(value);
    }
    public static double parseCurrency(String value) throws ParseException {
        if(value == null || value.isEmpty()) return 0;
        NumberFormat numberFormat = NumberFormat.getInstance(new Locale("vi", "VN"));
        numberFormat.setMaximumFractionDigits(1);
        return numberFormat.parse(value).doubleValue();
    }
}
