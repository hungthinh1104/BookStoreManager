package application.bookstoremanager;

import application.bookstoremanager.classdb.Nguoidung;

public class GlobalVariable {
    public static Nguoidung User;
    public static void SetUser(Nguoidung user) {
        User = user;
    }
}
