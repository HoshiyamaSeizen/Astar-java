module com.example.a_star {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;

    opens com.example.a_star to javafx.fxml;
    exports com.example.a_star;
}