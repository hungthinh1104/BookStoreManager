package application.bookstoremanager.controller.ContactWindow.BookWindow;

import application.bookstoremanager.DatabaseUtil;
import application.bookstoremanager.classdb.Sach;
import application.bookstoremanager.controller.ContactWindow.CustomerWindow.EditCustomer;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.kordamp.ikonli.javafx.FontIcon;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.ResourceBundle;

public class BookTableRow implements Initializable {

    @FXML
    private ImageView AnhSach;

    @FXML
    private TextField DonGia;

    @FXML
    private FontIcon btnEdit;

    @FXML
    private TextField SoLuong;

    @FXML
    private TextField TacGia;

    @FXML
    private TextField TenSach;

    @FXML
    private TextField TheLoai;

    private Sach sach;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        btnEdit.setOnMouseClicked(event -> {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/UI/ContactWindow/BookWindow/SearchBookWindow/BookAddedWindow/BookEditWindow.fxml"));
                Parent parent = loader.load();
                BookEdit customer = loader.getController();
                customer.SetInitData(sach.getMaSach());
                Stage stage = new Stage();
                Scene scene = new Scene(parent);
                stage.setScene(scene);
                stage.initModality(Modality.WINDOW_MODAL);
                stage.initOwner((Stage)btnEdit.getScene().getWindow());
                stage.showAndWait();
                System.out.println("load data");
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    public void setData(Integer _STT, Sach book) {
        byte[] imageBytes = book.getHinhAnh();
        sach = book;
        if (imageBytes != null && imageBytes.length > 0) {
            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(imageBytes);
            Image img = new Image(byteArrayInputStream);
            AnhSach.setImage(img);
        } else {
            System.err.println("Error: Image data is null or empty.");
        }
        try{
            Connection conn = DatabaseUtil.getConnection();
            if (conn != null) {
                DonGia.setText(formatCurrency(book.getDonGia() * DatabaseUtil.getThamso(conn).getTiLeTinhDonGiaBan()));
            }
            assert conn != null;
            conn.close();
        }catch (Exception e){
            e.printStackTrace();
        }

        TenSach.setText(book.getTenSach());
        SoLuong.setText(book.getSoLuongTon().toString());
        TacGia.setText(book.getTacGia());
        TheLoai.setText(book.getTheLoai().getTenTheLoai());
    }

    public static String formatCurrency(double value) {
        NumberFormat numberFormat = NumberFormat.getInstance(new Locale("vi", "VN"));
        numberFormat.setMaximumFractionDigits(1);
        return numberFormat.format(value);
    }
    public static double parseCurrency(String value) throws ParseException {
        if(value == null || value.isEmpty()) return 0;
        NumberFormat numberFormat = NumberFormat.getInstance(new Locale("vi", "VN"));
        numberFormat.setMaximumFractionDigits(1);
        return numberFormat.parse(value).doubleValue();
    }
}
