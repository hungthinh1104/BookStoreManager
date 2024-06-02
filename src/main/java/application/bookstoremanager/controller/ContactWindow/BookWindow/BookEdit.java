package application.bookstoremanager.controller.ContactWindow.BookWindow;

import application.bookstoremanager.DatabaseUtil;
import application.bookstoremanager.classdb.Sach;
import application.bookstoremanager.classdb.Theloai;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;

import static application.bookstoremanager.controller.ContactWindow.BillWindow.BillAdd.showErrorDialog;


public class BookEdit implements Initializable {

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
    private Button btnSua;

    @FXML
    private ImageView imgBook;

    private Theloai selectedTL = null;
    private byte[] imageBytes = null;
    private Sach sach;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        btnSua.setStyle("-fx-background-color: #1e77fc;");
        try {
            Connection conn = DatabaseUtil.getConnection();
            if (conn != null) {
                List<Theloai> theloais = DatabaseUtil.getAllTheLoai(conn);
                TheLoai.getItems().clear();
                for (Theloai theloai : theloais) {
                    TheLoai.getItems().add(theloai.getTenTheLoai());
                }
                TheLoai.setOnAction(event -> {
                    String selectedOption = TheLoai.getValue();
                    for (Theloai theloai : theloais) {
                        if (theloai.getTenTheLoai().equals(selectedOption)) {
                            selectedTL = theloai;
                            break;
                        }
                    }
                    System.out.println("Selected option: " + selectedTL.getTenTheLoai());
                });
            }
            if (conn != null) conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void SetInitData(int id) {
        try{
            Connection conn = DatabaseUtil.getConnection();
            if (conn != null) {
                sach = DatabaseUtil.getSachById(conn, id);
                TenSach.setText(sach.getTenSach());
                TacGia.setText(sach.getTacGia());
                TheLoai.setValue(sach.getTheLoai().getTenTheLoai());
            }
            imageBytes = sach.getHinhAnh();
            if (imageBytes != null && imageBytes.length > 0) {
                ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(imageBytes);
                Image img = new Image(byteArrayInputStream);
                imgBook.setImage(img);
            } else {
                System.err.println("Error: Image data is null or empty.");
            }
            assert conn != null;
            conn.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @FXML
    protected void btnSuaOnAction() {
        Connection conn = DatabaseUtil.getConnection();
        try {
            if (conn != null && !Objects.equals(TenSach.getText(), "") && !Objects.equals(TacGia.getText(), "")
                    && selectedTL != null && TenSach.getText() != null && TacGia.getText() != null && imageBytes != null) {
                Sach newSach = new Sach(sach.getMaSach(), TenSach.getText(), selectedTL.getMaTheLoai(), TacGia.getText(), sach.getSoLuongTon(), sach.getDonGia(), sach.getTheLoai(),imageBytes);
                DatabaseUtil.updateSach(conn, newSach);
            } else {
               if(Objects.equals(TenSach.getText(), "") || TenSach.getText() == null) {
                   showErrorDialog("Thông tin không hợp lệ", "Vui lòng nhập tên sách");
                   return;
               }
                if(Objects.equals(TacGia.getText(), "") || TacGia.getText() == null) {
                    showErrorDialog("Thông tin không hợp lệ", "Vui lòng nhập tên tác giả");
                    return;
                }
                if(selectedTL == null) {
                    showErrorDialog("Thông tin không hợp lệ", "Vui lòng chọn thể loại");
                    return;
                }
                if(imageBytes == null) {
                    showErrorDialog("Thông tin không hợp lệ", "Vui lòng chọn ảnh");
                    return;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (conn != null) conn.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
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
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg", "*.gif")
        );

        File selectedFile = fileChooser.showOpenDialog(null);

        if (selectedFile != null) {
            try {
                Image image = new Image(selectedFile.toURI().toString());
                imgBook.setImage(image);

                BufferedImage bufferedImage = SwingFXUtils.fromFXImage(image, null);

                // Compress image
                BufferedImage resizedImage = resizeImage(bufferedImage, 800, 1200);
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                ImageIO.write(resizedImage, "png", baos);
                baos.flush();
                imageBytes = baos.toByteArray();
                baos.close();

                if (imageBytes.length > 1048576) { // 1MB
                    System.out.println("Selected image is too large. Please choose a smaller image.");
                    imageBytes = null;
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private BufferedImage resizeImage(BufferedImage originalImage, int targetWidth, int targetHeight) throws IOException {
        BufferedImage resizedImage = new BufferedImage(targetWidth, targetHeight, BufferedImage.TYPE_INT_ARGB);
        resizedImage.getGraphics().drawImage(originalImage, 0, 0, targetWidth, targetHeight, null);
        return resizedImage;
    }
}
