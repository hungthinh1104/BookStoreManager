package application.bookstoremanager.controller.ContactWindow.BookWindow.ReceiptBook;

import application.bookstoremanager.DatabaseUtil;
import application.bookstoremanager.classdb.CtPhieunhapsach;
import application.bookstoremanager.classdb.Phieunhapsach;
import application.bookstoremanager.classdb.Sach;
import application.bookstoremanager.controller.ContactWindow.BookWindow.ReceiptBook.BookTableRow;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
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
import java.util.stream.Collectors;

public class ReceiptBook implements Initializable {

    @FXML
    private VBox CTPNS;

    @FXML
    private VBox PNSContainer;

    @FXML
    private Button btnNhanSach;

    @FXML
    private VBox MainBookConTainer;

    @FXML
    private DatePicker searchDate;

    private int SelectedPNS;
    private AnchorPane PreSelectedPNS;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        LoadData(null);
        searchDate.valueProperty().addListener(new ChangeListener<LocalDate>() {
            @Override
            public void changed(ObservableValue<? extends LocalDate> observable, LocalDate oldValue, LocalDate newValue) {
                LoadData(newValue);
            }
        });
    }

    public void LoadData(LocalDate date) {
        try{
            Connection conn = DatabaseUtil.getConnection();
            if (conn != null) {
                List<Phieunhapsach> PNList = DatabaseUtil.getAllPhieunhapsach(conn);
                PNSContainer.getChildren().clear();
                SelectedPNS = -1;
                System.out.println("Before: " + SelectedPNS);
                for(Phieunhapsach pns: PNList) {
                    if(date != null && !pns.getNgayNhap().equals(date)) continue;
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/UI/ContactWindow/BookWindow/ReceiptBookWindow/BookTableRow/BookTableRow.fxml"));
                    Parent newContent = loader.load();
                    newContent.setOnMouseClicked(this::handleAnchorPaneClick);
                    BookTableRow controller = loader.getController();
                    controller.setData(pns);
                    if(SelectedPNS == -1) {
                        SelectedPNS = pns.getMaPhieuNhap();
                        newContent.setStyle("-fx-background-color: #F8F8F8;");
                        PreSelectedPNS = (AnchorPane) newContent;
                    }
                    PNSContainer.getChildren().add(newContent);
                }
                System.out.println("After: " + SelectedPNS);
                LoadDataPNSDetail(SelectedPNS);
            }
            assert conn != null;
            conn.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    private void handleAnchorPaneClick(MouseEvent event) {
        AnchorPane clickedAnchorPane = (AnchorPane) event.getSource();
        Label label = (Label) clickedAnchorPane.lookup("#MaPNS");
        System.out.println("AnchorPane được nhấp vào: " + label.getText());
        PreSelectedPNS.setStyle("-fx-background-color: white;");
        clickedAnchorPane.setStyle("-fx-background-color: #F8F8F8;");
        PreSelectedPNS = clickedAnchorPane;
        SelectedPNS = Integer.parseInt(label.getText());
        LoadDataPNSDetail(SelectedPNS);
    }
    private void LoadDataPNSDetail(int id) {
        if(id == -1) {
            CTPNS.getChildren().clear();
            return;
        }
        try{
            Connection conn = DatabaseUtil.getConnection();
            if (conn != null) {
                List<CtPhieunhapsach> CTList = DatabaseUtil.getAllCtPhieunhapsach(conn);
                List<CtPhieunhapsach> filteredList = CTList.stream()
                        .filter(obj -> obj.getMaPhieuNhap().equals(id))
                        .toList();
                CTPNS.getChildren().clear();
                for(CtPhieunhapsach pns: filteredList) {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/UI/ContactWindow/BookWindow/ReceiptBookWindow/DetailPane/DetailPaneForMain.fxml"));
                    Parent newContent = loader.load();
                    DetailPaneForMain controller = loader.getController();
                    controller.setData(pns);
                    CTPNS.getChildren().add(newContent);
                }
            }
            assert conn != null;
            conn.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    @FXML
    protected void btnChangeToTKS_OnAction() {
        System.out.println("btnChangeToPNS_OnAction");
        try {
            Parent newContent = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/UI/ContactWindow/BookWindow/SearchBookWindow/SearchBookWindow.fxml")));
            Parent pane = MainBookConTainer.getParent();
            HBox anchorPane = (HBox) pane;
            anchorPane.getChildren().removeLast();
            anchorPane.getChildren().add(newContent);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    protected void btnAddBook_OnAction() {
        System.out.println("btnAddBook_OnAction");
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/UI/ContactWindow/BookWindow/ReceiptBookWindow/BookInputWindow/BookInputWindow.fxml"));
            Parent parent = loader.load();
            Stage stage = new Stage();
            Scene scene = new Scene(parent);
            stage.setScene(scene);
            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner((Stage) btnNhanSach.getScene().getWindow());
            stage.showAndWait();
            System.out.println("load data");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
