package application.bookstoremanager.classdb;

import java.time.LocalDate;

public class Phieunhapsach {
    private Integer maPhieuNhap;
    private LocalDate ngayNhap;

    public Integer getMaPhieuNhap() {
        return this.maPhieuNhap;
    }
    public LocalDate getNgayNhap() {
        return this.ngayNhap;
    }
    public Phieunhapsach(Integer maPhieuNhap, LocalDate ngayNhap) {
        this.maPhieuNhap = maPhieuNhap;
        this.ngayNhap = ngayNhap;
    }
}
