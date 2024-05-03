package application.bookstoremanager.classdb;

public class Khachhang {
    private Integer maKhachHang;
    private String hoTen;
    private String soDienThoai;

    public Integer getMaKhachHang() {
        return this.maKhachHang;
    }
    public String getHoTen() {
        return this.hoTen;
    }
    public String getSoDienThoai() {
        return this.soDienThoai;
    }
    public Khachhang(Integer maKhachHang, String hoTen, String soDienThoai) {
        this.maKhachHang = maKhachHang;
        this.hoTen = hoTen;
        this.soDienThoai = soDienThoai;
    }
}
