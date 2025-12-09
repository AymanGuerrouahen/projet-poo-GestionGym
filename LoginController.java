package org.example.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.example.dao.HistoriqueDAO; // Vérifie que cette classe existe bien
import org.example.dao.UtilisateurDAO;
import org.example.model.Utilisateur;
import org.example.util.UserSession;

import java.io.IOException;

public class LoginController {

    @FXML private TextField txtUsername;
    @FXML private PasswordField txtPassword;
    @FXML private Label lblError;

    // Tes DAOs
    private final UtilisateurDAO userDAO = new UtilisateurDAO();

    // ATTENTION : Si l'étudiant 4 n'a pas encore fini HistoriqueDAO, commente cette ligne
    private final HistoriqueDAO historiqueDAO = new HistoriqueDAO();

    @FXML
    private void handleLogin(ActionEvent event) {
        String username = txtUsername.getText();
        String password = txtPassword.getText();

        // 1. Vérification du login (Le DAO s'occupe du BCrypt)
        Utilisateur user = userDAO.getUtilisateur(username, password);

        if (user != null) {
            // 2. Connexion réussie -> On stocke l'info dans la Session
            UserSession.getInstance().setUser(user);
            System.out.println("Connexion OK : " + user.getUsername() + " (" + user.getRole() + ")");

            // 3. (Optionnel) Enregistrement dans l'historique
            // Si cette ligne souligne en rouge, c'est que la méthode n'existe pas encore dans HistoriqueDAO.
            // Tu peux la commenter avec // au début en attendant.
            try {
                historiqueDAO.enregistrerConnexion(user.getUsername(), user.getRole());
            } catch (Exception e) {
                System.out.println("Info : Impossible d'enregistrer l'historique de connexion pour l'instant.");
            }

            // 4. Chargement du Menu Principal
            try {
                Parent root = FXMLLoader.load(getClass().getResource("/menu.fxml"));
                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

                // Configuration de la fenêtre principale
                stage.setMinWidth(1000);
                stage.setMinHeight(700);

                Scene scene = new Scene(root);
                stage.setScene(scene);
                stage.setMaximized(true); // Plein écran confortable
                stage.centerOnScreen();
                stage.show();

            } catch (IOException e) {
                e.printStackTrace();
                lblError.setText("Erreur critique : Impossible de charger le menu.fxml !");
            }
        } else {
            // 5. Échec de connexion
            lblError.setText("Identifiant ou mot de passe incorrect.");
            txtPassword.clear(); // On vide le champ mot de passe par sécurité
        }
    }
}