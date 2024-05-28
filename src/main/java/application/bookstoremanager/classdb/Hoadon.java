package application.bookstoremanager.classdb;

import java.time.LocalDate;

public class Hoadon {
    private final Integer maHoaDon;
    private final LocalDate ngayLap;
    private final Integer maKhachHang;
    private final Double tongTien;
    private final Khachhang khachHang;
    private final Integer giamGia;
    private final String tenDangNhap;
    private final Nguoidung user;

    public Hoadon(Integer maHoaDon, LocalDate ngayLap, Integer maKhachHang, Double tongTien,
                  Khachhang khachHang, Integer giamGia, String tenDangNhap, Nguoidung user) {
        this.maHoaDon = maHoaDon;
        this.ngayLap = ngayLap;
        this.maKhachHang = maKhachHang;
        this.tongTien = tongTien;
        this.khachHang = khachHang;
        this.giamGia = giamGia;
        this.tenDangNhap = tenDangNhap;
        this.user = user;
    }

    public Integer getMaHoaDon() {
        return this.maHoaDon;
    }

    public LocalDate getNgayLap() {
        return this.ngayLap;
    }

    public Integer getMaKhachHang() {
        return this.maKhachHang;
    }

    public Double getTongTien() {
        return this.tongTien;
    }

    public Integer getGiamGia() { return this.giamGia; }

    public Khachhang getKhachHang() {
        return this.khachHang;
    }

    public String getTenDangNhap() {return this.tenDangNhap;}

    public Nguoidung getUser() {return this.user;}

}
