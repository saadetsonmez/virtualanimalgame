module com.virtanimal {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires org.json;

    opens com.virtanimal to javafx.fxml;
    exports com.virtanimal;
}
