package application.bookstoremanager.controller.ContactWindow.BookWindow.BookInput;

import application.bookstoremanager.DatabaseUtil;
import application.bookstoremanager.classdb.CtDondathang;
import application.bookstoremanager.classdb.Dondathang;
import application.bookstoremanager.classdb.Sach;
import application.bookstoremanager.controller.ContactWindow.BookWindow.BookTableRow;
import application.bookstoremanager.controller.ContactWindow.BookWindow.ReceiptBook.DetailPane;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.net.URL;
import java.sql.Connection;
import java.time.LocalDate;
import java.util.*;

import static application.bookstoremanager.controller.ContactWindow.BookWindow.BookSearchWindow.removeDiacritics;
import static application.bookstoremanager.controller.ContactWindow.BillWindow.BillAdd.showErrorDialog;

public class BookInput implements Initializable {

    @FXML
    private VBox BookContainer;

    @FXML
    private VBox DanhSachNhap;

    @FXML
    private TextField DonGia;

    @FXML
    private TextField SoLuong;

    @FXML
    private Button btnHuy;

    @FXML
    private Button btnThemPhieu;

    @FXML
    private Button btnThemSach;

    @FXML
    private DatePicker datePicker;

    @FXML
    private TextField searchBook;

    private int SelectedBook = -1;
    private AnchorPane PreSelectedBook;
    private Map<Integer, Boolean> isUsed = new HashMap<>();
    private boolean isFirstLoad = true;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        LoadData("");
        searchBook.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                LoadData(newValue);
            }
        });
        datePicker.setPromptText("d/M/y");
        datePicker.setValue(LocalDate.now());
        SoLuong.setTextFormatter(new TextFormatter<>(change -> {
            String newText = change.getControlNewText();
            if (newText.matches("\\d*")) {
                return change; // If the new text is numeric, accept the change
            }
            return null; // Otherwise, reject the change
        }));
        DonGia.setTextFormatter(new TextFormatter<>(change -> {
            String newText = change.getControlNewText();
            if (newText.matches("\\d*")) {
                return change; // If the new text is numeric, accept the change
            }
            return null; // Otherwise, reject the change
        }));

    }
    public void LoadData(String search) {
        try{
            Connection conn = DatabaseUtil.getConnection();
            if (conn != null) {
                List<Sach> bookList = DatabaseUtil.getAllSach(conn);
                BookContainer.getChildren().clear();
                for(Sach sach : bookList) {
                    if(search != null && !search.isEmpty() && !removeDiacritics(sach.getTenSach().toLowerCase()).contains(removeDiacritics(search.toLowerCase()))) continue;
                    if(!isFirstLoad && isUsed.get(sach.getMaSach())) continue;
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/UI/ContactWindow/BookWindow/ReceiptBookWindow/BookTableRow/BookTableInputRow.fxml"));
                    Parent newContent3 = loader.load();
                    BookInputTableRow book = loader.getController();
                    if(book != null) book.setData(sach);
                    BookContainer.getChildren().add(newContent3);
                    newContent3.setOnMouseClicked(this::handleAnchorPaneClick);
                    if(isFirstLoad) isUsed.put(sach.getMaSach(), false);
                }
            }
            assert conn != null;
            conn.close();
        }catch (Exception e){
            e.printStackTrace();
        }
        if(isFirstLoad) isFirstLoad = false;
    }

    private void handleAnchorPaneClick(MouseEvent event) {
        AnchorPane clickedAnchorPane = (AnchorPane) event.getSource();
        Integer id = (Integer) clickedAnchorPane.getUserData();  // Lấy id từ UserData
        if(PreSelectedBook != null) {
            PreSelectedBook.setStyle("-fx-background-color: white;");
        }
        clickedAnchorPane.setStyle("-fx-background-color: #F5F5F5;");
        PreSelectedBook = clickedAnchorPane;
        SelectedBook = id;
        System.out.println("Selected Book: " + SelectedBook);
    }

    @FXML
    private void setBtnThemSach_OnAction(ActionEvent event) {
        if(SelectedBook == -1) {
            showErrorDialog("Thông tin không hợp lệ", "Vui lòng chọn sách");
            return;
        }
        if(SoLuong == null || Objects.equals(SoLuong.getText(), "")) {
            showErrorDialog("Thông tin không hợp lệ", "Vui lòng nhập số lượng");
            return;
        }

        if(DonGia == null || Objects.equals(DonGia.getText(), "")) {
            showErrorDialog("Thông tin không hợp lệ", "Vui lòng nhập đơn giá");
            return;
        }
        if(SelectedBook != -1 && SoLuong != null && !Objects.equals(SoLuong.getText(), "") && DonGia != null && !Objects.equals(DonGia.getText(), "")) {
            try{
                Connection conn = DatabaseUtil.getConnection();
                if (conn != null) {
                    if(Integer.parseInt(SoLuong.getText()) < DatabaseUtil.getThamso(conn).getSoLuongNhapToiThieu()) {
                        showErrorDialog("Thông tin không hợp lệ", "Số lượng không đủ lượng nhập tối thiểu");
                        return;
                    }
                    if(Integer.parseInt(SoLuong.getText()) +  DatabaseUtil.getSachById(conn, SelectedBook).getSoLuongTon() > DatabaseUtil.getThamso(conn).getSoLuongNhapToiThieu()) {
                        showErrorDialog("Thông tin không hợp lệ", "Số lượng vượt quá số lượng tồn cho phép");
                        return;
                    }
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/UI/ContactWindow/BookWindow/ReceiptBookWindow/DetailPane/DetailPane.fxml"));
                    Parent newContent3 = loader.load();
                    DetailPane book = loader.getController();
                    book.setData(DatabaseUtil.getSachById(conn, SelectedBook), SoLuong.getText(), DonGia.getText(), this);
                    DanhSachNhap.getChildren().add(newContent3);
                    newContent3.getProperties().put("controller", book);
                    isUsed.put(SelectedBook, true);
                    LoadData(searchBook.getText());
                    SoLuong.setText("");
                    DonGia.setText("");
                }
                assert conn != null;
                conn.close();
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        else {
            System.out.println("Thiếu thông tin");
        }

    }
    public void updateIsUsed(int bookId, boolean isUsed) {
        this.isUsed.put(bookId, isUsed);
        LoadData(searchBook.getText());
    }

    @FXML
    private void btnThemPhieu_OnAction(ActionEvent event) {
        if(datePicker.getValue() == null || Objects.equals(datePicker.getValue().toString(), "")) {
            showErrorDialog("Thông tin không hợp lệ", "Vui lòng chọn ngày lập phù hợp");
            return;
        }
        if(!datePicker.getValue().equals(LocalDate.now().minusDays(1)) && !datePicker.getValue().equals(LocalDate.now())) {
            showErrorDialog("Thông tin không hợp lệ", "Ngày lập hoá đơn không được quá một ngày so với ngày hôm nay");
            return;
        }
        if(DanhSachNhap.getChildren().size() <= 0) {
            showErrorDialog("Thông tin không hợp lệ", "Không có sách được chọn");
            return;
        }
        try{
            Connection conn = DatabaseUtil.getConnection();
            if (conn != null) {
                int idPN = DatabaseUtil.createPhieunhapsach(datePicker.getValue(), conn);
                ObservableList<Node> children = DanhSachNhap.getChildren();

                for (Node node : children) {
                    if (node instanceof AnchorPane) {
                        AnchorPane anchorPane = (AnchorPane) node;
                        DetailPane detailPaneController = (DetailPane) anchorPane.getProperties().get("controller"); // Lấy controller của DetailPane
                        int id = detailPaneController.getID();
                        int soLuong = detailPaneController.getSoLuong();
                        double donGia = detailPaneController.getDonGia();
                        DatabaseUtil.createCtPhieunhapsach(conn, id, idPN, soLuong, donGia);
                    }
                }

                List<Dondathang> dhList = DatabaseUtil.getDonhangByTrangThai(conn,"Chưa đủ");
                for (Dondathang dh : dhList) {
                    boolean isDu = true;
                    List<CtDondathang> ctList = DatabaseUtil.getCtDondathangByIdDondathang(conn,dh.getMaDonHang());
                    for(CtDondathang ct : ctList) {
                        Sach sach = DatabaseUtil.getSachById(conn, ct.getMaSach());
                        if(Objects.equals(ct.getTrangThai(), "Chưa đủ")) {
                            if((ct.getSoLuong() <= (sach.getSoLuongTon() - DatabaseUtil.getThamso(conn).getSoLuongTonToiThieu()))) {
                                Sach newSach = new Sach(sach.getMaSach(), sach.getTenSach(), sach.getMaTheLoai(), sach.getTacGia(), sach.getSoLuongTon() - ct.getSoLuong(), sach.getDonGia(), sach.getTheLoai(), sach.getHinhAnh());
                                DatabaseUtil.updateSach(conn, newSach);
                                DatabaseUtil.updateCtDonHang(conn, dh.getMaDonHang(), "Đã đủ", sach.getMaSach());
                            }
                            else isDu = false;
                        }
                    }
                    if(isDu) {
                        DatabaseUtil.updateDonHang(conn,dh.getMaDonHang(), "Đã đủ");
                    }
                }

            }
            assert conn != null;
            conn.close();
        }catch (Exception e){
            e.printStackTrace();
        }
        Stage stage = (Stage) btnThemPhieu.getScene().getWindow();
        stage.close();
    }

    @FXML
    private void btnHuyOnAction(ActionEvent event) {
        Stage stage = (Stage) btnThemPhieu.getScene().getWindow();
        stage.close();
    }
}
