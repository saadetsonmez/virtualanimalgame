module com.virtanimal {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires org.json; // <-- GEREKEN SATIR

    opens com.virtanimal to javafx.fxml;
    exports com.virtanimal;
}
