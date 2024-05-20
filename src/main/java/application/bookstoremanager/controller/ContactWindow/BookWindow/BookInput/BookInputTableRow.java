package application.bookstoremanager.controller.ContactWindow.BookWindow.BookInput;

import application.bookstoremanager.classdb.Sach;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;

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
        TheLoai.setText(book.getTheLoai().getTenTheLoai());
        rootPane.setUserData(id);
    }

    public int getId() {
        return id;
    }
}
