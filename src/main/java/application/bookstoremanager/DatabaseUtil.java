package application.bookstoremanager;

import application.bookstoremanager.classdb.*;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

//    Cách lấy ảnh
//    byte[] imageBytes = sach.getHinhAnh();
//    BufferedImage img = null;
//    try {
//        ByteArrayInputStream bis = new ByteArrayInputStream(imageBytes);
//        img = ImageIO.read(bis);
//    } catch ( IOException e) {
//        e.printStackTrace();
//    }

public class DatabaseUtil {
    public static Connection getConnection() {
        try{
            var url = "jdbc:mysql://localhost:3306/quanlynhasach";
            var user = "root";
            var pass = "";
            return DriverManager.getConnection(url,user,pass);
        }catch(SQLException e){
            e.printStackTrace();
            return null;
        }
    }

    public static List<Theloai> getAllTheLoai(Connection conn){
        List<Theloai> theLoai = new ArrayList<Theloai>();
        try {
            Statement statement = conn.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM theloai");

            while (resultSet.next()) {
                int id = resultSet.getInt(1);
                String name = resultSet.getString(2);

                Theloai tl = new Theloai(id, name);
                theLoai.add(tl);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return theLoai;
    }

    public static Sach getSachById(Connection conn, int idSach){
        Sach theSach = null;
        List<Sach> sach = getAllSach(conn);
        for(Sach item : sach){
            if(item.getMaSach() == idSach){
                theSach = item;
                break;
            }
        }
        return theSach;
    }

    public static List<Sach> getAllSach(Connection conn){
        List<Sach> sach = new ArrayList<Sach>();
        try {
            Statement statement = conn.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM sach");

            while (resultSet.next()) {
                int id = resultSet.getInt(1);
                String name = resultSet.getString(2);
                int idTheloai = resultSet.getInt(3);
                String tacGia = resultSet.getString(4);
                int soLuong = resultSet.getInt(5);
                double donGia = resultSet.getDouble(6);
                List<Theloai> theloai = getAllTheLoai(conn);
                Theloai timThay = null;
                for (Theloai theLoaiItem : theloai) {
                    if (theLoaiItem.getMaTheLoai() == idTheloai) {
                        timThay = theLoaiItem;
                        break;
                    }
                }
                byte[] hinhAnh =  resultSet.getBytes(7);
                Sach s = new Sach(id,name,idTheloai,tacGia,soLuong,donGia, timThay, hinhAnh);
                sach.add(s);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return sach;
    }

    public static void createSach(String tenSach, int maTheLoai,String tacGia, byte[] image, Connection conn){
        try{
            String sql = "INSERT INTO sach(TenSach, MaTheLoai, TacGia, HinhAnh) VALUES (?, ?, ?, ?)";
            PreparedStatement pstmt = conn.prepareStatement(sql);

            pstmt.setString(1, tenSach);
            pstmt.setInt(2, maTheLoai);
            pstmt.setString(3, tacGia);
            pstmt.setBytes(4, image);

            pstmt.executeUpdate();
            System.out.println("Thêm mới sách thành công!");
            pstmt.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public static boolean deleteSachById(Connection conn, int idSach ){
        List<CtHoadon> ctHoadon = getAllCtHoadon(conn);
        List<CtDondathang> ctDondathang = getAllCtDondathang(conn);
        List<CtPhieunhapsach> ctPhieunhapsach = getAllCtPhieunhapsach(conn);
        int count = 0;
        for (CtPhieunhapsach thePhieunhapsach : ctPhieunhapsach) {
            if(thePhieunhapsach.getMaSach() == idSach ){
                ++count;
                break;
            }
        }
        for (CtHoadon theHoadon : ctHoadon) {
            if(theHoadon.getMaSach() == idSach ){
                ++count;
                break;
            }
        }
        for(CtDondathang theDondathang : ctDondathang){
            if(theDondathang.getMaSach() == idSach ){
                ++count;
                break;
            }
        }
        if(count == 0){
            try{
                String deleteSql = "DELETE FROM sach WHERE MaSach = ?";
                PreparedStatement deleteStmt = conn.prepareStatement(deleteSql);
                deleteStmt.setInt(1, idSach);
                deleteStmt.executeUpdate();
                return true;
            }catch (Exception e){
                e.printStackTrace();
                return false;
            }
        }else{
            return false;
        }
    }

    public static void updateSach(Connection conn, Sach sach){
        try{
            String updateQuery = "UPDATE sach SET TacGia = ?, TenSach = ?,MaTheLoai = ?  WHERE MaSach = ?";
            PreparedStatement preparedStatement = conn.prepareStatement(updateQuery);
            preparedStatement.setString(1, sach.getTacGia());
            preparedStatement.setString(2, sach.getTenSach());
            preparedStatement.setInt(3, sach.getMaTheLoai());
            preparedStatement.setInt(4, sach.getMaSach());
            preparedStatement.executeUpdate();
        }catch (Exception e){
            e.printStackTrace();
        }
    }


    public static Phieunhapsach getPhieunhapsachById(Connection conn, int idPhieunhapsach){
        Phieunhapsach pns = null;
        List<Phieunhapsach> pnss = getAllPhieunhapsach(conn);
        for(Phieunhapsach item : pnss){
            if(item.getMaPhieuNhap() == idPhieunhapsach){
                pns = item;
                break;
            }
        }
        return pns;
    }

    public static List<Phieunhapsach> getAllPhieunhapsach(Connection conn){
        List<Phieunhapsach> pns = new ArrayList<Phieunhapsach>();
        try {
            Statement statement = conn.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM phieunhapsach");

            while (resultSet.next()) {
                int id = resultSet.getInt(1);
                LocalDate ngayNhap = resultSet.getDate(2).toLocalDate();
                Phieunhapsach pn =  new Phieunhapsach(id, ngayNhap);
                pns.add(pn);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return pns;
    }

    public static int createPhieunhapsach(LocalDate ngayNhap, Connection conn){
        int idPhieunhap = 0;
        try{
            String sql = "INSERT INTO phieunhapsach(NgayNhap) VALUES (?)";
            PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            java.sql.Date sqlDate = java.sql.Date.valueOf(ngayNhap);
            pstmt.setDate(1, sqlDate);

            pstmt.executeUpdate();

            ResultSet rs = pstmt.getGeneratedKeys();
            if(rs.next()){
                idPhieunhap = rs.getInt(1);
            }
            System.out.println("Thêm mới phiếu nhập sách thành công! id:" + idPhieunhap);
            pstmt.close();
            rs.close();
        }catch (Exception e){
            e.printStackTrace();
        }
        return idPhieunhap;
    }

    public static boolean deletePhieunhapsachById(Connection conn, int idPhieunhapsach ){
        try{
            String deleteSqlct = "DELETE FROM ct_phieunhapsach WHERE MaPhieuNhap = ?";
            String deleteSql = "DELETE FROM phieunhapsach WHERE MaPhieuNhap = ?";
            PreparedStatement deleteStmtct = conn.prepareStatement(deleteSqlct);
            PreparedStatement deleteStmt = conn.prepareStatement(deleteSql);
            deleteStmtct.setInt(1, idPhieunhapsach);
            deleteStmt.setInt(1, idPhieunhapsach);
            deleteStmtct.executeUpdate();
            deleteStmt.executeUpdate();
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    public static List<CtPhieunhapsach> getAllCtPhieunhapsach(Connection conn){
        List<CtPhieunhapsach> ctpns = new ArrayList<CtPhieunhapsach>();
        try {
            Statement statement = conn.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM ct_phieunhapsach");

            while (resultSet.next()) {
                int id = resultSet.getInt(1);
                int idPhieuNhap = resultSet.getInt(2);
                int idMaSach = resultSet.getInt(3);
                int soLuongNhap = resultSet.getInt(4);
                double donGiaNhap = resultSet.getDouble(5);
                Sach sach = getSachById(conn,idMaSach);
                Phieunhapsach pns = getPhieunhapsachById(conn,idPhieuNhap);
                CtPhieunhapsach ct = new CtPhieunhapsach(id,idPhieuNhap,idMaSach,soLuongNhap,donGiaNhap,pns,sach);
                ctpns.add(ct);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return ctpns;
    }

    public static List<CtPhieunhapsach> getCtPhieunhapsachByIdPhieunhapsach(Connection conn, int idPhieunhapsach){
        List<CtPhieunhapsach> ctPhieunhapsachList = new ArrayList<CtPhieunhapsach>();
        List<CtPhieunhapsach> ctpns = getAllCtPhieunhapsach(conn);
        for(CtPhieunhapsach item : ctpns){
            if(item.getMaPhieuNhap() == idPhieunhapsach){
                ctPhieunhapsachList.add(item);
            }
        }
        return ctPhieunhapsachList;
    }

    public static void createCtPhieunhapsach(Connection conn, int maSach, int maPhieuNhap,int soLuongNhap, double donGiaNhap){
        try{
            String sql = "INSERT INTO ct_phieunhapsach(MaPhieuNhap, MaSach, SoLuongNhap, DonGiaNhap) VALUES (?, ?, ?, ?)";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(2, maSach);
            pstmt.setInt(1, maPhieuNhap);
            pstmt.setInt(3, soLuongNhap);
            pstmt.setDouble(4, donGiaNhap);
            pstmt.executeUpdate();
            pstmt.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public static Thamso getThamso(Connection conn){
        Thamso thamso = null;
        List<Thamso> tss = new ArrayList<Thamso>();
        try {
            Statement statement = conn.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM thamso");
            while (resultSet.next()) {
                int nhapToiThieu = resultSet.getInt(1);
                int tonToiDa = resultSet.getInt(2);
                int tonToiThieu = resultSet.getInt(3);
                float tiLe = resultSet.getFloat(4);
                double tienCoc = resultSet.getDouble(5);
                Thamso ts = new Thamso(nhapToiThieu,tonToiDa, tonToiThieu, tiLe, tienCoc);
                tss.add(ts);
            }
            thamso = tss.getFirst();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return thamso;
    }

    public static void updateThamso(Connection conn, Thamso thamso){
        try{
            String updateQuery = "UPDATE thamso SET SoLuongNhapToiThieu = ?, SoLuongTonToiDa = ?," +
                    "SoLuongTonToiThieu = ?, TiLeTinhDonGiaBan = ?, TienCoc = ? ";
            PreparedStatement preparedStatement = conn.prepareStatement(updateQuery);
            preparedStatement.setInt(1, thamso.getSoLuongNhapToiThieu());
            preparedStatement.setInt(2, thamso.getSoLuongTonToiDa());
            preparedStatement.setInt(3, thamso.getSoLuongTonToiThieu());
            preparedStatement.setFloat(4, thamso.getTiLeTinhDonGiaBan());
            preparedStatement.setDouble(5, thamso.getTienCoc());
            preparedStatement.executeUpdate();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public static Khachhang getKhachhangById(Connection conn, int idKhachhang){
        Khachhang kh = null;
        List<Khachhang> khs = getAllKhachhang(conn);
        for(Khachhang item : khs){
            if(item.getMaKhachHang() == idKhachhang){
                kh = item;
                break;
            }
        }
        return kh;
    }

    public static Khachhang getKhachhangBySdt(Connection conn, String sdt ){
        Khachhang kh = null;
        List<Khachhang> khs = getAllKhachhang(conn);
        for(Khachhang item : khs){
            if(item.getSoDienThoai().equals(sdt) ){
                kh = item;
                break;
            }
        }
        return kh;
    }

    public static List<Khachhang> getAllKhachhang(Connection conn){
        List<Khachhang> khs = new ArrayList<Khachhang>();
        try {
            Statement statement = conn.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM khachhang");

            while (resultSet.next()) {
                int id = resultSet.getInt(1);
                String hoTen = resultSet.getString(2);
                String sdt = resultSet.getString(3);
                double tichDiem = resultSet.getDouble(4);
                Khachhang kh = new Khachhang(id, hoTen, sdt, tichDiem);
                khs.add(kh);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return khs;
    }

    public static void createKhachhang(Connection conn, String hoTen, String sdt, double tichDiem){
        try{
            String sql = "INSERT INTO khachhang(HoTen, SoDienThoai, TichDiem) VALUES (?, ?, ?)";
            PreparedStatement pstmt = conn.prepareStatement(sql);

            pstmt.setString(1, hoTen);
            pstmt.setString(2, sdt);
            pstmt.setDouble(3, tichDiem);

            pstmt.executeUpdate();
            System.out.println("Thêm mới Khách hàng thành công!");
            pstmt.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public static boolean deleteKhachById(Connection conn, int idKhachHang ){
        List<Hoadon> hoadons = getAllHoadon(conn);
        List<Dondathang> dondathangs = getAllDondathang(conn);
        int count = 0;
        for(Hoadon hoadon : hoadons){
            if (hoadon.getMaKhachHang() == idKhachHang){
                ++count;
                break;
            }
        }
        for(Dondathang dondathang : dondathangs){
            if (dondathang.getMaKhachHang() == idKhachHang){
                ++count;
                break;
            }
        }
        if(count == 0){
            try{
                String deleteSql = "DELETE FROM khachhang WHERE MaKhachHang = ?";
                PreparedStatement deleteStmt = conn.prepareStatement(deleteSql);
                deleteStmt.setInt(1, idKhachHang);
                deleteStmt.executeUpdate();
                return true;
            }catch (Exception e){
                e.printStackTrace();
                return false;
            }
        }else{
            return false;
        }
    }

    public static void updateKhachhang(Connection conn, Khachhang khachhang){
        try{
            String updateQuery = "UPDATE khachhang SET HoTen = ?, SoDienThoai = ?,TichDiem = ? WHERE MaKhachHang = ?";
            PreparedStatement preparedStatement = conn.prepareStatement(updateQuery);
            preparedStatement.setString(1, khachhang.getHoTen());
            preparedStatement.setString(2, khachhang.getSoDienThoai());
            preparedStatement.setInt(4, khachhang.getMaKhachHang());
            preparedStatement.setDouble(3, khachhang.getTichDiem());
            preparedStatement.executeUpdate();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public static Hoadon getHoadonById(Connection conn, int idHoadon){
        Hoadon hoadon = null;
        List<Hoadon> hoadons = getAllHoadon(conn);
        for(Hoadon item : hoadons){
            if(item.getMaHoaDon() == idHoadon){
                hoadon = item;
                break;
            }
        }
        return hoadon;
    }

    public static List<Hoadon> getAllHoadon(Connection conn){
        List<Hoadon> hoadons = new ArrayList<Hoadon>();
        try {
            Statement statement = conn.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM hoadon");

            while (resultSet.next()) {
                int id = resultSet.getInt(1);
                LocalDate ngayLap = resultSet.getDate(2).toLocalDate();
                int maKhachHang = resultSet.getInt(3);
                double tongTien = resultSet.getDouble(4);
                int giamGia = resultSet.getInt(5);
                Khachhang kh = getKhachhangById(conn,maKhachHang);
                Hoadon hoadon = new Hoadon(id,ngayLap,maKhachHang,tongTien,kh,giamGia);
                hoadons.add(hoadon);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return hoadons;
    }

    public static int createHoadon(LocalDate ngayLap, int maKhachHang, double giamGia, Connection conn){
        int idHoaDon = 0;
        try{
            String sql = "INSERT INTO hoadon(NgayLap, MaKhachHang, GiamGia) VALUES (?, ?, ?)";
            PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            java.sql.Date sqlDate = java.sql.Date.valueOf(ngayLap);
            pstmt.setDate(1, sqlDate);
            pstmt.setInt(2, maKhachHang);
            pstmt.setDouble(3, giamGia);

            pstmt.executeUpdate();

            ResultSet rs = pstmt.getGeneratedKeys();
            if(rs.next()){
                idHoaDon = rs.getInt(1);
            }
            System.out.println("Thêm mới hóa đơn thành công! id:" + idHoaDon);
            pstmt.close();
            rs.close();
        }catch (Exception e){
            e.printStackTrace();
        }
        return idHoaDon;
    }

    public static int createHoadonForNull(LocalDate ngayLap, Connection conn){
        int idHoaDon = 0;
        try{
            String sql = "INSERT INTO hoadon(NgayLap) VALUES (?)";
            PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            java.sql.Date sqlDate = java.sql.Date.valueOf(ngayLap);
            pstmt.setDate(1, sqlDate);

            pstmt.executeUpdate();

            ResultSet rs = pstmt.getGeneratedKeys();
            if(rs.next()){
                idHoaDon = rs.getInt(1);
            }
            System.out.println("Thêm mới hóa đơn thành công! id:" + idHoaDon);
            pstmt.close();
            rs.close();
        }catch (Exception e){
            e.printStackTrace();
        }
        return idHoaDon;
    }

    public static boolean deleteHoadonById(Connection conn, int idHoadon ){
        try{
            String deleteSqlct = "DELETE FROM ct_hoadon WHERE MaHoaDon = ?";
            String deleteSql = "DELETE FROM hoadon WHERE MaHoaDon = ?";
            PreparedStatement deleteStmtct = conn.prepareStatement(deleteSqlct);
            PreparedStatement deleteStmt = conn.prepareStatement(deleteSql);
            deleteStmtct.setInt(1, idHoadon);
            deleteStmt.setInt(1, idHoadon);
            deleteStmtct.executeUpdate();
            deleteStmt.executeUpdate();
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    public static List<CtHoadon> getAllCtHoadon(Connection conn){
        List<CtHoadon> ctHoadonList = new ArrayList<CtHoadon>();
        try {
            Statement statement = conn.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM ct_hoadon");

            while (resultSet.next()) {
                int id = resultSet.getInt(1);
                int idHoaDon = resultSet.getInt(2);
                int idSach = resultSet.getInt(3);
                int soLuong = resultSet.getInt(4);
                double donGiaBan = resultSet.getDouble(5);
                double thanhTien = resultSet.getDouble(6);
                Hoadon hoadon = getHoadonById(conn,idHoaDon);
                Sach sach = getSachById(conn,idSach);
                CtHoadon ctHoadon = new CtHoadon(id, idHoaDon,idSach,soLuong,donGiaBan,thanhTien,hoadon,sach);
                ctHoadonList.add(ctHoadon);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ctHoadonList;
    }

    public static List<CtHoadon> getCtHoaDonByIdHoadon(Connection conn, int idHoadon){
        List<CtHoadon> ctHoadonList = new ArrayList<CtHoadon>();
        List<CtHoadon> cthd = getAllCtHoadon(conn);
        for(CtHoadon item : cthd){
            if(item.getMaHoaDon() == idHoadon){
                ctHoadonList.add(item);
            }
        }
        return ctHoadonList;
    }

    public static void createCtHoadon(Connection conn, int maSach, int maHoadon,int soLuong){
        try{
            String sql = "INSERT INTO ct_hoadon(MaHoaDon, MaSach, SoLuong) VALUES (?, ?, ?)";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(2, maSach);
            pstmt.setInt(1, maHoadon);
            pstmt.setInt(3, soLuong);
            pstmt.executeUpdate();
            pstmt.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public static Dondathang getDondathangById(Connection conn, int idDonHang){
        Dondathang dondathang = null;
        List<Dondathang> dondathangs = getAllDondathang(conn);
        for(Dondathang item : dondathangs){
            if(item.getMaDonHang() == idDonHang){
                dondathang = item;
                break;
            }
        }
        return dondathang;
    }

    public static List<Dondathang> getAllDondathang(Connection conn){
        List<Dondathang> dondathangList = new ArrayList<Dondathang>();
        try {
            Statement statement = conn.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM dondathang");

            while (resultSet.next()) {
                int id = resultSet.getInt(1);
                int maKhachHang = resultSet.getInt(2);
                double tongTienCoc = resultSet.getDouble(3);
                LocalDate ngayLap = resultSet.getDate(4).toLocalDate();
                String trangThai = resultSet.getString(5);
                Khachhang kh = getKhachhangById(conn,maKhachHang);
                Dondathang dondathang = new Dondathang(id,maKhachHang,tongTienCoc,kh, ngayLap, trangThai);
                dondathangList.add(dondathang);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dondathangList;
    }

    public static int createDondathang( int maKhachHang, Connection conn, LocalDate ngayLap){
        int idDondathang = 0;
        try{
            String sql = "INSERT INTO dondathang(MaKhachHang, NgayLap) VALUES (?,?)";
            PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            pstmt.setInt(1, maKhachHang);
            java.sql.Date sqlDate = java.sql.Date.valueOf(ngayLap);
            pstmt.setDate(2, sqlDate);
            pstmt.executeUpdate();

            ResultSet rs = pstmt.getGeneratedKeys();
            if(rs.next()){
                idDondathang = rs.getInt(1);
            }
            System.out.println("Thêm mới đơn đặt hàng thành công! id:" + idDondathang);
            pstmt.close();
            rs.close();
        }catch (Exception e){
            e.printStackTrace();
        }
        return idDondathang;
    }

    public static boolean deleteDondathangById(Connection conn, int idDonhang ){
        try{
            String deleteSqlct = "DELETE FROM ct_dondathang WHERE MaDonHang = ?";
            String deleteSql = "DELETE FROM dondathang WHERE MaDonHang = ?";
            PreparedStatement deleteStmtct = conn.prepareStatement(deleteSqlct);
            PreparedStatement deleteStmt = conn.prepareStatement(deleteSql);
            deleteStmtct.setInt(1, idDonhang);
            deleteStmt.setInt(1, idDonhang);
            deleteStmtct.executeUpdate();
            deleteStmt.executeUpdate();
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    public static List<Dondathang> getDonhangByTrangThai(Connection conn, String trangThai){
        List<Dondathang> dondathangList = new ArrayList<Dondathang>();
        List<Dondathang> allDonhang = getAllDondathang(conn);
        for(Dondathang item : allDonhang){
            if(item.getTrangThai().equals(trangThai)){
                dondathangList.add(item);
            }
        }
        return dondathangList;
    }

    public static void updateDonHang(Connection conn, int idDonhang, String trangThai){
        try{
            String updateQuery = "UPDATE dondathang SET TrangThai = ?  WHERE MaDonHang = ?";
            PreparedStatement preparedStatement = conn.prepareStatement(updateQuery);
            preparedStatement.setString(1,trangThai);
            preparedStatement.setInt(2,idDonhang);
            preparedStatement.executeUpdate();
            preparedStatement.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public static List<CtDondathang> getAllCtDondathang(Connection conn){
        List<CtDondathang> ctDondathangList = new ArrayList<CtDondathang>();
        try {
            Statement statement = conn.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM ct_dondathang");

            while (resultSet.next()) {
                int id = resultSet.getInt(1);
                int idDonHang = resultSet.getInt(2);
                int idSach = resultSet.getInt(3);
                int soLuong = resultSet.getInt(4);
                String trangThai = resultSet.getString(5);
                Dondathang dondathang = getDondathangById(conn,idDonHang);
                Sach sach = getSachById(conn,idSach);
                CtDondathang ctDondathang = new CtDondathang(id, idDonHang,idSach,dondathang,sach, soLuong, trangThai);
                ctDondathangList.add(ctDondathang);
                resultSet.close();
                statement.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ctDondathangList;
    }

    public static List<CtDondathang> getCtDondathangByIdDondathang(Connection conn, int idDondathang){
        List<CtDondathang> ctDondathangList = new ArrayList<CtDondathang>();
        List<CtDondathang> ctdh = getAllCtDondathang(conn);
        for(CtDondathang item : ctdh){
            if(item.getMaDonHang() == idDondathang){
                ctDondathangList.add(item);
            }
        }
        return ctDondathangList;
    }

    public static void createCtDondathang(Connection conn, int maSach, int maDonhang,int soLuong, String TrangThai){
        try{
            String sql = "INSERT INTO ct_dondathang(MaDonHang, MaSach, SoLuong, TrangThai) VALUES (?, ?, ?, ?)";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(2, maSach);
            pstmt.setInt(1, maDonhang);
            pstmt.setInt(3, soLuong);
            pstmt.setString(4, TrangThai);
            pstmt.executeUpdate();
            pstmt.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public static void updateCtDonHang(Connection conn, int idDonhang, String trangThai, int MaSach){
        try{
            String updateQuery = "UPDATE ct_dondathang SET TrangThai = ?  WHERE MaDonHang = ? AND MaSach = ?";
            PreparedStatement preparedStatement = conn.prepareStatement(updateQuery);
            preparedStatement.setString(1,trangThai);
            preparedStatement.setInt(2,idDonhang);
            preparedStatement.setInt(3,MaSach);
            preparedStatement.executeUpdate();
            preparedStatement.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public static List<Phanquyen> getAllPhanquyen(Connection conn){
        List<Phanquyen> phanquyens = new ArrayList<Phanquyen>();
        try {
            Statement statement = conn.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM phanquyen");

            while (resultSet.next()) {
                int id = resultSet.getInt(1);
                String quyenHan = resultSet.getString(2);
                Phanquyen phanquyen = new Phanquyen(id, quyenHan);
                phanquyens.add(phanquyen);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return phanquyens;
    }

    public static List<Nguoidung> getAllNguoidung(Connection conn){
        List<Nguoidung> nguoidungList = new ArrayList<Nguoidung>();
        try {
            Statement statement = conn.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM nguoidung");

            while (resultSet.next()) {
                String tenDangNhap = resultSet.getString(1);
                String matKhau = resultSet.getString(2);
                int maPhanQuyen = resultSet.getInt(3);
                List<Phanquyen> phanquyenList = getAllPhanquyen(conn);
                Phanquyen phanquyen = null;
                for(Phanquyen item : phanquyenList){
                    if(item.getMaPhanQuyen() == maPhanQuyen){
                        phanquyen = item;
                        break;
                    }
                }
                Nguoidung nguoidung = new Nguoidung(tenDangNhap,matKhau,maPhanQuyen,phanquyen);
                nguoidungList.add(nguoidung);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return nguoidungList;
    }

    public static void updateNguoidung(Connection conn, Nguoidung nguoidung){
        try{
            String updateQuery = "UPDATE nguoidung SET MatKhau = ? WHERE TenDangNhap = ?";
            PreparedStatement preparedStatement = conn.prepareStatement(updateQuery);
            preparedStatement.setString(1, nguoidung.getMatKhau());
            preparedStatement.setString(2, nguoidung.getTenDangNhap());
            preparedStatement.executeUpdate();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
