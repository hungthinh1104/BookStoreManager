package application.bookstoremanager.classdb;

public class Nguoidung {
    private String tenDangNhap;
    private String matKhau;
    private Integer maPhanQuyen;
    private Phanquyen phanQuyen;

    public Nguoidung(String tenDangNhap, String matKhau, Integer maPhanQuyen, Phanquyen phanQuyen) {
        this.tenDangNhap = tenDangNhap;
        this.matKhau = matKhau;
        this.maPhanQuyen = maPhanQuyen;
        this.phanQuyen = phanQuyen;
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
    public Phanquyen getPhanQuyen() {return this.phanQuyen;}
}
