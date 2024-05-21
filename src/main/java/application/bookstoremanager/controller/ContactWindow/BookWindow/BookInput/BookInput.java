package application.bookstoremanager.controller.ContactWindow.BookWindow.BookInput;

import application.bookstoremanager.DatabaseUtil;
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
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.net.URL;
import java.sql.Connection;
import java.time.LocalDate;
import java.util.*;

import static application.bookstoremanager.controller.ContactWindow.BookWindow.BookSearchWindow.removeDiacritics;

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
        datePicker.setValue(LocalDate.now());
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
                    book.setData(sach);
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
        if(SelectedBook != -1 && SoLuong != null && !Objects.equals(SoLuong.getText(), "") && DonGia != null && !Objects.equals(DonGia.getText(), "")) {
            try{
                Connection conn = DatabaseUtil.getConnection();
                if (conn != null) {
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
        if(datePicker.getValue() == null || DanhSachNhap == null || DanhSachNhap.getChildren().isEmpty() ) {
            System.out.println("Khong the them");
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
            }
            assert conn != null;
            conn.close();
        }catch (Exception e){
            e.printStackTrace();
        }
        Stage stage = (Stage) btnThemPhieu.getScene().getWindow();
        stage.close();
    }

}