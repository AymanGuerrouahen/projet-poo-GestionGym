package org.example.dao;

import org.example.model.Membre;
import org.example.util.DBConnector;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MembreDAOImpl implements MembreDAO {

    // --- 1. AJOUTER (Modifié pour inclure l'abonnement) ---
    @Override
    public void save(Membre membre) {
        // On ajoute id_abonnement dans la requête
        String sql = "INSERT INTO membre (nom, prenom, email, telephone, id_abonnement) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DBConnector.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setString(1, membre.getNom());
            pstmt.setString(2, membre.getPrenom());
            pstmt.setString(3, membre.getEmail());
            pstmt.setString(4, membre.getTelephone());
            pstmt.setInt(5, membre.getIdAbonnement()); // Ajout de l'ID abonnement

            pstmt.executeUpdate();

            ResultSet rs = pstmt.getGeneratedKeys();
            if (rs.next()) {
                membre.setId(rs.getInt(1));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // --- 2. TROUVER PAR ID ---
    @Override
    public Membre findById(int id) {
        String sql = "SELECT * FROM membre WHERE id = ?";
        try (Connection conn = DBConnector.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return mapResultSetToMembre(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // --- 3. LISTER TOUT ---
    @Override
    public List<Membre> findAll() {
        String sql = "SELECT * FROM membre";
        List<Membre> liste = new ArrayList<>();

        try (Connection conn = DBConnector.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                liste.add(mapResultSetToMembre(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return liste;
    }

    // --- 4. MODIFIER ---
    @Override
    public void update(Membre membre) {
        // Mise à jour de l'abonnement aussi
        String sql = "UPDATE membre SET nom = ?, prenom = ?, email = ?, telephone = ?, id_abonnement = ? WHERE id = ?";

        try (Connection conn = DBConnector.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, membre.getNom());
            pstmt.setString(2, membre.getPrenom());
            pstmt.setString(3, membre.getEmail());
            pstmt.setString(4, membre.getTelephone());
            pstmt.setInt(5, membre.getIdAbonnement());
            pstmt.setInt(6, membre.getId());

            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // --- 5. SUPPRIMER ---
    @Override
    public void delete(int id) {
        String sql = "DELETE FROM membre WHERE id = ?";
        try (Connection conn = DBConnector.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // --- 6. RECHERCHE ---
    @Override
    public List<Membre> search(String keyword) {
        String sql = "SELECT * FROM membre WHERE nom LIKE ? OR prenom LIKE ?";
        List<Membre> liste = new ArrayList<>();

        try (Connection conn = DBConnector.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            String searchPattern = "%" + keyword + "%";
            pstmt.setString(1, searchPattern);
            pstmt.setString(2, searchPattern);

            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                liste.add(mapResultSetToMembre(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return liste;
    }

    // --- Méthode utilitaire pour éviter de répéter le code de mapping ---
    private Membre mapResultSetToMembre(ResultSet rs) throws SQLException {
        return new Membre(
                rs.getInt("id"),
                rs.getString("nom"),
                rs.getString("prenom"),
                rs.getString("email"),
                rs.getString("telephone"),
                rs.getInt("id_abonnement"),   // Récupération de l'abonnement
                rs.getDate("date_inscription") // Récupération de la date
        );
    }
}