package org.example.dao;

import org.example.model.Utilisateur;
import org.example.util.DBConnector;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UtilisateurDAO {

    /**
     * Vérifie si le login/password est correct.
     * @return L'objet Utilisateur si OK, null si mauvais mot de passe.
     */
    public Utilisateur getUtilisateur(String username, String password) {
        String sql = "SELECT * FROM users WHERE username = ? AND password = ?";

        try (Connection conn = DBConnector.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, username);
            pstmt.setString(2, password); // Attention: en production, il faudrait hacher le mdp !

            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return new Utilisateur(
                        rs.getInt("id"),
                        rs.getString("username"),
                        rs.getString("password"),
                        rs.getString("role")
                );
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null; // Retourne null si l'utilisateur n'est pas trouvé
    }
}