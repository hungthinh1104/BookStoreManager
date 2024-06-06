package application.bookstoremanager.controller.ContactWindow.OrderWindow;

import application.bookstoremanager.DatabaseUtil;
import application.bookstoremanager.classdb.Dondathang;
import application.bookstoremanager.classdb.Khachhang;
import application.bookstoremanager.classdb.Sach;
import application.bookstoremanager.controller.ContactWindow.BillWindow.DetailPaneBill;
import application.bookstoremanager.controller.ContactWindow.BookWindow.BookInput.BookInputTableRow;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.chart.PieChart;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.net.URL;
import java.sql.Connection;
import java.text.ParseException;
import java.time.LocalDate;
import java.util.*;

import static application.bookstoremanager.controller.ContactWindow.BookWindow.BookSearchWindow.removeDiacritics;
import static application.bookstoremanager.controller.ContactWindow.BookWindow.BookTableRow.formatCurrency;
import static application.bookstoremanager.controller.ContactWindow.BookWindow.BookTableRow.parseCurrency;

public class OrderAdd implements Initializable {

    @FXML
    private VBox BookContainer;

    @FXML
    private VBox DanhSachNhap;

    @FXML
    private DatePicker NgayLap;

    @FXML
    private TextField SDTKH;

    @FXML
    private TextField SoLuong;

    @FXML
    private TextField TenKH;

    @FXML
    private Label TienCoc;

    @FXML
    private Button btnHuy;

    @FXML
    private Button btnThemHD;

    @FXML
    private Button btnThemSach;

    @FXML
    private TextField searchSDT;

    @FXML
    private TextField searchSach;

    private boolean isFirstLoad = true;
    private Map<Integer, Boolean> isUsed = new HashMap<>();
    private int SelectedBook = -1;
    private AnchorPane PreSelectedBook;
    private double TongTienHT = 0.0;
    private double GiamGiaHT = 0.0;
    private List<Khachhang> KHList = new ArrayList<>();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        LoadData("");
        NgayLap.setValue(LocalDate.now());
        searchSDT.setTextFormatter(new TextFormatter<>(change -> {
            String newText = change.getControlNewText();
            if (newText.matches("\\d*")) {
                return change; // If the new text is numeric, accept the change
            }
            return null; // Otherwise, reject the change
        }));
        searchSDT.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                handlerSearchSDT(newValue);
            }
        });
        searchSach.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                LoadData(newValue);
            }
        });
    }

    public void LoadData(String search) {
        try{
            Connection conn = DatabaseUtil.getConnection();
            if (conn != null) {
                List<Sach> bookList = DatabaseUtil.getAllSach(conn);
                KHList.clear();
                KHList = DatabaseUtil.getAllKhachhang(conn);
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
    }

    private void handlerSearchSDT(String sdt) {
        TenKH.setText("");
        SDTKH.setText("");
        System.out.println("sdt: " + sdt);
        if(sdt == null || sdt.isEmpty()) {
            return;
        }
        try{
            Connection conn = DatabaseUtil.getConnection();
            if (conn != null) {
                List<Khachhang> khList = DatabaseUtil.getAllKhachhang(conn);
                for(Khachhang khachhang : khList) {
                    if(khachhang.getSoDienThoai().contains(sdt)) {
                        TenKH.setText(khachhang.getHoTen());
                        SDTKH.setText(khachhang.getSoDienThoai());
                        break;
                    }
                }
            }
            assert conn != null;
            conn.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @FXML
    private void setBtnThemSach_OnAction(ActionEvent event) {
        if(SelectedBook != -1 && SoLuong != null && !Objects.equals(SoLuong.getText(), "")) {
            try{
                Connection conn = DatabaseUtil.getConnection();
                if (conn != null) {
                    Sach sach = DatabaseUtil.getSachById(conn, SelectedBook);
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/UI/ContactWindow/OrderWindow/OrderAddedWindow/OrderInformationWindow/DetailPaneOrder.fxml"));
                    Parent newContent3 = loader.load();
                    DetailPaneOrder book = loader.getController();
                    book.setData(sach, SoLuong.getText(), this);
                    DanhSachNhap.getChildren().add(newContent3);
                    TongTienHT += DatabaseUtil.getThamso(conn).getTienCoc() * Integer.parseInt(SoLuong.getText());
                    TienCoc.setText(formatCurrency(TongTienHT));
                    newContent3.getProperties().put("controller", book);
                    isUsed.put(SelectedBook, true);
                    LoadData(searchSach.getText());
                    SoLuong.setText("");
                    SelectedBook = -1;
                }
                assert conn != null;
                conn.close();
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        else {
            showErrorDialog("Thông tin không hợp lệ", "Vui lòng nhập đầy đủ thông tin");
        }

    }
    public void updateIsUsed(int bookId, boolean isUsed, double soLuong) {
        this.isUsed.put(bookId, isUsed);
        try{
            Connection conn = DatabaseUtil.getConnection();
            if (conn != null) {
                Sach sach = DatabaseUtil.getSachById(conn,bookId);
                if(sach != null) {
                    TongTienHT += DatabaseUtil.getThamso(conn).getTienCoc() * soLuong;
                    TienCoc.setText(formatCurrency(TongTienHT));
                }

            }
            assert conn != null;
            conn.close();
        }catch (Exception e){
            e.printStackTrace();
        }

        LoadData(searchSach.getText());
    }

    @FXML
    private void setBtnThemHD_OnAction(ActionEvent event) {
        if(NgayLap == null || Objects.equals(NgayLap.toString(), "")) {
            showErrorDialog("Thông tin không hợp lệ", "Vui lòng chọn ngày lập phù hợp");
            return;
        }
        if(!NgayLap.getValue().equals(LocalDate.now().minusDays(1)) && !NgayLap.getValue().equals(LocalDate.now())) {
            showErrorDialog("Thông tin không hợp lệ", "Ngày lập hoá đơn không được quá một ngày so với ngày hôm nay");
            return;
        }
        if(DanhSachNhap.getChildren().size() <= 0) {
            showErrorDialog("Thông tin không hợp lệ", "Không có sách được chọn");
            return;
        }
        if((TenKH == null || Objects.equals(TenKH.getText(), "")) && (SDTKH != null && !SDTKH.getText().isEmpty())) {
            showErrorDialog("Thông tin không hợp lệ", "Vui lòng nhập thêm số điện thoại");
            return;
        }
        if((SDTKH == null || Objects.equals(SDTKH.getText(), "")) && (TenKH != null && !TenKH.getText().isEmpty())) {
            showErrorDialog("Thông tin không hợp lệ", "Vui lòng nhập thêm tên khách hàng");
            return;
        }
        try{
            Connection conn = DatabaseUtil.getConnection();
            if (conn != null) {
                if(TenKH != null && !Objects.equals(TenKH.getText(), "")) {
                    Khachhang kh = DatabaseUtil.getKhachhangBySdt(conn, SDTKH.getText());
                    if(kh == null) {
                        DatabaseUtil.createKhachhang(conn, TenKH.getText(), SDTKH.getText(), 0);
                        kh = DatabaseUtil.getKhachhangBySdt(conn, SDTKH.getText());
                    }
                    else {
                        if(!kh.getHoTen().equals(TenKH.getText())) {
                            showErrorDialog("Thông tin không hợp lệ", "Tên và số điện thoại không khớp với dữ liệu");
                            conn.close();
                            return;
                        }
                    }
                    int idDH = DatabaseUtil.createDondathang(kh.getMaKhachHang(), conn, NgayLap.getValue());
                    ObservableList<Node> children = DanhSachNhap.getChildren();
                    boolean isDu = true;
                    for (Node node : children) {
                        if (node instanceof AnchorPane) {
                            AnchorPane anchorPane = (AnchorPane) node;
                            DetailPaneOrder detailPaneController = (DetailPaneOrder) anchorPane.getProperties().get("controller"); // Lấy controller của DetailPane
                            int id = detailPaneController.getID();
                            int soLuong = detailPaneController.getSoLuong();
                            Sach sach = DatabaseUtil.getSachById(conn, id);
                            String TrangThai = (sach.getSoLuongTon() - DatabaseUtil.getThamso(conn).getSoLuongTonToiThieu()) >= soLuong ? "Đã đủ" : "Chưa đủ";
                            System.out.println(TrangThai);
                            if(TrangThai.equals("Đã đủ")) {
                                System.out.println("So luong: " + (sach.getSoLuongTon() - soLuong));
                                Sach newSach = new Sach(sach.getMaSach(), sach.getTenSach(), sach.getMaTheLoai(), sach.getTacGia(), sach.getSoLuongTon() - soLuong, sach.getDonGia(), sach.getTheLoai(), sach.getHinhAnh());
                                DatabaseUtil.updateSach(conn, newSach);
                            }
                            else isDu = false;
                            DatabaseUtil.createCtDondathang(conn, id, idDH, soLuong, TrangThai);
                        }

                    }
                    if(isDu) {
                        DatabaseUtil.updateDonHang(conn, idDH, "Đã đủ");
                    }
                }
                else {
                    showErrorDialog("Thông tin không hợp lệ", "Không để trống thông tin khách hàng");
                    return;
                }
            }
            assert conn != null;
            conn.close();
        }catch (Exception e){
            e.printStackTrace();
        }
        Stage stage = (Stage) btnThemHD.getScene().getWindow();
        stage.close();
    }

    @FXML
    private void btnThemHD_OnAction(ActionEvent event) {
        Stage stage = (Stage) btnThemHD.getScene().getWindow();
        stage.close();
    }

    public void showErrorDialog(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null); // You can set a header text if you want
        alert.setContentText(message);
        alert.showAndWait();
    }

}
