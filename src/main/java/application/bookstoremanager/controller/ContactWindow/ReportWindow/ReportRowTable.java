package application.bookstoremanager.controller.ContactWindow.ReportWindow;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class ReportRowTable {

    @FXML
    private Label Cot1;

    @FXML
    private Label Cot2;

    @FXML
    private Label Cot3;

    @FXML
    private Label Cot4;

    @FXML
    private Label Cot5;

    @FXML
    private Label Cot6;

    public void setData(String _cot1, String _cot2, String _cot3, String _cot4, String _cot5, String _cot6) {
        Cot1.setText(_cot1);
        Cot2.setText(_cot2);
        Cot3.setText(_cot3);
        Cot4.setText(_cot4);
        Cot5.setText(_cot5);
        Cot6.setText(_cot6);
    }

}
