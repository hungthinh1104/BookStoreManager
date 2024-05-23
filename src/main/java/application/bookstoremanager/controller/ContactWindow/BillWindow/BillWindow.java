package application.bookstoremanager.controller.ContactWindow.BillWindow;

import application.bookstoremanager.DatabaseUtil;
import application.bookstoremanager.classdb.Hoadon;
import application.bookstoremanager.classdb.Khachhang;
import application.bookstoremanager.classdb.Sach;
import application.bookstoremanager.controller.ContactWindow.BookWindow.BookTableRow;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;

import static application.bookstoremanager.controller.ContactWindow.BookWindow.BookSearchWindow.removeDiacritics;

public class BillWindow implements Initializable {

    @FXML
    private VBox BillContainer;

    @FXML
    private Button btnTaoHoaDon;

    @FXML
    private TextField seachTenKH;

    @FXML
    private DatePicker searchDate;

    @FXML
    private TextField searchSDT;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        LoadData("", "", "");

        seachTenKH.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                LoadData(newValue, searchDate.getValue() == null ? "" : searchDate.getValue().toString(), searchSDT.getText());
            }
        });
        searchSDT.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                LoadData(seachTenKH.getText(), searchDate.getValue() == null ? "" : searchDate.getValue().toString(), newValue);
            }
        });
        searchDate.valueProperty().addListener(new ChangeListener<LocalDate>() {
            @Override
            public void changed(ObservableValue<? extends LocalDate> observable, LocalDate oldValue, LocalDate newValue) {
                LoadData(seachTenKH.getText(), newValue == null ? "" : newValue.toString(), searchSDT.getText());
            }
        });
    }

    public void LoadData(String searchTen, String searchDate, String searchSDT) {
        System.out.println("Ten: " + searchDate);
        try{
            Connection conn = DatabaseUtil.getConnection();
            if (conn != null) {
                List<Hoadon> billList = DatabaseUtil.getAllHoadon(conn);
                System.out.println(searchDate);
                BillContainer.getChildren().clear();
                for(Hoadon hd : billList) {
                    Khachhang kh = DatabaseUtil.getKhachhangById(conn, hd.getMaKhachHang());
                    if( searchTen != null && !searchTen.isEmpty() && (kh == null || !removeDiacritics(kh.getHoTen().toLowerCase()).contains(removeDiacritics(searchTen.toLowerCase())))) continue;
                    if(searchSDT != null && !searchSDT.isEmpty() && (kh == null || !removeDiacritics(kh.getSoDienThoai().toLowerCase()).contains(removeDiacritics(searchSDT.toLowerCase())))) continue;
                    if(searchDate != null && !searchDate.isEmpty() && !hd.getNgayLap().toString().equals(searchDate)) continue;
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/UI/ContactWindow/BillWindow/BillRowWindow/BillRowWindow.fxml"));
                    Parent newContent3 = loader.load();
                    newContent3.setOnMouseClicked(this::handleAnchorPaneClick);
                    BillRowTable book = loader.getController();
                    book.setData(hd);
                    BillContainer.getChildren().add(newContent3);
                }
            }
            assert conn != null;
            conn.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @FXML
    private void btnTaoHoaDonOnAction(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/UI/ContactWindow/BillWindow/BillAddedWindow/BillAddedWindow.fxml"));
            Parent parent = loader.load();
            Stage stage = new Stage();
            Scene scene = new Scene(parent);
            stage.setScene(scene);
            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner((Stage) btnTaoHoaDon.getScene().getWindow());
            stage.showAndWait();
            System.out.println("load data");
            LoadData(seachTenKH.getText(),searchDate.getValue() == null ? "" : searchDate.getValue().toString(), searchSDT.getText());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void handleAnchorPaneClick(MouseEvent event) {
        AnchorPane clickedAnchorPane = (AnchorPane) event.getSource();
        Label label = (Label) clickedAnchorPane.lookup("#MaHD");
        System.out.println("AnchorPane được nhấp vào: " + label.getText());
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/UI/ContactWindow/BillWindow/BillPropertiesWindow/BillPropertiesWindow.fxml"));
            Parent parent = loader.load();
            BillProperties bill = loader.getController();
            bill.setData(Integer.parseInt(label.getText()));
            Stage stage = new Stage();
            Scene scene = new Scene(parent);
            stage.setScene(scene);
            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner((Stage)btnTaoHoaDon.getScene().getWindow());
            stage.showAndWait();
            System.out.println("load data");
            LoadData(seachTenKH.getText(),searchDate.getValue() == null ? "" : searchDate.getValue().toString(), searchSDT.getText());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
