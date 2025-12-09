package org.example.dao;

import org.example.model.Abonnement;
import org.example.util.DBConnector;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AbonnementDAOImpl {

    public List<Abonnement> findAll() {
        List<Abonnement> list = new ArrayList<>();
        try (Connection conn = DBConnector.getConnection();
             ResultSet rs = conn.createStatement().executeQuery("SELECT * FROM abonnement")) {
            while (rs.next()) {
                list.add(new Abonnement(
                        rs.getInt("id"),
                        rs.getString("libelle"),
                        rs.getDouble("prix"),
                        rs.getInt("duree_mois")
                ));
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return list;
    }

    public void save(String libelle, double prix, int duree) {
        try (Connection conn = DBConnector.getConnection();
             PreparedStatement ps = conn.prepareStatement("INSERT INTO abonnement (libelle, prix, duree_mois) VALUES (?, ?, ?)")) {
            ps.setString(1, libelle);
            ps.setDouble(2, prix);
            ps.setInt(3, duree);
            ps.executeUpdate();
        } catch (SQLException e) { e.printStackTrace(); }
    }

    // --- NOUVELLE MÉTHODE POUR MODIFIER ---
    public void update(Abonnement abo) {
        String sql = "UPDATE abonnement SET libelle = ?, prix = ?, duree_mois = ? WHERE id = ?";
        try (Connection conn = DBConnector.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, abo.getLibelle());
            ps.setDouble(2, abo.getPrix());
            ps.setInt(3, abo.getDureeMois());
            ps.setInt(4, abo.getId()); // On utilise l'ID pour trouver la bonne ligne
            ps.executeUpdate();
        } catch (SQLException e) { e.printStackTrace(); }
    }

    public void delete(int id) {
        try (Connection conn = DBConnector.getConnection();
             PreparedStatement ps = conn.prepareStatement("DELETE FROM abonnement WHERE id = ?")) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) { e.printStackTrace(); }
    }
}