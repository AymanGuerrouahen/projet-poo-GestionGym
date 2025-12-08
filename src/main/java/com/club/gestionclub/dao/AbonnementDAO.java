package com.club.gestionclub.dao;

import com.club.gestionclub.model.Abonnement;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class AbonnementDAO {

    private Connection connection;

    public AbonnementDAO() {
        this.connection = DatabaseConnection.getConnection();
    }

    // 1. Récupérer tous les abonnements (Pour ton tableau)
    public List<Abonnement> getAll() {
        List<Abonnement> liste = new ArrayList<>();
        String sql = "SELECT * FROM abonnement";

        try {
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            while (rs.next()) {
                Abonnement a = new Abonnement(
                        rs.getInt("id"),
                        rs.getString("type"),
                        rs.getDouble("prix"),
                        rs.getInt("duree")
                );
                liste.add(a);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return liste;
    }

    // 2. Calculer le Chiffre d'Affaires (Ta mission spéciale !)
    // On additionne tous les prix des abonnements vendus (simulation ici)
    public double getChiffreAffairesTotal() {
        String sql = "SELECT SUM(prix) as total FROM abonnement";
        try {
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            if (rs.next()) {
                return rs.getDouble("total");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0.0;
    }
}