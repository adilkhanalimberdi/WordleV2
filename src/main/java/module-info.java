module com.example.wordlev2 {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;


    opens com.example.wordlev2 to javafx.fxml;
    exports com.example.wordlev2;
}