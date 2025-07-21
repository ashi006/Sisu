module fi.tuni.prog3.sisu {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.base;
    requires com.google.gson;
    requires transitive javafx.web;
    
    opens fi.tuni.prog3.sisu to javafx.fxml, com.google.gson;
    exports fi.tuni.prog3.sisu;
}