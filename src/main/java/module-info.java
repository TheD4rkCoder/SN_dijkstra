module com.example.sn_dijkstra {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.swing;


    opens com.example.sn_dijkstra to javafx.fxml;
    exports com.example.sn_dijkstra;
}