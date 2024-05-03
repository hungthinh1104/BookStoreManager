package application.bookstoremanager.classdb;

public class Theloai {
    private Integer maTheLoai;
    private String tenTheLoai;

    public Integer getMaTheLoai() {
        return this.maTheLoai;
    }

    public String getTenTheLoai() {
        return this.tenTheLoai;
    }

    public Theloai(Integer maTheLoai, String tenTheLoai) {
        this.maTheLoai = maTheLoai;
        this.tenTheLoai = tenTheLoai;
    }
}
