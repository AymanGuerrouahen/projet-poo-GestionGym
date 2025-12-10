package org.example.dao;

import org.example.model.Historique;
import org.example.util.DBConnector;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class HistoriqueDAO {

    // Enregistrer (Tu l'as déjà)
    public void enregistrerConnexion(String username, String role) {
        String sql = "INSERT INTO historique_connexion (username, role) VALUES (?, ?)";
        try (Connection conn = DBConnector.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, username);
            ps.setString(2, role);
            ps.executeUpdate();
        } catch (SQLException e) { e.printStackTrace(); }
    }

    // --- AJOUT : LIRE L'HISTORIQUE ---
    public List<Historique> findAll() {
        List<Historique> list = new ArrayList<>();
        // On trie par date décroissante (le plus récent en haut)
        String sql = "SELECT username, role, date_heure FROM historique_connexion ORDER BY date_heure DESC";

        try (Connection conn = DBConnector.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                // On récupère la date sous forme de String simple pour l'affichage
                String dateStr = rs.getString("date_heure");
                list.add(new Historique(
                        rs.getString("username"),
                        rs.getString("role"),
                        dateStr
                ));
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return list;
    }
}