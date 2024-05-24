package application.bookstoremanager.controller.ContactWindow.ReportWindow;

import application.bookstoremanager.DatabaseUtil;
import application.bookstoremanager.classdb.CtHoadon;
import application.bookstoremanager.classdb.Hoadon;
import application.bookstoremanager.classdb.Khachhang;
import application.bookstoremanager.classdb.Sach;
import application.bookstoremanager.controller.ContactWindow.BillWindow.BillRowTable;
import application.bookstoremanager.controller.ContactWindow.BookWindow.BookTableRow;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.chart.BarChart;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.sql.Connection;
import java.time.LocalDate;
import java.util.*;

import static application.bookstoremanager.controller.ContactWindow.BookWindow.BookSearchWindow.removeDiacritics;
import static application.bookstoremanager.controller.ContactWindow.BookWindow.BookTableRow.formatCurrency;

public class ReportWindow implements Initializable {

    @FXML
    private VBox BookContainer;

    @FXML
    private ComboBox<String> CBNam1;

    @FXML
    private ComboBox<String> CBNam2;

    @FXML
    private ComboBox<String> CBThang1;

    @FXML
    private ComboBox<String> CBThang2;

    @FXML
    private BarChart<?, ?> Chart;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
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
            LoadTKSach(getMonthNumber(CBThang1.getValue()), Integer.parseInt(CBNam1.getValue()));
        });
        CBNam1.setOnAction(event -> {
            LoadTKSach(getMonthNumber(CBThang1.getValue()), Integer.parseInt(CBNam1.getValue()));
        });
        LoadTKSach(LocalDate.now().getMonthValue(), LocalDate.now().getYear());
    }
    private void LoadTKSach(Integer Thang, Integer Nam) {
        System.out.println("Load TKSach: " + Thang + ", " + Nam);
        try{
            Connection conn = DatabaseUtil.getConnection();
            if (conn != null) {
                Map<Integer, Integer> mapSL = new TreeMap<>();;
                Map<Integer, Double> mapDT = new TreeMap<>(java.util.Comparator.reverseOrder());;
                List<Hoadon> hdList = DatabaseUtil.getAllHoadon(conn);
                List<Integer> idHD = new ArrayList<>();
                for(Hoadon hoadon : hdList) {
                    LocalDate NgayLap = hoadon.getNgayLap();
                    if((Thang.equals(0) || Thang.equals(NgayLap.getMonthValue())) && Nam.equals(NgayLap.getYear())) {
                        idHD.add(hoadon.getMaHoaDon());
                    }
                }
                for(Integer id : idHD) {
                    List<CtHoadon> ctList = DatabaseUtil.getCtHoaDonByIdHoadon(conn,id);
                    for(CtHoadon ct : ctList) {
                        int idSach = ct.getMaSach();
                        int soLuong = mapSL.getOrDefault(idSach, 0);
                        double doanhThu = mapDT.getOrDefault(idSach, 0.0);
                        mapSL.put(idSach, soLuong + ct.getSoLuong());
                        mapDT.put(idSach, doanhThu + ct.getThanhTien());
                    }
                }
                Integer stt = 0;
                List<Map.Entry<Integer, Double>> list = new ArrayList<>(mapDT.entrySet());

                // Sắp xếp List theo giá trị value giảm dần
                Collections.sort(list, (entry1, entry2) -> entry2.getValue().compareTo(entry1.getValue()));
                BookContainer.getChildren().clear();
                for (Map.Entry<Integer, Double> entry : list) {
                    System.out.println(entry.getValue());
                    Sach sach = DatabaseUtil.getSachById(conn, entry.getKey());
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/UI/ContactWindow/ReportWindow/ReportBookRow.fxml"));
                    Parent newContent3 = loader.load();
                    ReportRowTable book = loader.getController();
                    book.setData((++stt).toString(), sach.getTenSach(), sach.getTacGia(), sach.getTheLoai().getTenTheLoai(), mapSL.get(entry.getKey()).toString(), formatCurrency(entry.getValue()));
                    BookContainer.getChildren().add(newContent3);
                }
            }
            assert conn != null;
            conn.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public static int getMonthNumber(String monthString) {
        switch (monthString) {
            case "Tháng 1":
                return 1;
            case "Tháng 2":
                return 2;
            case "Tháng 3":
                return 3;
            case "Tháng 4":
                return 4;
            case "Tháng 5":
                return 5;
            case "Tháng 6":
                return 6;
            case "Tháng 7":
                return 7;
            case "Tháng 8":
                return 8;
            case "Tháng 9":
                return 9;
            case "Tháng 10":
                return 10;
            case "Tháng 11":
                return 11;
            case "Tháng 12":
                return 12;
            default:
                return 0; // Trả về -1 nếu chuỗi không hợp lệ
        }
    }
}
