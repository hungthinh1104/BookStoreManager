package application.bookstoremanager.controller.ContactWindow.BookWindow;

import application.bookstoremanager.classdb.Sach;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;

import java.net.URL;
import java.text.NumberFormat;
import java.util.Locale;
import java.util.ResourceBundle;

public class BookTableRow implements Initializable {

    @FXML
    private Label DonGia;

    @FXML
    private Label STT;

    @FXML
    private Label SoLuong;

    @FXML
    private Label TacGia;

    @FXML
    private Label TenSach;

    @FXML
    private Label TheLoai;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    public void setData(Integer _STT, Sach book) {
        STT.setText(_STT.toString());
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
}
