package application.bookstoremanager.controller.ContactWindow.BookWindow;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableView;

import java.net.URL;
import java.util.ResourceBundle;

public class BookWindow implements Initializable {

    @FXML
    private TreeTableView<Book> GridViewTable;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        TreeItem<Book> root = new TreeItem<>(new Book("Root", "A", "B", "C", "D", "E"));
        TreeItem<Book> child1 = new TreeItem<>(new Book("Alice", "A", "B", "C", "D", "E"));
        TreeItem<Book> child2 = new TreeItem<>(new Book("Alice", "A", "B", "C", "D", "E"));
        root.getChildren().addAll(child1, child2);

        GridViewTable.setRoot(root);
    }


}

class Book {
    private String Ten;
    private String TheLoai;
    private String TacGia;
    private String SoLuong;
    private String NhaXB;
    private String NamXB;

    public Book(String _Ten, String _TheLoai, String _TacGia, String _SoLuong, String _NhaXB, String _NamXB) {
        this.Ten = _Ten;
        this.TheLoai = _TheLoai;
        this.TacGia = _TacGia;
        this.SoLuong = _SoLuong;
        this.NhaXB = _NhaXB;
        this.NamXB = _NamXB;
    }

    @Override
    public String toString() {
        return "Book{" +
                "Ten='" + Ten + '\'' +
                ", TheLoai='" + TheLoai + '\'' +
                ", TacGia='" + TacGia + '\'' +
                ", SoLuong='" + SoLuong + '\'' +
                ", NhaXB='" + NhaXB + '\'' +
                ", NamXB='" + NamXB + '\'' +
                '}';
    }
}
