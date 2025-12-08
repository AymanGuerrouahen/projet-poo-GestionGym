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

    // --- NOUVEAU : On stocke le rôle ici pour que tout le monde puisse le lire ---
    public static String ROLE_CONNECTE = "";

    private static final String URL = "jdbc:mysql://localhost:3306/projet_poo";
    private static final String USER = "root";
    private static final String PASSWORD = "";

    @Override
    public void start(Stage stage) {
        Label titre = new Label("CONNEXION SECURISEE");
        titre.setStyle("-fx-font-weight: bold; -fx-font-size: 16px;");

        TextField champUser = new TextField();
        champUser.setPromptText("Identifiant");
        champUser.setMaxWidth(200);

        PasswordField champPass = new PasswordField();
        champPass.setPromptText("Mot de passe");
        champPass.setMaxWidth(200);

        Button boutonLogin = new Button("Se connecter");
        Label message = new Label();

        boutonLogin.setOnAction(e -> {
            // On essaie de récupérer le rôle depuis la BDD
            String roleTrouve = recupererRole(champUser.getText(), champPass.getText());

            if (roleTrouve != null) {
                // C'EST GAGNÉ !
                ROLE_CONNECTE = roleTrouve; // On sauvegarde le rôle (ex: "ADMIN" ou "USER")

                try {
                    FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("membre-view.fxml"));
                    Scene sceneMembre = new Scene(fxmlLoader.load(), 600, 550);
                    stage.setScene(sceneMembre);
                    stage.setTitle("Gestion - Connecté en tant que " + roleTrouve);
                    stage.centerOnScreen();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }

            } else {
                message.setStyle("-fx-text-fill: red;");
                message.setText("Identifiants incorrects !");
            }
        });

        VBox root = new VBox(15, titre, champUser, champPass, boutonLogin, message);
        root.setAlignment(Pos.CENTER);
        Scene scene = new Scene(root, 300, 250);
        stage.setScene(scene);
        stage.show();
    }

    // Cette fonction renvoie le Rôle (String) ou null si pas trouvé
    private String recupererRole(String username, String password) {
        String sql = "SELECT role FROM users WHERE username = ? AND password = ?";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, username);
            stmt.setString(2, password);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return rs.getString("role"); // On retourne "ADMIN" ou "USER"
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null; // Échec
    }

    public static void main(String[] args) {
        launch();
    }
}