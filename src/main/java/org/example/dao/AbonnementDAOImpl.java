package org.example.dao;

import org.example.model.Abonnement;
import org.example.util.DBConnector;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AbonnementDAOImpl implements AbonnementDAO {

    @Override
    public List<Abonnement> findAll() {
        List<Abonnement> liste = new ArrayList<>();
        String sql = "SELECT * FROM abonnement";

        try (Connection conn = DBConnector.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                liste.add(new Abonnement(
                        rs.getInt("id"),
                        rs.getString("libelle"),
                        rs.getDouble("prix"),
                        rs.getInt("duree_mois")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return liste;
    }

    @Override
    public Abonnement findById(int id) {
        String sql = "SELECT * FROM abonnement WHERE id = ?";
        try (Connection conn = DBConnector.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return new Abonnement(
                        rs.getInt("id"),
                        rs.getString("libelle"),
                        rs.getDouble("prix"),
                        rs.getInt("duree_mois")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
