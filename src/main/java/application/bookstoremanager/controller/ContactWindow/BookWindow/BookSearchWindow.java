package application.bookstoremanager.controller.ContactWindow.BookWindow;

import application.bookstoremanager.DatabaseUtil;
import application.bookstoremanager.classdb.Nguoidung;
import application.bookstoremanager.classdb.Sach;
import application.bookstoremanager.controller.ContactWindow.MainWindow.BookCard.Card1;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.text.Normalizer;
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

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        LoadData("");
        searchText.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                LoadData(newValue);
            }
        });
    }

    private void LoadData(String search) {
        try{
            Connection conn = DatabaseUtil.getConnection();
            if (conn != null) {
                Integer stt = 0;
                List<Sach> bookList = DatabaseUtil.getAllSach(conn);
                BookContainer.getChildren().clear();
                for(Sach sach : bookList) {
                    if(!search.isEmpty() && !removeDiacritics(sach.getTenSach().toLowerCase()).contains(removeDiacritics(search.toLowerCase()))) continue;
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
            Scene scene = new Scene(loader.load());
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
