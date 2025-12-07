module com.poo.poo {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.poo.poo to javafx.fxml;
    exports com.poo.poo;
}