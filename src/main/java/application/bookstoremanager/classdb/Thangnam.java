package application.bookstoremanager.classdb;

public class Thangnam {
    private Integer maThangNam;
    private Integer thang;
    private Integer nam;

    public Thangnam(Integer maThangNam, Integer thang, Integer nam) {
        this.maThangNam = maThangNam;
        this.thang = thang;
        this.nam = nam;
    }

    public Integer getMaThangNam() {
        return this.maThangNam;
    }

    public Integer getThang() {
        return this.thang;
    }

    public Integer getNam() {
        return this.nam;
    }

}
