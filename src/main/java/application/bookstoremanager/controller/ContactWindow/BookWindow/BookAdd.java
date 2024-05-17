package application.bookstoremanager.controller.ContactWindow.BookWindow;

import application.bookstoremanager.DatabaseUtil;
import application.bookstoremanager.classdb.Sach;
import application.bookstoremanager.classdb.Theloai;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.net.URL;
import java.sql.Connection;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;

public class BookAdd implements Initializable {

    @FXML
    private TextField TacGia;

    @FXML
    private TextField TenSach;

    @FXML
    private ComboBox<String> TheLoai;

    @FXML
    private Button btnHuy;

    @FXML
    private Button btnThem;

    @FXML
    private ImageView imgBook;

    private Theloai selectedTL = null;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        try{
            Connection conn = DatabaseUtil.getConnection();
            if (conn != null) {
                List<Theloai> theloais = DatabaseUtil.getAllTheLoai(conn);
                TheLoai.getItems().clear();
                for (Theloai theloai : theloais) {
                    TheLoai.getItems().add(theloai.getTenTheLoai());
                }
                TheLoai.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
                        String selectedOption = TheLoai.getValue();
                        for (Theloai theloai : theloais) {
                            if(theloai.getTenTheLoai().equals(selectedOption)){
                                selectedTL = theloai;
                                break;
                            }
                        }
                        System.out.println("Selected option: " + selectedTL.getTenTheLoai());
                    }
                });
            }
            assert conn != null;
            conn.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @FXML
    protected void btnThemOnAction() {
        Connection conn = DatabaseUtil.getConnection();
        try{
            if(conn != null && !Objects.equals(TenSach.getText(), "") && !Objects.equals(TacGia.getText(), "")
                    && selectedTL.getMaTheLoai() != null && TenSach.getText() != null && TacGia.getText() != null) {
                DatabaseUtil.createSach(TenSach.getText(), selectedTL.getMaTheLoai(), TacGia.getText(), conn);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        Stage stage = (Stage) TacGia.getScene().getWindow();
        stage.close();
    }

    @FXML
    protected void btnHuyOnAction() {
        Stage stage = (Stage) TacGia.getScene().getWindow();
        stage.close();
    }
}
