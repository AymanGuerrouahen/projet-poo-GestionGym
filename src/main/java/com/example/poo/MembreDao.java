package com.example.poo;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MembreDao {
    // INFOS DE CONNEXION (Vérifie ton mot de passe !)
    private String url = "jdbc:mysql://localhost:3306/projet_poo";
    private String user = "root";
    private String password = ""; // <--- Mets ton mot de passe ici si besoin

    // 1. AJOUTER un membre dans la base
    public void ajouter(String nom, String prenom, String email, String tel, int idAbonnement) {
        String sql = "INSERT INTO membre (nom, prenom, email, telephone, id_abonnement) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DriverManager.getConnection(url, user, password);
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, nom);
            stmt.setString(2, prenom);
            stmt.setString(3, email);
            stmt.setString(4, tel);
            stmt.setInt(5, idAbonnement);

            stmt.executeUpdate(); // Exécute l'ajout
            System.out.println("Membre ajouté avec succès !");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // 2. RÉCUPÉRER la liste de tous les membres
    public List<Membre> lister() {
        List<Membre> liste = new ArrayList<>();
        String sql = "SELECT * FROM membre";

        try (Connection conn = DriverManager.getConnection(url, user, password);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                // On fabrique un objet Membre pour chaque ligne de la BDD
                Membre m = new Membre(
                        rs.getInt("id"),
                        rs.getString("nom"),
                        rs.getString("prenom"),
                        rs.getString("email"),
                        rs.getString("telephone"),
                        rs.getInt("id_abonnement")
                );
                liste.add(m);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return liste;
    }
}
