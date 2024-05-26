package application.bookstoremanager;

import application.bookstoremanager.classdb.*;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class Main extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/UI/LoginWindow/LoginWindow.fxml"));
            Scene scene = new Scene(loader.load());
            stage.setTitle("Bookstore Manager");
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error loading FXML file: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        Connection conn = DatabaseUtil.getConnection();
        LocalDate currentDate = LocalDate.now();
        int month = currentDate.getMonthValue() - 1;
        int year = currentDate.getYear();
        boolean flag = findThangNam(month,year,conn);
        if(!flag){
            int idThangNam = DatabaseUtil.createThangNam(conn, month, year);
            List<Sach> allSach = DatabaseUtil.getAllSach(conn);
            if(!findThangNam(month-1,year,conn)){
                for(Sach sach : allSach){
                    if(sach.getSoLuongTon() != 0){
                        DatabaseUtil.createBaocaoton(conn, sach.getMaSach(), 0,sach.getSoLuongTon(), idThangNam);
                    }
                }
            }else{
                for(Sach sach : allSach){
                    if(sach.getSoLuongTon() != 0){
                        DatabaseUtil.createBaocaoton(conn, sach.getMaSach(), getToncuoiByIdsachAndThangNam(conn,sach.getMaSach(),month-1,year),
                                sach.getSoLuongTon(), idThangNam);
                    }
                }
            }
        }
        launch();
    }

    public static boolean findThangNam(int thang, int nam, Connection conn){
        List<Thangnam> all = DatabaseUtil.getAllThangnam(conn);
        boolean flag = false;
        for(Thangnam thangnam : all) {
            if(thang == thangnam.getThang() && nam == thangnam.getNam()){
                flag = true;
                return flag;
            }
        }
        return flag;
    }

    public static int getToncuoiByIdsachAndThangNam(Connection conn, int idsach, int thang, int nam){
        List<Baocaoton> all = DatabaseUtil.getAllBaocaoton(conn);
        int tonCuoi = 0;
        for(Baocaoton baocaoton : all) {
            if(idsach == baocaoton.getMaSach() && thang == baocaoton.getThangNam().getThang() && nam == baocaoton.getThangNam().getNam()){
                tonCuoi = baocaoton.getTonCuoi();
            }
        }
        return tonCuoi;
    }
}
