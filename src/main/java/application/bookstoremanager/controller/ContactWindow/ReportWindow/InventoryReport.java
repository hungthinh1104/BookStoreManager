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
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
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

    private List<Map.Entry<Integer, Double>> ChiPhiNhap = new ArrayList<>();
    Map<Integer, Integer> SoLuongNhap = new TreeMap<>();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        InitData();
        LoadDataTon(LocalDate.now().getMonthValue(), LocalDate.now().getYear());
        LoadDataNhapSach(LocalDate.now().getMonthValue(), LocalDate.now().getYear());
    }

    private void InitData() {
        LocalDate dateNow = LocalDate.now();
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
                ChiPhiNhap = list;
                SoLuongNhap = mapSL;
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
    private void ExportTonKho() {
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Bao cao sach ton kho");

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
            cell1.setCellValue("Báo cáo sách tồn kho");
            sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 5));
            cell1.setCellStyle(centeredStyle);
            // Dòng 2: Tháng 5/2024
            Row row2 = sheet.createRow(1);
            Cell cell2 = row2.createCell(0);
            cell2.setCellValue((getMonthNumber(CBThang1.getValue()) == 0 ? "" : CBThang1.getValue() + "/") + CBNam1.getValue());
            sheet.addMergedRegion(new CellRangeAddress(1, 1, 0, 5));
            cell2.setCellStyle(centeredStyle);
            // Dòng 3: STT, A, B, C, D, E
            Row row3 = sheet.createRow(2);
            String[] headers = {"STT", "Tên sách", "Tác giả", "Thể loại", "Tồn đầu tháng", "Tồn cuối tháng"};
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

            try{
                Connection conn = DatabaseUtil.getConnection();
                if (conn != null) {
                    List<Baocaoton> bcList = DatabaseUtil.getBaocaotonByIdThangNam(conn, getMonthNumber(CBThang1.getValue()), Integer.parseInt(CBNam1.getValue()));
                    Integer stt = 0;
                    for (Baocaoton bc : bcList) {
                        Sach sach = DatabaseUtil.getSachById(conn, bc.getMaSach());
                        Row row = sheet.createRow(stt+3);
                        Cell cell = row.createCell(0);
                        cell.setCellValue(++stt);
                        cell.setCellStyle(borderStyle);

                        Cell cell3 = row.createCell(1);
                        cell3.setCellValue(sach.getTenSach());
                        cell3.setCellStyle(borderStyle);

                        Cell cell4 = row.createCell(2);
                        cell4.setCellValue(sach.getTacGia());
                        cell4.setCellStyle(borderStyle);

                        Cell cell5 = row.createCell(3);
                        cell5.setCellValue(sach.getTheLoai().getTenTheLoai());
                        cell5.setCellStyle(borderStyle);

                        Cell cell6 = row.createCell(4);
                        cell6.setCellValue(bc.getTonDau());
                        cell6.setCellStyle(borderStyle);

                        Cell cell7 = row.createCell(5);
                        cell7.setCellValue(bc.getTonCuoi());
                        cell7.setCellStyle(borderStyle);
                    }
                }
                assert conn != null;
                conn.close();
            }catch (Exception e){
                e.printStackTrace();
            }
            String downloadFolderPath = System.getProperty("user.home") + "/Downloads";
            String nameExport = getMonthNumber(CBThang1.getValue()) != 0 ? "bao_cao_sach_ton_kho_thang_" +  getMonthNumber(CBThang1.getValue()) + "_" + CBNam1.getValue(): "bao_cao_sach_ton_kho_nam" + CBNam1.getValue();
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

    @FXML
    private void ExportNhapKho() {
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Bao cao nhap sach");

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
            cell1.setCellValue("Báo cáo sách nhập sách");
            sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 5));
            cell1.setCellStyle(centeredStyle);
            // Dòng 2: Tháng 5/2024
            Row row2 = sheet.createRow(1);
            Cell cell2 = row2.createCell(0);
            cell2.setCellValue((getMonthNumber(CBThang2.getValue()) == 0 ? "" : CBThang2.getValue() + "/") + CBNam2.getValue());
            sheet.addMergedRegion(new CellRangeAddress(1, 1, 0, 5));
            cell2.setCellStyle(centeredStyle);
            // Dòng 3: STT, A, B, C, D, E
            Row row3 = sheet.createRow(2);
            String[] headers = {"STT", "Tên sách", "Tác giả", "Thể loại", "Số lượng nhập", "Chi phí"};
            for (int i = 0; i < headers.length; i++) {
                Cell headerCell = row3.createCell(i);
                headerCell.setCellValue(headers[i]);
                headerCell.setCellStyle(boldBorderStyle);
            }

            // Dòng 4: Các giá trị
            Integer stt = 0;
            for (Map.Entry<Integer, Double> entry : ChiPhiNhap) {
                Row row = sheet.createRow(stt+3);
                Cell cell = row.createCell(0);
                cell.setCellValue(++stt);
                cell.setCellStyle(borderStyle);
                try{
                    Connection conn = DatabaseUtil.getConnection();
                    if (conn != null) {
                        Sach sach = DatabaseUtil.getSachById(conn, entry.getKey());
                        Cell cell3 = row.createCell(1);
                        cell3.setCellValue(sach.getTenSach());
                        cell3.setCellStyle(borderStyle);

                        Cell cell4 = row.createCell(2);
                        cell4.setCellValue(sach.getTacGia());
                        cell4.setCellStyle(borderStyle);

                        Cell cell5 = row.createCell(3);
                        cell5.setCellValue(sach.getTheLoai().getTenTheLoai());
                        cell5.setCellStyle(borderStyle);

                        Cell cell6 = row.createCell(4);
                        cell6.setCellValue(SoLuongNhap.get(entry.getKey()).toString());
                        cell6.setCellStyle(borderStyle);

                        Cell cell7 = row.createCell(5);
                        cell7.setCellValue(entry.getValue());
                        cell7.setCellStyle(borderStyle);
                    }
                    assert conn != null;
                    conn.close();
                }catch (Exception e){
                    e.printStackTrace();
                }
            }


            String downloadFolderPath = System.getProperty("user.home") + "/Downloads";
            String nameExport = getMonthNumber(CBThang2.getValue()) != 0 ? "bao_cao_nhap_sach_thang_" +  getMonthNumber(CBThang2.getValue()) + "_" + CBNam2.getValue(): "bao_cao_nhap_sach_nam" + CBNam2.getValue();
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
