package application.bookstoremanager.classdb;

public class CtDondathang {
    private Integer maCtDonHang;
    private Integer maDonHang;
    private Integer maSach;
    private Dondathang dondathang;
    private Sach sach;

    public CtDondathang(Integer maCtDonHang, Integer maDonHang, Integer maSach, Dondathang dondathang, Sach sach) {
        this.maCtDonHang = maCtDonHang;
        this.maDonHang = maDonHang;
        this.maSach = maSach;
        this.dondathang = dondathang;
        this.sach = sach;
    }
    public Integer getMaCtDonHang() {
        return this.maCtDonHang;
    }
    public Integer getMaDonHang() {
        return this.maDonHang;
    }
    public Integer getMaSach() {
        return this.maSach;
    }
    public Dondathang getDondathang() {return this.dondathang;}
    public Sach getSach() {return this.sach;}
}
