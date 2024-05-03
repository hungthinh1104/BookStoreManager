package application.bookstoremanager;

import application.bookstoremanager.classdb.*;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

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

    public static Sach getSachById(List<Sach> sach, int idSach){
        Sach theSach = null;
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
                Sach s = new Sach(id,name,idTheloai,tacGia,soLuong,donGia, timThay);
                sach.add(s);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return sach;
    }

    public static Phieunhapsach getPhieunhapsachById(List<Phieunhapsach> pnss, int idPhieunhapsach){
        Phieunhapsach pns = null;
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
                List<Sach> sachs = getAllSach(conn);
                Sach sach = getSachById(sachs,idMaSach);
                List<Phieunhapsach> pnss = getAllPhieunhapsach(conn);
                Phieunhapsach pns = getPhieunhapsachById(pnss,idPhieuNhap);
                CtPhieunhapsach ct = new CtPhieunhapsach(id,idPhieuNhap,idMaSach,soLuongNhap,donGiaNhap,pns,sach);
                ctpns.add(ct);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return ctpns;
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

    public static Khachhang getKhachhangById(List<Khachhang> khs, int idKhachhang){
        Khachhang kh = null;
        for(Khachhang item : khs){
            if(item.getMaKhachHang() == idKhachhang){
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
                Khachhang kh = new Khachhang(id, hoTen, sdt);
                khs.add(kh);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return khs;
    }

    public static Hoadon getHoadonById(List<Hoadon> hoadons, int idHoadon){
        Hoadon hoadon = null;
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
                List<Khachhang> khs = getAllKhachhang(conn);
                Khachhang kh = getKhachhangById(khs,maKhachHang);
                Hoadon hoadon = new Hoadon(id,ngayLap,maKhachHang,tongTien,kh);
                hoadons.add(hoadon);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return hoadons;
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
                List<Hoadon> hoadons = getAllHoadon(conn);
                Hoadon hoadon = getHoadonById(hoadons,idHoaDon);
                List<Sach> sachs = getAllSach(conn);
                Sach sach = getSachById(sachs,idSach);
                CtHoadon ctHoadon = new CtHoadon(id, idHoaDon,idSach,soLuong,donGiaBan,thanhTien,hoadon,sach);
                ctHoadonList.add(ctHoadon);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ctHoadonList;
    }

    public static Dondathang getDondathangById(List<Dondathang> dondathangs, int idDonHang){
        Dondathang dondathang = null;
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
                List<Khachhang> khs = getAllKhachhang(conn);
                Khachhang kh = getKhachhangById(khs,maKhachHang);
                Dondathang dondathang = new Dondathang(id,maKhachHang,tongTienCoc,kh);
                dondathangList.add(dondathang);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dondathangList;
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
                List<Dondathang> dondathangList = getAllDondathang(conn);
                Dondathang dondathang = getDondathangById(dondathangList,idDonHang);
                List<Sach> sachs = getAllSach(conn);
                Sach sach = getSachById(sachs,idSach);
                CtDondathang ctDondathang = new CtDondathang(id, idDonHang,idSach,dondathang,sach);
                ctDondathangList.add(ctDondathang);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ctDondathangList;
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
}
