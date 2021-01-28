module org.netsim {
    requires javafx.fxml;
    requires javafx.controls;
    requires static lombok;

    opens org.netsim.ui to javafx.graphics;

    exports org.netsim;
}
