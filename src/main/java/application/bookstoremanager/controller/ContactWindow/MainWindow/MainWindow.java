package application.bookstoremanager.controller.ContactWindow.MainWindow;

import application.bookstoremanager.DatabaseUtil;
import application.bookstoremanager.classdb.CtHoadon;
import application.bookstoremanager.classdb.Hoadon;
import application.bookstoremanager.classdb.Sach;
import application.bookstoremanager.controller.ContactWindow.BookWindow.BookTableRow;
import application.bookstoremanager.controller.ContactWindow.MainWindow.BookCard.SachMoi;
import application.bookstoremanager.controller.ContactWindow.MainWindow.BookCard.SachThinhHanh;
import application.bookstoremanager.controller.ContactWindow.ReportWindow.ReportRowTable;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

import static application.bookstoremanager.controller.ContactWindow.BookWindow.BookTableRow.formatCurrency;
import static application.bookstoremanager.controller.ContactWindow.OrderWindow.OrderWindow.isWithinLast7Days;

public class MainWindow implements Initializable {

    @FXML
    private HBox SachMoiContainer;

    @FXML
    private GridPane SachTHContainer;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        LoadDataForNewFeed();
        LoadDataForBestSeller();
        SachTHContainer.setHgap(10); // Thiết lập khoảng cách ngang giữa các cột
        SachTHContainer.setVgap(10);
    }
    public void LoadDataForNewFeed() {
        try{
            Connection conn = DatabaseUtil.getConnection();
            if (conn != null) {
                int stt = 0;
                List<Sach> bookList = DatabaseUtil.getAllSach(conn);
                Collections.reverse(bookList);
                SachMoiContainer.getChildren().clear();
                int cnt = 1;
                for(Sach sach : bookList) {
                    cnt++;
                    if(cnt > 5) break;
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/UI/ContactWindow/MainWindow/BookCard/Card1/Card.fxml"));
                    Parent newContent3 = loader.load();
                    SachMoi book = loader.getController();
                    book.setData(sach);
                    SachMoiContainer.getChildren().add(newContent3);
                }
            }
            assert conn != null;
            conn.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void LoadDataForBestSeller() {
        try{
            Connection conn = DatabaseUtil.getConnection();
            if (conn != null) {
                Map<Integer, Integer> mapSL = new TreeMap<>();
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                LocalDate today = LocalDate.now().minusDays(7);
                List<Hoadon> hdList = DatabaseUtil.getHoadonByNL(today,conn);
                List<Integer> idHD = new ArrayList<>();
                System.out.println("hdList size: " + hdList.size());
                for(Hoadon hoadon : hdList) {
                    if(!isWithinLast7Days( hoadon.getNgayLap(), LocalDate.now())) continue;
                    idHD.add(hoadon.getMaHoaDon());
                }
                for(Integer id : idHD) {
                    List<CtHoadon> ctList = DatabaseUtil.getCtHoaDonByIdHoadon(conn,id);
                    for(CtHoadon ct : ctList) {
                        int idSach = ct.getMaSach();
                        int soLuong = mapSL.getOrDefault(idSach, 0);
                        mapSL.put(idSach, soLuong + ct.getSoLuong());
                    }
                }
                Integer stt = 0;
                int col = 0, row = 0;
                List<Map.Entry<Integer, Integer>> list = new ArrayList<>(mapSL.entrySet());
                Collections.sort(list, (entry1, entry2) -> entry2.getValue().compareTo(entry1.getValue()));
                SachTHContainer.getChildren().clear();
                for (Map.Entry<Integer, Integer> entry : list) {
                    stt++;
                    if(stt > 8) break;
                    Sach sach = DatabaseUtil.getSachById(conn, entry.getKey());
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/UI/ContactWindow/MainWindow/BookCard/Card2/Card.fxml"));
                    Parent newContent3 = loader.load();
                    SachThinhHanh book = loader.getController();
                    book.setData(sach, entry.getValue().toString());
                    SachTHContainer.add(newContent3, col, row);
                    col++;
                    if(col > 3) {
                        col = 0;
                        row++;
                    }
                }
            }
            assert conn != null;
            conn.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
