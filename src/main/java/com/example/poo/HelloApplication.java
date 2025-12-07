
package com.example.poo;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import java.sql.*;

public class HelloApplication extends Application {

    // CONFIGURATION BDD
    private static final String URL = "jdbc:mysql://localhost:3306/projet_poo";
    private static final String USER = "root";
    private static final String PASSWORD = ""; // <--- Mets ton mot de passe ici !

    @Override
    public void start(Stage stage) {
        Label titre = new Label("LOGIN MYSQL");

        TextField champUser = new TextField();
        champUser.setPromptText("Identifiant");

        PasswordField champPass = new PasswordField();
        champPass.setPromptText("Mot de passe");

        Button boutonLogin = new Button("Se connecter");
        Label message = new Label();

        boutonLogin.setOnAction(e -> {
            String user = champUser.getText();
            String pass = champPass.getText();

            if (verifierLogin(user, pass)) {
                message.setStyle("-fx-text-fill: green;");
                message.setText("Connexion réussie !");
                // Plus tard, on ouvrira la fenêtre Membre ici
            } else {
                message.setStyle("-fx-text-fill: red;");
                message.setText("Erreur login");
            }
        });

        VBox root = new VBox(15, titre, champUser, champPass, boutonLogin, message);
        root.setAlignment(Pos.CENTER);
        Scene scene = new Scene(root, 300, 250);
        stage.setTitle("Projet POO");
        stage.setScene(scene);
        stage.show();
    }

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