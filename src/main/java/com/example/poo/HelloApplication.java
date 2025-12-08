package com.example.poo;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import java.sql.*;

public class HelloApplication extends Application {

    // INFO BDD (Vérifie ton mot de passe !)
    private static final String URL = "jdbc:mysql://localhost:3306/projet_poo";
    private static final String USER = "root";
    private static final String PASSWORD = "";

    @Override
    public void start(Stage stage) {
        // --- ÉCRAN LOGIN ---
        Label titre = new Label("CONNEXION PROJET POO");
        titre.setStyle("-fx-font-weight: bold; -fx-font-size: 16px;");

        TextField champUser = new TextField();
        champUser.setPromptText("Identifiant (admin)");
        champUser.setMaxWidth(200);

        PasswordField champPass = new PasswordField();
        champPass.setPromptText("Mot de passe (1234)");
        champPass.setMaxWidth(200);

        Button boutonLogin = new Button("Se connecter");
        Label message = new Label();

        // Action du bouton
        boutonLogin.setOnAction(e -> {
            String user = champUser.getText();
            String pass = champPass.getText();

            if (verifierLogin(user, pass)) {
                // SI C'EST BON : On change de page !
                try {
                    // 1. Charger le fichier FXML des membres
                    FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("membre-view.fxml"));
                    Scene sceneMembre = new Scene(fxmlLoader.load(), 600, 500);

                    // 2. Mettre la nouvelle scène sur la fenêtre
                    stage.setScene(sceneMembre);
                    stage.setTitle("Gestion des Membres");
                    stage.centerOnScreen();

                } catch (Exception ex) {
                    ex.printStackTrace();
                    message.setText("Erreur : Impossible d'ouvrir la vue Membre");
                }

            } else {
                message.setStyle("-fx-text-fill: red;");
                message.setText("Identifiants incorrects !");
            }
        });

        VBox root = new VBox(15, titre, champUser, champPass, boutonLogin, message);
        root.setAlignment(Pos.CENTER);
        Scene scene = new Scene(root, 300, 250);
        stage.setTitle("Login");
        stage.setScene(scene);
        stage.show();
    }

    // Vérification SQL
    private boolean verifierLogin(String username, String password) {
        String sql = "SELECT * FROM users WHERE username = ? AND password = ?";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, username);
            stmt.setString(2, password);
            ResultSet rs = stmt.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static void main(String[] args) {
        launch();
    }
}