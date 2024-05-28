package application.bookstoremanager.classdb;

public class Khachhang {
    private final Integer maKhachHang;
    private final String hoTen;
    private final String soDienThoai;
    private final Double tichDiem;

    public Khachhang(Integer maKhachHang, String hoTen, String soDienThoai, Double tichDiem) {
        this.maKhachHang = maKhachHang;
        this.hoTen = hoTen;
        this.soDienThoai = soDienThoai;
        this.tichDiem = tichDiem;
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

    public Double getTichDiem() {return this.tichDiem;}
}
