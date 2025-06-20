module com.virtanimal {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires org.json;
    requires jbcrypt;

    opens com.virtanimal to javafx.fxml;
    exports com.virtanimal;
    exports com.utils;
    opens com.utils to javafx.fxml;
}
