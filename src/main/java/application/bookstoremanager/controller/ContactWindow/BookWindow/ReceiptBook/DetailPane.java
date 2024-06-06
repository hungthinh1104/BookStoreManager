package application.bookstoremanager.controller.ContactWindow.BookWindow.ReceiptBook;

import application.bookstoremanager.DatabaseUtil;
import application.bookstoremanager.classdb.CtPhieunhapsach;
import application.bookstoremanager.classdb.Phieunhapsach;
import application.bookstoremanager.classdb.Sach;
import application.bookstoremanager.controller.ContactWindow.BookWindow.BookInput.BookInput;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import org.kordamp.ikonli.javafx.FontIcon;

import java.sql.Connection;
import java.util.List;

public class DetailPane {

    @FXML
    private Label DonGia;

    @FXML
    private Label SoLuong;

    @FXML
    private Label TenSach;

    @FXML
    private FontIcon btnDelete;

    private BookInput bookInput;
    private int id;

    public void setData(Sach book, String _SoLuong, String _DonGia, BookInput _bookInput) {
        TenSach.setText(book.getTenSach());
        SoLuong.setText("Số lượng: " + _SoLuong);
        DonGia.setText("Đơn giá: " + _DonGia);
        bookInput = _bookInput;
        id = book.getMaSach();
    }

    public int getID() {
        return id;
    }

    public int getSoLuong() {
        return Integer.parseInt(SoLuong.getText());
    }

    public Double getDonGia() {
        return Double.parseDouble(DonGia.getText());
    }
    @FXML
    private void handleDeleteButtonClick(MouseEvent event) {
        try {
            Parent detailPane = (Parent) btnDelete.getParent();

            VBox danhSachNhap = (VBox) detailPane.getParent();

            danhSachNhap.getChildren().remove(detailPane);
            if (bookInput != null) {
                bookInput.updateIsUsed(id, false);
            }

            // Đồng thời, cần cập nhật lại trạng thái của sách trong BookInput
            // Đặc biệt là cập nhật lại isUsed để cho phép sách này có thể được chọn lại.
            // Điều này có thể thực hiện bằng cách gọi một phương thức public của BookInput từ DetailPane.
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
