module com.example.poo {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires java.desktop;          // Pour MySQL

    // Autorise l'accès pour le FXML (la vue)
    opens com.example.poo to javafx.fxml;

    // IMPORTANT : C'est cette ligne qui manquait pour l'erreur "IllegalAccessException"
    exports com.example.poo;
}