package application.bookstoremanager.controller.ContactWindow.BookWindow.ReceiptBook;

import application.bookstoremanager.classdb.Phieunhapsach;
import application.bookstoremanager.classdb.Sach;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.Image;

import java.io.ByteArrayInputStream;
import java.net.URL;
import java.util.ResourceBundle;

public class BookTableRow implements Initializable {

    @FXML
    private Label MaPNS;

    @FXML
    private Label NgayLPNS;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    public void setData(Phieunhapsach PNS) {
        MaPNS.setText(PNS.getMaPhieuNhap().toString());
        NgayLPNS.setText(PNS.getNgayNhap().toString());
    }
}
