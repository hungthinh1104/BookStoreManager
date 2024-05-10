package application.bookstoremanager.classdb;

public class CtHoadon {
    private final Integer maCtHoaDon;
    private final Integer maHoaDon;
    private final Integer maSach;
    private final Integer soLuong;
    private final Double donGiaBan;
    private final Double thanhTien;
    private final Hoadon hoadon;
    private final Sach sach;

    public CtHoadon(Integer maCtHoaDon, Integer maHoaDon, Integer maSach, Integer soLuong,
                    Double donGiaBan, Double thanhTien, Hoadon hoadon, Sach sach) {
        this.maCtHoaDon = maCtHoaDon;
        this.maHoaDon = maHoaDon;
        this.maSach = maSach;
        this.soLuong = soLuong;
        this.donGiaBan = donGiaBan;
        this.thanhTien = thanhTien;
        this.hoadon = hoadon;
        this.sach = sach;
    }

    public Integer getMaCtHoaDon() {
        return this.maCtHoaDon;
    }

    public Integer getMaHoaDon() {
        return this.maHoaDon;
    }

    public Integer getMaSach() {
        return this.maSach;
    }

    public Integer getSoLuong() {
        return this.soLuong;
    }

    public Double getDonGiaBan() {
        return this.donGiaBan;
    }

    public Double getThanhTien() {
        return this.thanhTien;
    }

    public Hoadon getHoadon() {
        return this.hoadon;
    }

    public Sach getSach() {
        return this.sach;
    }
}
