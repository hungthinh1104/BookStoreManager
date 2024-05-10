package application.bookstoremanager.classdb;

public class Khachhang {
    private final Integer maKhachHang;
    private final String hoTen;
    private final String soDienThoai;

    public Khachhang(Integer maKhachHang, String hoTen, String soDienThoai) {
        this.maKhachHang = maKhachHang;
        this.hoTen = hoTen;
        this.soDienThoai = soDienThoai;
    }

    public Integer getMaKhachHang() {
        return this.maKhachHang;
    }

    public String getHoTen() {
        return this.hoTen;
    }

    public String getSoDienThoai() {
        return this.soDienThoai;
    }
}
