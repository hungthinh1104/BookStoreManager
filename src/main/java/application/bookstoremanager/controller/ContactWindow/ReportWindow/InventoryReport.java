package application.bookstoremanager.controller.ContactWindow.ReportWindow;

import application.bookstoremanager.DatabaseUtil;
import application.bookstoremanager.classdb.*;
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
        InitData();
        LoadDataTon(LocalDate.now().getMonthValue(), LocalDate.now().getYear());
        LoadDataNhapSach(LocalDate.now().getMonthValue(), LocalDate.now().getYear());
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
            LoadDataTon(getMonthNumber(CBThang1.getValue()),Integer.parseInt(CBNam1.getValue()));
        });
        CBNam1.setOnAction(event -> {
            LoadDataTon(getMonthNumber(CBThang1.getValue()),Integer.parseInt(CBNam1.getValue()));
        });
        CBThang2.setOnAction(event -> {
            LoadDataNhapSach(getMonthNumber(CBThang2.getValue()),Integer.parseInt(CBNam2.getValue()));
        });
        CBNam2.setOnAction(event -> {
            LoadDataNhapSach(getMonthNumber(CBThang2.getValue()),Integer.parseInt(CBNam2.getValue()));
        });
    }

    private void LoadDataTon(int month, int year) {
        System.out.println("LoadDataTon: " + month + ", " + year);
        try{
            Connection conn = DatabaseUtil.getConnection();
            if (conn != null) {
                List<Baocaoton> bcList = DatabaseUtil.getBaocaotonByIdThangNam(conn, month, year);
                System.out.println(bcList.size());
                Integer stt = 0;
                for (Baocaoton bc : bcList) {
                    Sach sach = DatabaseUtil.getSachById(conn, bc.getMaSach());
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/UI/ContactWindow/ReportWindow/ReportBookRow.fxml"));
                    Parent newContent3 = loader.load();
                    ReportRowTable book = loader.getController();
                    book.setData((++stt).toString(), sach.getTenSach(), sach.getTacGia(), sach.getTheLoai().getTenTheLoai(), bc.getTonDau().toString(), bc.getTonCuoi().toString());
                    TonContainer.getChildren().add(newContent3);
                }
            }
            assert conn != null;
            conn.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void LoadDataNhapSach(int month, int year) {
        try{
            Connection conn = DatabaseUtil.getConnection();
            if (conn != null) {
                Map<Integer, Integer> mapSL = new TreeMap<>();
                Map<Integer, Double> mapDT = new TreeMap<>(java.util.Comparator.reverseOrder());
                List<Phieunhapsach> pnsList = DatabaseUtil.getAllPhieunhapsach(conn);
                List<Integer> idPNS = new ArrayList<>();
                for (Phieunhapsach pns : pnsList) {
                    if(pns.getNgayNhap().getMonthValue() == month && pns.getNgayNhap().getYear() == year) {
                        idPNS.add(pns.getMaPhieuNhap());
                    }
                }
                for (Integer id : idPNS) {
                    List<CtPhieunhapsach> ctList = DatabaseUtil.getCtPhieunhapsachByIdPhieunhapsach(conn,id);
                    for (CtPhieunhapsach cts : ctList) {
                        int idSach = cts.getMaSach();
                        int soLuong = mapSL.getOrDefault(idSach, 0);
                        double doanhThu = mapDT.getOrDefault(idSach, 0.0);
                        mapSL.put(idSach, soLuong + cts.getSoLuongNhap());
                        mapDT.put(idSach, doanhThu + cts.getDonGiaNhap() * cts.getSoLuongNhap());
                    }
                }
                Integer stt = 0;
                List<Map.Entry<Integer, Double>> list = new ArrayList<>(mapDT.entrySet());
                // Sắp xếp List theo giá trị value giảm dần
                Collections.sort(list, (entry1, entry2) -> entry2.getValue().compareTo(entry1.getValue()));
                NhapContainer.getChildren().clear();
                for (Map.Entry<Integer, Double> entry : list) {
                    System.out.println(entry.getValue());
                    Sach sach = DatabaseUtil.getSachById(conn, entry.getKey());
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/UI/ContactWindow/ReportWindow/ReportBookRow.fxml"));
                    Parent newContent3 = loader.load();
                    ReportRowTable book = loader.getController();
                    book.setData((++stt).toString(), sach.getTenSach(), sach.getTacGia(), sach.getTheLoai().getTenTheLoai(), mapSL.get(entry.getKey()).toString(), formatCurrency(entry.getValue()));
                    NhapContainer.getChildren().add(newContent3);
                }
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
