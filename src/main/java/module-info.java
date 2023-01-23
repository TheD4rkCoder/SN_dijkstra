module com.example.sn_dijkstra {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.sn_dijkstra to javafx.fxml;
    exports com.example.sn_dijkstra;
}