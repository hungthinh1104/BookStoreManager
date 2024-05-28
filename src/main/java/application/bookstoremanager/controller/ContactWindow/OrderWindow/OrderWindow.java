package application.bookstoremanager.controller.ContactWindow.OrderWindow;

import application.bookstoremanager.DatabaseUtil;
import application.bookstoremanager.classdb.Dondathang;
import application.bookstoremanager.classdb.Hoadon;
import application.bookstoremanager.classdb.Khachhang;
import application.bookstoremanager.controller.ContactWindow.BillWindow.BillProperties;
import application.bookstoremanager.controller.ContactWindow.BillWindow.BillRowTable;
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
import java.util.Collections;
import java.util.List;
import java.util.ResourceBundle;

import static application.bookstoremanager.controller.ContactWindow.BookWindow.BookSearchWindow.removeDiacritics;

public class OrderWindow implements Initializable {

    @FXML
    private VBox OrderContainer;

    @FXML
    private Button btnTaoDHg;

    @FXML
    private DatePicker searchDate;

    @FXML
    private TextField searchSDT;

    @FXML
    private TextField searchTenKH;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        LoadData("","","");
        searchTenKH.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                LoadData(newValue, searchDate.getValue() == null ? "" : searchDate.getValue().toString(), searchSDT.getText());
            }
        });
        searchSDT.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                LoadData(searchTenKH.getText(), searchDate.getValue() == null ? "" : searchDate.getValue().toString(), newValue);
            }
        });
        searchDate.valueProperty().addListener(new ChangeListener<LocalDate>() {
            @Override
            public void changed(ObservableValue<? extends LocalDate> observable, LocalDate oldValue, LocalDate newValue) {
                LoadData(searchTenKH.getText(), newValue == null ? "" : newValue.toString(), searchSDT.getText());
            }
        });
    }

    public void LoadData(String searchTen, String searchDate, String searchSDT) {
        try{
            Connection conn = DatabaseUtil.getConnection();
            if (conn != null) {
                List<Dondathang> orderList = DatabaseUtil.getAllDondathang(conn);
                Collections.reverse(orderList);
                System.out.println(searchDate);
                OrderContainer.getChildren().clear();
                for(Dondathang dh : orderList) {
                    if(searchTen != null && !searchTen.isEmpty() && (dh.getKhachHang() == null || !removeDiacritics(dh.getKhachHang().getHoTen().toLowerCase()).contains(removeDiacritics(searchTen.toLowerCase())))) continue;
                    if(searchSDT != null && !searchSDT.isEmpty() && (dh.getKhachHang() == null || !removeDiacritics(dh.getKhachHang().getSoDienThoai().toLowerCase()).contains(removeDiacritics(searchSDT.toLowerCase())))) continue;
                    if(searchDate != null && !searchDate.isEmpty() && !dh.getNgayLap().toString().equals(searchDate)) continue;
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/UI/ContactWindow/OrderWindow/OrderRowWindow/OrderRowWindow.fxml"));
                    Parent newContent3 = loader.load();
                    newContent3.setOnMouseClicked(this::handleAnchorPaneClick);
                    OrderRow book = loader.getController();
                    book.setData(dh);
                    OrderContainer.getChildren().add(newContent3);
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
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/UI/ContactWindow/OrderWindow/OrderAddedWindow/OrderAddedWindow.fxml"));
            Parent parent = loader.load();
            Stage stage = new Stage();
            Scene scene = new Scene(parent);
            stage.setScene(scene);
            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner((Stage) btnTaoDHg.getScene().getWindow());
            stage.showAndWait();
            System.out.println("load data");
            LoadData(searchTenKH.getText(),searchDate.getValue() == null ? "" : searchDate.getValue().toString(), searchSDT.getText());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void handleAnchorPaneClick(MouseEvent event) {
        AnchorPane clickedAnchorPane = (AnchorPane) event.getSource();
        Label label = (Label) clickedAnchorPane.lookup("#MaDH");
        System.out.println("AnchorPane được nhấp vào: " + label.getText());
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/UI/ContactWindow/OrderWindow/OrderPropertiesWindow/OrderPropertiesWindow.fxml"));
            Parent parent = loader.load();
            OrderProperties order = loader.getController();
            order.setData(Integer.parseInt(label.getText()));
            Stage stage = new Stage();
            Scene scene = new Scene(parent);
            stage.setScene(scene);
            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner((Stage)btnTaoDHg.getScene().getWindow());
            stage.showAndWait();
            System.out.println("load data");
            LoadData(searchTenKH.getText(),searchDate.getValue() == null ? "" : searchDate.getValue().toString(), searchSDT.getText());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
