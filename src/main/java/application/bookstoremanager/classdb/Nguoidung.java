package application.bookstoremanager.classdb;

public class Nguoidung {
    private final String tenDangNhap;
    private final String matKhau;
    private final Integer maPhanQuyen;
    private final Phanquyen phanQuyen;
    private final String hoTen;

    public Nguoidung(String tenDangNhap, String matKhau, Integer maPhanQuyen, Phanquyen phanQuyen, String hoTen) {
        this.tenDangNhap = tenDangNhap;
        this.matKhau = matKhau;
        this.maPhanQuyen = maPhanQuyen;
        this.phanQuyen = phanQuyen;
        this.hoTen = hoTen;
    }

    public String getTenDangNhap() {
        return this.tenDangNhap;
    }

    public String getMatKhau() {
        return this.matKhau;
    }

    public Integer getMaPhanQuyen() {
        return this.maPhanQuyen;
    }

    public Phanquyen getPhanQuyen() {
        return this.phanQuyen;
    }
    public String getHoTen() {return this.hoTen;}
}
