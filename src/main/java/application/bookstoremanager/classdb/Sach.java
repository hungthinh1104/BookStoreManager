package application.bookstoremanager.classdb;

public class Sach {
    private final Integer maSach;
    private final String tenSach;
    private final Integer maTheLoai;
    private final String tacGia;
    private final Integer soLuongTon;
    private final Double donGia;
    private final Theloai theLoai;

    public Sach(Integer maSach, String tenSach, Integer maTheLoai, String tacGia, Integer soLuongTon, Double donGia, Theloai theLoai) {
        this.maSach = maSach;
        this.tenSach = tenSach;
        this.maTheLoai = maTheLoai;
        this.tacGia = tacGia;
        this.soLuongTon = soLuongTon;
        this.donGia = donGia;
        this.theLoai = theLoai;
    }

    public Integer getMaSach() {
        return this.maSach;
    }

    public String getTenSach() {
        return this.tenSach;
    }

    public Integer getMaTheLoai() {
        return this.maTheLoai;
    }

    public String getTacGia() {
        return this.tacGia;
    }

    public Integer getSoLuongTon() {
        return this.soLuongTon;
    }

    public Double getDonGia() {
        return this.donGia;
    }

    public Theloai getTheLoai() {
        return this.theLoai;
    }
}
