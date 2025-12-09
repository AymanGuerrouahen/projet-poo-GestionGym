package org.example.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import org.example.dao.MembreDAOImpl;
import org.example.model.Utilisateur;
import org.example.util.UserSession;

import java.io.IOException;
import java.util.Map;

public class MenuController {

    @FXML private Label lblBienvenue;
    @FXML private Button btnAbo;
    @FXML private Button btnMembres;
    @FXML private Button btnHistorique; // Le bouton Historique
    @FXML private PieChart pieChart;    // Le Graphique

    @FXML
    public void initialize() {
        // --- 1. GESTION DE L'UTILISATEUR (ROLES) ---
        Utilisateur user = UserSession.getInstance().getUser();

        if (user != null) {
            // Affichage du message de bienvenue
            if (lblBienvenue != null) {
                lblBienvenue.setText("Bonjour, " + user.getUsername() + " (" + user.getRole() + ")");
            }

            // Si c'est un COACH (pas un ADMIN)
            if (!"ADMIN".equals(user.getRole())) {
                // On désactive la gestion des abonnements
                if (btnAbo != null) {
                    btnAbo.setDisable(true);
                    btnAbo.setStyle("-fx-background-color: #95a5a6; -fx-text-fill: white;");
                }
                // On cache le bouton historique
                if (btnHistorique != null) {
                    btnHistorique.setVisible(false);
                }
            }
        }

        // --- 2. CHARGEMENT DU GRAPHIQUE ---
        chargerGraphique();
    }

    private void chargerGraphique() {
        if (pieChart == null) return; // Sécurité si le graphique n'est pas dans le FXML

        MembreDAOImpl membreDAO = new MembreDAOImpl();
        // Récupère les stats depuis la BDD
        Map<String, Integer> stats = membreDAO.getRepartitionAbonnements();

        ObservableList<PieChart.Data> pieData = FXCollections.observableArrayList();

        for (Map.Entry<String, Integer> entry : stats.entrySet()) {
            // Création de la part de tarte : "Standard (12)"
            String label = entry.getKey() + " (" + entry.getValue() + ")";
            pieData.add(new PieChart.Data(label, entry.getValue()));
        }

        pieChart.setData(pieData);
    }

    // --- NAVIGATION ---

    @FXML
    public void goToMembres(ActionEvent event) throws IOException {
        switchContent(event, "/membre_view.fxml");
    }

    @FXML
    public void goToAbonnements(ActionEvent event) throws IOException {
        switchContent(event, "/abonnement.fxml");
    }

    @FXML
    public void goToHistorique(ActionEvent event) throws IOException {
        switchContent(event, "/historique.fxml");
    }

    @FXML
    public void handleLogout(ActionEvent event) throws IOException {
        // Déconnexion propre
        UserSession.getInstance().cleanUserSession();

        // Retour au login (petite fenêtre)
        Parent root = FXMLLoader.load(getClass().getResource("/login.fxml"));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.setMaximized(false);
        stage.setWidth(600);
        stage.setHeight(500);
        stage.centerOnScreen();
        stage.show();
    }

    // Méthode pour changer le contenu en gardant le plein écran
    private void switchContent(ActionEvent event, String fxml) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource(fxml));
        Scene scene = ((Node) event.getSource()).getScene();
        scene.setRoot(root);
    }
}