package application.bookstoremanager.controller.ContactWindow.BookWindow;

import application.bookstoremanager.DatabaseUtil;
import application.bookstoremanager.classdb.Sach;
import application.bookstoremanager.classdb.Theloai;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import javax.imageio.ImageIO;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.util.List;
import java.util.ResourceBundle;
import java.awt.image.BufferedImage;

public class BookAdd implements Initializable {

    @FXML
    private Button btnChonAnh;
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
    private byte[] imageBytes = null;


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
            DatabaseUtil.createSach(TenSach.getText(), selectedTL.getMaTheLoai(), TacGia.getText(), imageBytes, conn);
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

    @FXML
    protected void btnChonAnhOnAction() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Chọn Ảnh");
        // Set file extension filters
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg", "*.gif")
        );

        // Show open file dialog
        File selectedFile = fileChooser.showOpenDialog(null);

        if (selectedFile != null) {
            try {
                // Load and set the image
                Image image = new Image(selectedFile.toURI().toString());
                imgBook.setImage(image);
                BufferedImage bufferedImage = SwingFXUtils.fromFXImage(image, null);
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                ImageIO.write(bufferedImage, "png", baos);
                baos.flush();
                imageBytes = baos.toByteArray();
                baos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
