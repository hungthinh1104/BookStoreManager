package application.bookstoremanager.controller.ContactWindow.OrderWindow;

import application.bookstoremanager.classdb.Sach;
import application.bookstoremanager.controller.ContactWindow.BillWindow.BillAdd;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import org.kordamp.ikonli.javafx.FontIcon;

import static application.bookstoremanager.controller.ContactWindow.BookWindow.BookTableRow.formatCurrency;

public class DetailPaneOrder {

    @FXML
    private Label SoLuong;

    @FXML
    private Label TenSach;

    @FXML
    private FontIcon btnDelete;

    private OrderAdd bookInput;
    private int id;

    public void setData(Sach book, String _SoLuong, OrderAdd _bookInput) {
        TenSach.setText(book.getTenSach());
        SoLuong.setText(_SoLuong);
        bookInput = _bookInput;
        id = book.getMaSach();
    }

    public int getID() {
        return id;
    }

    public int getSoLuong() {
        return Integer.parseInt(SoLuong.getText());
    }

    @FXML
    private void handleDeleteButtonClick(MouseEvent event) {
        try {
            Parent detailPane = (Parent) btnDelete.getParent();

            VBox danhSachNhap = (VBox) detailPane.getParent();

            danhSachNhap.getChildren().remove(detailPane);
            if (bookInput != null) {
                bookInput.updateIsUsed(id, false, Double.parseDouble(SoLuong.getText()));
            }

            // Đồng thời, cần cập nhật lại trạng thái của sách trong BookInput
            // Đặc biệt là cập nhật lại isUsed để cho phép sách này có thể được chọn lại.
            // Điều này có thể thực hiện bằng cách gọi một phương thức public của BookInput từ DetailPane.
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
