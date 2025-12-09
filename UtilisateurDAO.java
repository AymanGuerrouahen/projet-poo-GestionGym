package org.example.dao;

import org.example.model.Utilisateur;
import org.example.util.DBConnector;
import org.mindrot.jbcrypt.BCrypt; // <--- L'import indispensable pour la sécurité
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UtilisateurDAO {

    /**
     * Vérifie si le login/password est correct.
     * @return L'objet Utilisateur si OK, null si mauvais mot de passe.
     */
    public Utilisateur getUtilisateur(String username, String passwordSaisi) {
        // CORRECTION 1 : On cherche l'utilisateur uniquement par son nom
        // On ne met PLUS le password dans le WHERE car on ne peut pas comparer du clair avec du haché en SQL directement
        String sql = "SELECT * FROM users WHERE username = ?";

        try (Connection conn = DBConnector.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, username);

            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                // CORRECTION 2 : On récupère le mot de passe haché (crypté) depuis la base
                String passwordHacheBDD = rs.getString("password");

                // CORRECTION 3 : C'est ici que la vérification se fait !
                // BCrypt compare le "1234" (passwordSaisi) avec le hash bizarre (passwordHacheBDD)
                if (BCrypt.checkpw(passwordSaisi, passwordHacheBDD)) {

                    // Si ça matche, on retourne l'utilisateur connecté
                    return new Utilisateur(
                            rs.getInt("id"),
                            rs.getString("username"),
                            passwordHacheBDD, // On garde le hash dans l'objet
                            rs.getString("role")
                    );
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Si l'utilisateur n'existe pas OU si le mot de passe est faux, on renvoie null
        return null;
    }
}