package application.bookstoremanager.controller.ContactWindow.BookWindow;

import application.bookstoremanager.DatabaseUtil;
import application.bookstoremanager.classdb.Nguoidung;
import application.bookstoremanager.classdb.Sach;
import application.bookstoremanager.classdb.Theloai;
import application.bookstoremanager.controller.ContactWindow.MainWindow.BookCard.Card1;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.regex.Pattern;

public class BookSearchWindow implements Initializable {

    @FXML
    private VBox BookContainer;

    @FXML
    private TextField searchText;

    @FXML
    private Button btnAddBook;

    @FXML
    private ComboBox<String> cbTacGia;

    @FXML
    private TextField searchTG;

    @FXML
    private ComboBox<String> cbTheLoai;

    private String selectedTL;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        LoadData("", "", "");

        searchText.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                LoadData(newValue, selectedTL, searchTG.getText());
            }
        });
        searchTG.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                LoadData(searchText.getText(), selectedTL, newValue);
            }
        });
        try {
            Connection conn = DatabaseUtil.getConnection();
            if (conn != null) {
                List<Theloai> theloais = DatabaseUtil.getAllTheLoai(conn);
                cbTheLoai.getItems().clear();
                cbTheLoai.getItems().add("Tất cả");
                for (Theloai theloai : theloais) {
                    cbTheLoai.getItems().add(theloai.getTenTheLoai());
                }
                cbTheLoai.setOnAction(event -> {
                    selectedTL = cbTheLoai.getValue();
                    LoadData(searchText.getText(), selectedTL, searchTG.getText());
                });
            }
            if (conn != null) conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    public void LoadData(String search, String theLoai, String tacGia) {
        System.out.println(search + "  " + theLoai);
        try{
            Connection conn = DatabaseUtil.getConnection();
            if (conn != null) {
                int stt = 0;
                List<Sach> bookList = DatabaseUtil.getAllSach(conn);
                BookContainer.getChildren().clear();
                for(Sach sach : bookList) {
                    if(!search.isEmpty() && !removeDiacritics(sach.getTenSach().toLowerCase()).contains(removeDiacritics(search.toLowerCase()))) continue;
                    if(theLoai != null && !theLoai.isEmpty() && !theLoai.equals("Tất cả") && !Objects.equals(sach.getTheLoai().getTenTheLoai(), theLoai)) continue;
                    if(tacGia != null && !tacGia.isEmpty() && !tacGia.equals("Tất cả") && !removeDiacritics(sach.getTacGia().toLowerCase()).contains(removeDiacritics(tacGia.toLowerCase()))) continue;
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/UI/ContactWindow/BookWindow/SearchBookWindow/BookTableRow/BookTableRow.fxml"));
                    Parent newContent3 = loader.load();
                    BookTableRow book = loader.getController();
                    book.setData(++stt, sach);
                    BookContainer.getChildren().add(newContent3);
                }
            }
            assert conn != null;
            conn.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public static String removeDiacritics(String str) {
        // Chuẩn hóa chuỗi, chuyển các ký tự có dấu thành tổ hợp ký tự không dấu và dấu
        String normalized = Normalizer.normalize(str, Normalizer.Form.NFD);
        // Loại bỏ các dấu (các ký tự có mã Unicode trong khoảng \p{M})
        Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
        return pattern.matcher(normalized).replaceAll("");
    }

    @FXML
    protected void btnAddBook_OnAction() {
        System.out.println("btnAddBook_OnAction");
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/UI/ContactWindow/BookWindow/SearchBookWindow/BookAddedWindow/BookAddedWindow.fxml"));
            Parent parent = loader.load();
            Stage stage = new Stage();
            Scene scene = new Scene(parent);
            stage.setScene(scene);
            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner((Stage) btnAddBook.getScene().getWindow());
            stage.showAndWait();
            System.out.println("load data");
            LoadData(searchText.getText(), selectedTL, searchTG.getText());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}