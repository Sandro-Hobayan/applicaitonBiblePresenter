package sandro.hobayan.applicaitonbiblepresenter;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class MainController {
    @FXML
    private Label lbl;
    @FXML
    private TextField book, chapter, verse;
    @FXML
    private Button btn;

    @FXML
    public void btnClick() {
        lbl.setText("Book: " + book.getText() + "\nChapter: " + chapter.getText() + "\nVerse: " + verse.getText());
    }
}