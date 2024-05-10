package application.bookstoremanager.classdb;

public class Dondathang {
    private final Integer maDonHang;
    private final Integer maKhachHang;
    private final Double tongTienCoc;
    private final Khachhang khachHang;

    public Dondathang(Integer maDonHang, Integer maKhachHang, Double tongTienCoc, Khachhang khachHang) {
        this.maDonHang = maDonHang;
        this.maKhachHang = maKhachHang;
        this.tongTienCoc = tongTienCoc;
        this.khachHang = khachHang;
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
}
