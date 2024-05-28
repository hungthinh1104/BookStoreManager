package application.bookstoremanager.classdb;

public class Baocaoton {
    private Integer maBaoCao;
    private Integer maSach;
    private Integer tonDau;
    private Integer tonCuoi;
    private Integer maThangNam;
    private Thangnam thangNam;

    public Baocaoton(Integer maBaoCao, Integer maSach, Integer tonDau, Integer tonCuoi, Integer maThangNam, Thangnam thangNam) {
        this.maBaoCao = maBaoCao;
        this.maSach = maSach;
        this.tonDau = tonDau;
        this.tonCuoi = tonCuoi;
        this.maThangNam = maThangNam;
        this.thangNam = thangNam;
    }

    public Integer getMaBaoCao() {
        return this.maBaoCao;
    }

    public Integer getMaSach() {
        return this.maSach;
    }

    public Integer getTonDau() {
        return this.tonDau;
    }

    public Integer getTonCuoi() {
        return this.tonCuoi;
    }

    public Integer getMaThangNam() {
        return this.maThangNam;
    }

    public Thangnam getThangNam() {return this.thangNam;}

}
