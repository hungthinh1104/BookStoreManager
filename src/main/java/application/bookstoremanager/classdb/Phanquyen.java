package application.bookstoremanager.classdb;

public class Phanquyen {
    private Integer maPhanQuyen;
    private String quyenHan;

    public Phanquyen(Integer maPhanQuyen, String quyenHan) {
        this.maPhanQuyen = maPhanQuyen;
        this.quyenHan = quyenHan;
    }
    public Integer getMaPhanQuyen() {
        return this.maPhanQuyen;
    }
    public String getQuyenHan() {
        return this.quyenHan;
    }
}
