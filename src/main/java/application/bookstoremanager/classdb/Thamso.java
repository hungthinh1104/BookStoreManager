package application.bookstoremanager.classdb;

public class Thamso {
    private final Integer soLuongNhapToiThieu;
    private final Integer soLuongTonToiDa;
    private final Integer soLuongTonToiThieu;
    private final Float tiLeTinhDonGiaBan;
    private final Double tienCoc;
    private final Float tichDiem;

    public Thamso(Integer soLuongNhapToiThieu, Integer soLuongTonToiDa, Integer soLuongTonToiThieu, Float tileDonGiaBan, Double tienCoc, Float tichDiem) {
        this.soLuongNhapToiThieu = soLuongNhapToiThieu;
        this.soLuongTonToiDa = soLuongTonToiDa;
        this.soLuongTonToiThieu = soLuongTonToiThieu;
        this.tiLeTinhDonGiaBan = tileDonGiaBan;
        this.tienCoc = tienCoc;
        this.tichDiem = tichDiem;
    }

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

    public Float getTichDiem() {return this.tichDiem;}
}
