package application.bookstoremanager.controller.ContactWindow.ReportWindow;

import application.bookstoremanager.DatabaseUtil;
import application.bookstoremanager.classdb.CtHoadon;
import application.bookstoremanager.classdb.Hoadon;
import application.bookstoremanager.classdb.Sach;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.time.LocalDate;
import java.util.*;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

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
    private ComboBox<String> CBTuan2;

    @FXML
    private VBox MainContainer;

    @FXML
    private BarChart<String, Number> Chart;

    private List<Map.Entry<Integer, Double>> DoanhThuSach = new ArrayList<>();
    private Map<Integer, Integer> SoLuongSach = new TreeMap<>();;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        InitData();
        LoadTKSach(LocalDate.now().getMonthValue(), LocalDate.now().getYear());
        LoadDataForChart(LocalDate.now().getMonthValue(), LocalDate.now().getYear());
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
            LoadTKSach(getMonthNumber(CBThang1.getValue()), Integer.parseInt(CBNam1.getValue()));
        });
        CBNam1.setOnAction(event -> {
            LoadTKSach(getMonthNumber(CBThang1.getValue()), Integer.parseInt(CBNam1.getValue()));
        });
        CBThang2.setOnAction(event -> {
            LoadDataForChart(getMonthNumber(CBThang2.getValue()), Integer.parseInt(CBNam2.getValue()));
        });
        CBNam2.setOnAction(event -> {
            LoadDataForChart(getMonthNumber(CBThang2.getValue()), Integer.parseInt(CBNam2.getValue()));
        });
        final CategoryAxis xAxis = new CategoryAxis();
        final NumberAxis yAxis = new NumberAxis();
        xAxis.setLabel("Thời gian");
        yAxis.setLabel("Doanh thu");
        ;
    }

    private void LoadTKSach(Integer Thang, Integer Nam) {
        System.out.println("Load TKSach: " + Thang + ", " + Nam);
        try{
            Connection conn = DatabaseUtil.getConnection();
            if (conn != null) {
                Map<Integer, Integer> mapSL = new TreeMap<>();
                Map<Integer, Double> mapDT = new TreeMap<>(java.util.Comparator.reverseOrder());
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
                SoLuongSach = mapSL;
                DoanhThuSach = list;
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

    private void LoadDataForChart(int month, int year) {
        try{
            Connection conn = DatabaseUtil.getConnection();
            if (conn != null) {
                Map<Integer, Double> mapDT = new TreeMap<>();;
                int cnt = 0;
                if(month == 0) cnt = 12;
                else cnt = getDaysInMonth(month, year);
                System.out.println("SO ngay: " + cnt);
                for(int i = 1; i <= cnt; i++) {
                    List<Hoadon> hdList = DatabaseUtil.getAllHoadon(conn);
                    Double TongTien = 0.0;
                    for(Hoadon hoadon : hdList) {
                        LocalDate NgayLap = hoadon.getNgayLap();
                        if(((month != 0 && i == NgayLap.getDayOfMonth() && month == NgayLap.getMonthValue()) || (month == 0 && i == NgayLap.getMonthValue())) && year == NgayLap.getYear()) {
                            TongTien += hoadon.getTongTien();
                        }
                    }
                    mapDT.put(i, TongTien);
                }
                Chart.getData().clear();
                XYChart.Series<String, Number> series = new XYChart.Series<>();
                for (Map.Entry<Integer, Double> entry : mapDT.entrySet()) {
                    String label = month == 0 ? ("Tháng " + entry.getKey().toString()) : entry.getKey() .toString();
                    series.getData().add(new XYChart.Data<>(label, entry.getValue()));
                }
                Chart.getData().add(series);

            }
            assert conn != null;
            conn.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @FXML
    private void ExportSach() {
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Bao cao doanh thu sach");

            CellStyle centeredStyle = workbook.createCellStyle();
            centeredStyle.setAlignment(HorizontalAlignment.CENTER);
            centeredStyle.setVerticalAlignment(VerticalAlignment.CENTER);

            // Đặt border cho dòng đã merge

            // Tạo CellStyle cho tiêu đề và nội dung in đậm
            CellStyle boldStyle = workbook.createCellStyle();
            Font boldFont = workbook.createFont();
            boldFont.setBold(true);
            boldStyle.setFont(boldFont);
            centeredStyle.setFont(boldFont);

            sheet.setColumnWidth(0, 2000); // Độ rộng là 5000 đơn vị
            sheet.setColumnWidth(1, 8000); // Độ rộng là 8000 đơn vị
            sheet.setColumnWidth(2, 6000); // Độ rộng là 8000 đơn vị
            sheet.setColumnWidth(3, 5000); // Độ rộng là 8000 đơn vị
            sheet.setColumnWidth(4, 4000); // Độ rộng là 8000 đơn vị
            sheet.setColumnWidth(5, 5000);
            // Tạo CellStyle cho viền
            CellStyle boldBorderStyle = workbook.createCellStyle();
            boldFont.setBold(true);
            boldBorderStyle.setFont(boldFont);
            boldBorderStyle.setBorderTop(BorderStyle.THIN);
            boldBorderStyle.setBorderBottom(BorderStyle.THIN);
            boldBorderStyle.setBorderLeft(BorderStyle.THIN);
            boldBorderStyle.setBorderRight(BorderStyle.THIN);

            CellStyle borderStyle = workbook.createCellStyle();
            borderStyle.setBorderTop(BorderStyle.THIN);
            borderStyle.setBorderBottom(BorderStyle.THIN);
            borderStyle.setBorderLeft(BorderStyle.THIN);
            borderStyle.setBorderRight(BorderStyle.THIN);

            // Dòng 1: Bao cao doanh thu sach
            Row row1 = sheet.createRow(0);
            Cell cell1 = row1.createCell(0);
            cell1.setCellValue("Báo cáo thống kê sách");
            sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 5));
            cell1.setCellStyle(centeredStyle);
            // Dòng 2: Tháng 5/2024
            Row row2 = sheet.createRow(1);
            Cell cell2 = row2.createCell(0);
            cell2.setCellValue(CBThang1.getValue() + "/" + CBNam1.getValue());
            sheet.addMergedRegion(new CellRangeAddress(1, 1, 0, 5));
            cell2.setCellStyle(centeredStyle);
            // Dòng 3: STT, A, B, C, D, E
            Row row3 = sheet.createRow(2);
            String[] headers = {"STT", "Tên sách", "Tác giả", "Thể loại", "Lượng bán ra", "Doanh thu"};
            for (int i = 0; i < headers.length; i++) {
                Cell headerCell = row3.createCell(i);
                headerCell.setCellValue(headers[i]);
                headerCell.setCellStyle(boldBorderStyle);
            }

            // Dòng 4: Các giá trị
            String[] values = {"1", "Value A", "Value B", "Value C", "Value D", "Value E"};
            Row row4 = sheet.createRow(3);
            for (int i = 0; i < values.length; i++) {
                Cell valueCell = row4.createCell(i);
                valueCell.setCellValue(values[i]);
                valueCell.setCellStyle(borderStyle);
            }

            String downloadFolderPath = System.getProperty("user.home") + "/Downloads";
            String nameExport = getMonthNumber(CBThang1.getValue()) != 0 ? "bao_cao_doanh_thu_sach_theo_thang_" +  getMonthNumber(CBThang1.getValue()) + "_" + CBNam1.getValue(): "bao_cao_doanh_thu_sach_theo_nam" + CBNam1.getValue();
            String excelFileName = nameExport + ".xlsx";
            Path excelFilePath = Paths.get(downloadFolderPath, excelFileName);
            int fileNumber = 1;
            while (Files.exists(excelFilePath)) {
                excelFileName = nameExport + " (" + fileNumber + ").xlsx";
                excelFilePath = Paths.get(downloadFolderPath, excelFileName);
                fileNumber++;
            }
            try (FileOutputStream outputStream = new FileOutputStream(excelFilePath.toString())) {
                workbook.write(outputStream);
                System.out.println("Xuất tệp Excel thành công vào thư mục Download!");
            } catch (IOException e) {
                e.printStackTrace();
            }

        } catch (IOException e) {
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
    public static int getDaysInMonth(int month, int year) {
        switch (month) {
            case 1, 3, 5, 7, 8, 10, 12:
                return 31;
            case 4, 6, 9, 11:
                return 30;
            case 2:
                if (isLeapYear(year))
                    return 29;
                else
                    return 28;
            default:
                return -1; // Trường hợp không hợp lệ
        }
    }
    @FXML
    protected void btnChangeToPNS_OnAction() {
        System.out.println("btnChangeToPNS_OnAction");
        try {
            Parent newContent = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/UI/ContactWindow/ReportWindow/InventoryReportWindow.fxml")));
            Parent pane = MainContainer.getParent();
            HBox anchorPane = (HBox) pane;
            anchorPane.getChildren().removeLast();
            anchorPane.getChildren().add(newContent);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static boolean isLeapYear(int year) {
        return (year % 4 == 0 && year % 100 != 0) || (year % 400 == 0);
    }
}

