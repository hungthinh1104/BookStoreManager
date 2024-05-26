package application.bookstoremanager.controller.ContactWindow.SettingWindow;

import application.bookstoremanager.DatabaseUtil;
import application.bookstoremanager.classdb.Sach;
import application.bookstoremanager.classdb.Thamso;
import application.bookstoremanager.controller.ContactWindow.BookWindow.BookTableRow;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;

public class RegularWindow implements Initializable {

    @FXML
    private VBox MainContainer;

    @FXML
    private TextField depositInput;

    @FXML
    private TextField maxStockInput;

    @FXML
    private TextField minQuantityInput;

    @FXML
    private TextField minStockInput;

    @FXML
    private TextField priceRateInput;

    @FXML
    private Button btnconfirm;

    @FXML
    private Button btnedit;

    private boolean isEdit = true;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        depositInput.setEditable(false);
        maxStockInput.setEditable(false);
        minQuantityInput.setEditable(false);
        minStockInput.setEditable(false);
        priceRateInput.setEditable(false);
        btnedit.setStyle("-fx-background-color: #1e77fc;");
        LoadData();
    }

    private void LoadData() {
        try{
            Connection conn = DatabaseUtil.getConnection();
            if (conn != null) {
                Thamso ts = DatabaseUtil.getThamso(conn);
                if (ts != null) {
                    depositInput.setText(ts.getTienCoc().toString());
                    maxStockInput.setText(ts.getSoLuongTonToiDa().toString());
                    minStockInput.setText(ts.getSoLuongTonToiThieu().toString());
                    minQuantityInput.setText(ts.getSoLuongNhapToiThieu().toString());
                    priceRateInput.setText(ts.getTiLeTinhDonGiaBan().toString());
                }
            }
            assert conn != null;
            conn.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @FXML
    private void btnEdit_OnAction() {
        if(isEdit) {
            depositInput.setEditable(true);
            maxStockInput.setEditable(true);
            minQuantityInput.setEditable(true);
            minStockInput.setEditable(true);
            priceRateInput.setEditable(true);
            isEdit = false;
            btnedit.setText("Hủy");
            btnedit.setStyle("-fx-background-color: #f55442;");
        }
        else {
            btnedit.setText("Cập nhật");
            btnedit.setStyle("-fx-background-color: #1e77fc;");
            isEdit = true;
            LoadData();
            depositInput.setEditable(false);
            maxStockInput.setEditable(false);
            minQuantityInput.setEditable(false);
            minStockInput.setEditable(false);
            priceRateInput.setEditable(false);
        }
    }

    @FXML
    private void btnconfirmOnAction() {
        if(!isEdit) {
            btnedit.setText("Cập nhật");
            btnedit.setStyle("-fx-background-color: #1e77fc;");
            isEdit = true;
            try{
                Connection conn = DatabaseUtil.getConnection();
                if (conn != null) {
                    Thamso ts = new Thamso(Integer.parseInt(minQuantityInput.getText()),
                                           Integer.parseInt(maxStockInput.getText()),
                                           Integer.parseInt(maxStockInput.getText()),
                                           Float.parseFloat(priceRateInput.getText()),
                                           Double.parseDouble(depositInput.getText()), 0.01f);
                    System.out.println(ts.getSoLuongNhapToiThieu().toString());
                    DatabaseUtil.updateThamso(conn, ts);
                }
                assert conn != null;
                conn.close();
            }catch (Exception e){
                e.printStackTrace();
            }
            LoadData();
            depositInput.setEditable(false);
            maxStockInput.setEditable(false);
            minQuantityInput.setEditable(false);
            minStockInput.setEditable(false);
            priceRateInput.setEditable(false);

        }
    }

    @FXML
    protected void btnChangeToOtherPage_OnAction() {
        try {
            Parent newContent = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/UI/ContactWindow/SettingWindow/SettingWindow/SettingWindow.fxml")));
            Parent pane = MainContainer.getParent();
            HBox anchorPane = (HBox) pane;
            anchorPane.getChildren().removeLast();
            anchorPane.getChildren().add(newContent);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
