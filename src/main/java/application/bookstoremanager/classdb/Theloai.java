package application.bookstoremanager.classdb;

public class Theloai {
    private final Integer maTheLoai;
    private final String tenTheLoai;

    public Theloai(Integer maTheLoai, String tenTheLoai) {
        this.maTheLoai = maTheLoai;
        this.tenTheLoai = tenTheLoai;
    }

    public Integer getMaTheLoai() {
        return this.maTheLoai;
    }

    public String getTenTheLoai() {
        return this.tenTheLoai;
    }
}
