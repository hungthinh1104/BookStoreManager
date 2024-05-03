package application.bookstoremanager.classdb;

public class CtHoadon {
    private Integer maCtHoaDon;
    private Integer maHoaDon;
    private Integer maSach;
    private Integer soLuong;
    private Double donGiaBan;
    private Double thanhTien;
    private Hoadon hoadon;
    private Sach sach;

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
    public Hoadon getHoadon() {return this.hoadon;}
    public Sach getSach() {return this.sach;}
    public CtHoadon(Integer maCtHoaDon, Integer maHoaDon, Integer maSach, Integer soLuong,
        Double donGiaBan, Double thanhTien, Hoadon hoadon, Sach sach){
        this.maCtHoaDon = maCtHoaDon;
        this.maHoaDon = maHoaDon;
        this.maSach = maSach;
        this.soLuong = soLuong;
        this.donGiaBan = donGiaBan;
        this.thanhTien = thanhTien;
        this.hoadon = hoadon;
        this.sach = sach;
    }
}
