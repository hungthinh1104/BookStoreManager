package application.bookstoremanager.classdb;

import java.time.LocalDate;

public class Dondathang {
    private final Integer maDonHang;
    private final Integer maKhachHang;
    private final Double tongTienCoc;
    private final Khachhang khachHang;
    private final LocalDate ngayLap;
    private final String trangThai;

    public Dondathang(Integer maDonHang, Integer maKhachHang, Double tongTienCoc, Khachhang khachHang, LocalDate ngayLap, String trangThai) {
        this.maDonHang = maDonHang;
        this.maKhachHang = maKhachHang;
        this.tongTienCoc = tongTienCoc;
        this.khachHang = khachHang;
        this.ngayLap = ngayLap;
        this.trangThai = trangThai;
    }

    public Integer getMaDonHang() {
        return this.maDonHang;
    }

    public Integer getMaKhachHang() {
        return this.maKhachHang;
    }

    public Double getTongTienCoc() {
        return this.tongTienCoc;
    }

    public Khachhang getKhachHang() {
        return this.khachHang;
    }

    public LocalDate getNgayLap() {
        return this.ngayLap;
    }

    public String getTrangThai() {return this.trangThai;}
}
