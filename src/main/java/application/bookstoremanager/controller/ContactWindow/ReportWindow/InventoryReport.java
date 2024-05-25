package application.bookstoremanager.controller.ContactWindow.ReportWindow;

import application.bookstoremanager.DatabaseUtil;
import application.bookstoremanager.classdb.CtHoadon;
import application.bookstoremanager.classdb.Hoadon;
import application.bookstoremanager.classdb.Sach;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.time.LocalDate;
import java.util.*;

import static application.bookstoremanager.controller.ContactWindow.BookWindow.BookTableRow.formatCurrency;
import static application.bookstoremanager.controller.ContactWindow.ReportWindow.ReportWindow.getMonthNumber;


public class InventoryReport implements Initializable {

    @FXML
    private ComboBox<String> CBNam1;

    @FXML
    private ComboBox<String> CBNam2;

    @FXML
    private ComboBox<String> CBThang1;

    @FXML
    private ComboBox<String> CBThang2;

    @FXML
    private VBox NhapContainer;

    @FXML
    private VBox TonContainer;

    @FXML
    private VBox MainContainer;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
    }

    private void InitData() {
        LocalDate dateNow = LocalDate.now();
        CBThang1.getItems().add("Tất cả");
        CBThang2.getItems().add("Tất cả");
        CBThang1.setValue("Tháng " + LocalDate.now().getMonthValue());
        CBThang2.setValue("Tháng " + LocalDate.now().getMonthValue());
        CBNam1.setValue(String.valueOf(LocalDate.now().getYear()));
        CBNam2.setValue(String.valueOf(LocalDate.now().getYear()));
        for(int i = 1; i <= 12; i++) {
            CBThang1.getItems().add("Tháng " + i);
            CBThang2.getItems().add("Tháng " + i);
        }
        for(int i = 2023; i <= LocalDate.now().getYear(); i++) {
            CBNam1.getItems().add(String.valueOf(i));
            CBNam2.getItems().add(String.valueOf(i));
        }
        CBThang1.setOnAction(event -> {
        });
        CBNam1.setOnAction(event -> {
        });
        CBThang2.setOnAction(event -> {
        });
        CBNam2.setOnAction(event -> {
        });
    }

    private void LoadDataTon(int month, int year) {
        try{
            Connection conn = DatabaseUtil.getConnection();
            if (conn != null) {

            }
            assert conn != null;
            conn.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @FXML
    protected void btnChangeToPNS_OnAction() {
        System.out.println("btnChangeToPNS_OnAction");
        try {
            Parent newContent = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/UI/ContactWindow/ReportWindow/ReportWindow.fxml")));
            Parent pane = MainContainer.getParent();
            HBox anchorPane = (HBox) pane;
            anchorPane.getChildren().removeLast();
            anchorPane.getChildren().add(newContent);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
