package application.bookstoremanager.classdb;

public class CtPhieunhapsach {
    private final Integer maCtPhieuNhap;
    private final Integer maPhieuNhap;
    private final Integer maSach;
    private final Integer soLuongNhap;
    private final Double donGiaNhap;
    private final Phieunhapsach phieunhapsach;
    private final Sach sach;

    public CtPhieunhapsach(Integer maCtPhieuNhap, Integer maPhieuNhap, Integer maSach,
                           Integer soLuongNhap, Double donGiaNhap, Phieunhapsach phieunhapsach, Sach sach) {
        this.maCtPhieuNhap = maCtPhieuNhap;
        this.maPhieuNhap = maPhieuNhap;
        this.maSach = maSach;
        this.soLuongNhap = soLuongNhap;
        this.donGiaNhap = donGiaNhap;
        this.phieunhapsach = phieunhapsach;
        this.sach = sach;
    }

    public Integer getMaCtPhieuNhap() {
        return this.maCtPhieuNhap;
    }

    public Integer getMaPhieuNhap() {
        return this.maPhieuNhap;
    }

    public Integer getMaSach() {
        return this.maSach;
    }

    public Integer getSoLuongNhap() {
        return this.soLuongNhap;
    }

    public Double getDonGiaNhap() {
        return this.donGiaNhap;
    }

    public Phieunhapsach getPhieunhapsach() {
        return this.phieunhapsach;
    }

    public Sach getSach() {
        return this.sach;
    }
}
