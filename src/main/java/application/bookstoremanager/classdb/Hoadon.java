package application.bookstoremanager.classdb;

import java.time.LocalDate;

public class Hoadon {
    private final Integer maHoaDon;
    private final LocalDate ngayLap;
    private final Integer maKhachHang;
    private final Double tongTien;
    private final Khachhang khachHang;
    private final Integer giamGia;

    public Hoadon(Integer maHoaDon, LocalDate ngayLap, Integer maKhachHang, Double tongTien, Khachhang khachHang, Integer giamGia) {
        this.maHoaDon = maHoaDon;
        this.ngayLap = ngayLap;
        this.maKhachHang = maKhachHang;
        this.tongTien = tongTien;
        this.khachHang = khachHang;
        this.giamGia = giamGia;
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

}
