package application.bookstoremanager.classdb;

import java.time.LocalDate;

public class Hoadon {
    private Integer maHoaDon;
    private LocalDate ngayLap;
    private Integer maKhachHang;
    private Double tongTien;
    private Khachhang khachHang;

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
    public Khachhang getKhachHang() {return this.khachHang;}
    public Hoadon(Integer maHoaDon,LocalDate ngayLap, Integer maKhachHang, Double tongTien, Khachhang khachHang) {
        this.maHoaDon = maHoaDon;
        this.ngayLap = ngayLap;
        this.maKhachHang = maKhachHang;
        this.tongTien = tongTien;
        this.khachHang = khachHang;
    }

}
