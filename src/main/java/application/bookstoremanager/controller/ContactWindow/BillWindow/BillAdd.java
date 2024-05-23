package application.bookstoremanager.controller.ContactWindow.BillWindow;

import application.bookstoremanager.DatabaseUtil;
import application.bookstoremanager.classdb.Khachhang;
import application.bookstoremanager.classdb.Sach;
import application.bookstoremanager.controller.ContactWindow.BookWindow.BookInput.BookInputTableRow;
import application.bookstoremanager.controller.ContactWindow.BookWindow.ReceiptBook.DetailPane;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ListChangeListener;
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
import java.text.ParseException;
import java.time.LocalDate;
import java.util.*;

import static application.bookstoremanager.controller.ContactWindow.BookWindow.BookTableRow.formatCurrency;
import static application.bookstoremanager.controller.ContactWindow.BookWindow.BookTableRow.parseCurrency;
import static application.bookstoremanager.controller.ContactWindow.BookWindow.BookSearchWindow.removeDiacritics;

public class BillAdd implements Initializable {

    @FXML
    private VBox BookContainer;

    @FXML
    private VBox DanhSachNhap;

    @FXML
    private TextField DonGia;

    @FXML
    private ComboBox<String> DungTichDiem;

    @FXML
    private Label GiamGia;

    @FXML
    private DatePicker NgayLap;

    @FXML
    private TextField SDTKH;

    @FXML
    private TextField SoLuong;

    @FXML
    private TextField TenKH;

    @FXML
    private Label ThanhToan;

    @FXML
    private Label TongTien;

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
        DungTichDiem.setOnAction(event -> {
            try {
                if(!Objects.equals(DungTichDiem.getValue(), "Không")) {
                    GiamGiaHT = parseCurrency(DungTichDiem.getValue());
                    GiamGia.setText(formatCurrency(Math.min(GiamGiaHT, TongTienHT)));
                    ThanhToan.setText(formatCurrency(TongTienHT - Math.min(GiamGiaHT, TongTienHT)));
                }
                else {
                    GiamGiaHT = 0;
                    GiamGia.setText(formatCurrency(GiamGiaHT));
                    ThanhToan.setText(formatCurrency(TongTienHT - Math.min(GiamGiaHT, TongTienHT)));
                }
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
        });
        TenKH.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                DungTichDiem.getItems().clear();
                DungTichDiem.getItems().add("Không");
                DungTichDiem.setValue("Không");
                for(Khachhang khachhang : KHList){
                    if(khachhang.getSoDienThoai().equals(SDTKH.getText()) && khachhang.getHoTen().equals(newValue)){
                        DungTichDiem.getItems().add(khachhang.getTichDiem().toString());
                        break;
                    }
                }
            }
        });
        SDTKH.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                DungTichDiem.getItems().clear();
                DungTichDiem.getItems().add("Không");
                DungTichDiem.setValue("Không");
                for(Khachhang khachhang : KHList){
                    if(khachhang.getSoDienThoai().equals(newValue) && khachhang.getHoTen().equals(TenKH.getText())){
                        DungTichDiem.getItems().add(formatCurrency(khachhang.getTichDiem()));
                        break;
                    }
                }
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
        DonGia.setText("");
        try{
            Connection conn = DatabaseUtil.getConnection();
            if (conn != null) {
                Sach sach = DatabaseUtil.getSachById(conn,id);
                DonGia.setText(formatCurrency(sach.getDonGia() * DatabaseUtil.getThamso(conn).getTiLeTinhDonGiaBan()));
            }
            assert conn != null;
            conn.close();
        }catch (Exception e){
            e.printStackTrace();
        }
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
        if(SelectedBook != -1 && SoLuong != null && !Objects.equals(SoLuong.getText(), "") && DonGia != null && !Objects.equals(DonGia.getText(), "")) {
            try{
                Connection conn = DatabaseUtil.getConnection();
                if (conn != null) {
                    Sach sach = DatabaseUtil.getSachById(conn, SelectedBook);
                    if(sach.getSoLuongTon() - Integer.parseInt(SoLuong.getText()) < DatabaseUtil.getThamso(conn).getSoLuongTonToiThieu()  ) {
                        showErrorDialog("Thông tin không hợp lệ", "Số lượng vượt quá số lượng tồn cho phép");
                        return;
                    }
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/UI/ContactWindow/BillWindow/BillAddedWindow/BillInformationWindow/DetailPaneBill.fxml"));
                    Parent newContent3 = loader.load();
                    DetailPaneBill book = loader.getController();
                    book.setData(sach, SoLuong.getText(), String.valueOf(parseCurrency(DonGia.getText())), this);
                    DanhSachNhap.getChildren().add(newContent3);
                    System.out.println("Don gia: " + DonGia.getText());
                    System.out.println("Tong Tien: " + parseCurrency(DonGia.getText()));
                    TongTienHT += parseCurrency(DonGia.getText())* Double.parseDouble(SoLuong.getText());
                    TongTien.setText(formatCurrency(TongTienHT));
                    GiamGia.setText(formatCurrency(Math.min(GiamGiaHT, TongTienHT)));
                    ThanhToan.setText(formatCurrency(TongTienHT - Math.min(GiamGiaHT, TongTienHT)));
                    newContent3.getProperties().put("controller", book);
                    isUsed.put(SelectedBook, true);
                    LoadData(searchSach.getText());
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
            showErrorDialog("Thông tin không hợp lệ", "Vui lòng nhập đầy đủ thông tin");
        }

    }
    public void updateIsUsed(int bookId, boolean isUsed, double soLuong) {
        this.isUsed.put(bookId, isUsed);
        try{
            Connection conn = DatabaseUtil.getConnection();
            if (conn != null) {
                Sach sach = DatabaseUtil.getSachById(conn,bookId);
                System.out.println("Sach: " + sach);
                if(sach != null) {
                    System.out.println("Don gia: " + formatCurrency(sach.getDonGia()));
                    System.out.println("So Luong: " + formatCurrency(soLuong));
                    TongTienHT -= sach.getDonGia() * DatabaseUtil.getThamso(conn).getTiLeTinhDonGiaBan() * soLuong;
                    TongTien.setText(formatCurrency(TongTienHT));
                    GiamGia.setText(formatCurrency(Math.min(GiamGiaHT, TongTienHT)));
                    ThanhToan.setText(formatCurrency(TongTienHT - Math.min(GiamGiaHT, TongTienHT)));
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
        System.out.println("Before: " + NgayLap.getValue().isBefore(LocalDate.now()));
        System.out.println("Equals: " + NgayLap.getValue().equals(LocalDate.now()));
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
            showErrorDialog("Thông tin không hợp lệ", "Vui lòng nhập thêm số điện thoại hoặc để trống cả 2");
            return;
        }
        if((SDTKH == null || Objects.equals(SDTKH.getText(), "")) && (TenKH != null && !TenKH.getText().isEmpty())) {
            showErrorDialog("Thông tin không hợp lệ", "Vui lòng nhập thêm tên khách hàng hoặc để trống cả 2");
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
                    int idHD = DatabaseUtil.createHoadon(NgayLap.getValue(),kh.getMaKhachHang(),parseCurrency(GiamGia.getText()), conn);
                    double tichDiem = kh.getTichDiem();
                    if((int)parseCurrency(GiamGia.getText()) == 0) {
                        tichDiem += (int)(parseCurrency(TongTien.getText()) * 0.01);
                    }
                    else {
                        tichDiem -= (int)parseCurrency(GiamGia.getText());
                    }
                    System.out.println("Giam gia: " + GiamGia.getText());
                    System.out.println("Giam gia DK: " + (int)parseCurrency(GiamGia.getText()));
                    System.out.println("Tich diem: " + tichDiem);
                    Khachhang newkh = new Khachhang(kh.getMaKhachHang(), kh.getHoTen(), kh.getSoDienThoai(), tichDiem);
                    DatabaseUtil.updateKhachhang(conn,newkh);
                    ObservableList<Node> children = DanhSachNhap.getChildren();
                    for (Node node : children) {
                        if (node instanceof AnchorPane) {
                            AnchorPane anchorPane = (AnchorPane) node;
                            DetailPaneBill detailPaneController = (DetailPaneBill) anchorPane.getProperties().get("controller"); // Lấy controller của DetailPane
                            int id = detailPaneController.getID();
                            int soLuong = detailPaneController.getSoLuong();
                            double donGia = detailPaneController.getDonGia();
                            DatabaseUtil.createCtHoadon(conn, id, idHD, soLuong);
                        }
                    }
                }
                else {
                    int idHD = DatabaseUtil.createHoadonForNull(NgayLap.getValue(), conn);
                    ObservableList<Node> children = DanhSachNhap.getChildren();
                    for (Node node : children) {
                        if (node instanceof AnchorPane) {
                            AnchorPane anchorPane = (AnchorPane) node;
                            DetailPaneBill detailPaneController = (DetailPaneBill) anchorPane.getProperties().get("controller"); // Lấy controller của DetailPane
                            int id = detailPaneController.getID();
                            int soLuong = detailPaneController.getSoLuong();
                            double donGia = detailPaneController.getDonGia();
                            DatabaseUtil.createCtHoadon(conn, id, idHD, soLuong);
                        }
                    }
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
