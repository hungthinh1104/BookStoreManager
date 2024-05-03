package application.bookstoremanager.classdb;

public class Thamso {
    private Integer soLuongNhapToiThieu;
    private Integer soLuongTonToiDa;
    private Integer soLuongTonToiThieu;
    private Float tiLeTinhDonGiaBan;
    private Double tienCoc;

    public Integer getSoLuongNhapToiThieu() {
        return this.soLuongNhapToiThieu;
    }
    public Integer getSoLuongTonToiDa() {
        return this.soLuongTonToiDa;
    }
    public Integer getSoLuongTonToiThieu() {
        return this.soLuongTonToiThieu;
    }
    public Float getTiLeTinhDonGiaBan() {
        return this.tiLeTinhDonGiaBan;
    }
    public Double getTienCoc() {
        return this.tienCoc;
    }
    public Thamso(Integer soLuongNhapToiThieu, Integer soLuongTonToiDa, Integer soLuongTonToiThieu, Float tileDonGiaBan, Double tienCoc) {
        this.soLuongNhapToiThieu = soLuongNhapToiThieu;
        this.soLuongTonToiDa = soLuongTonToiDa;
        this.soLuongTonToiThieu = soLuongTonToiThieu;
        this.tiLeTinhDonGiaBan = tileDonGiaBan;
        this.tienCoc = tienCoc;
    }
}
