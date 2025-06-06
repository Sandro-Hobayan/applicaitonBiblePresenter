module sandro.hobayan.applicaitonbiblepresenter {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires javafx.graphics;

    opens sandro.hobayan.applicaitonbiblepresenter to javafx.fxml;
    exports sandro.hobayan.applicaitonbiblepresenter;
}