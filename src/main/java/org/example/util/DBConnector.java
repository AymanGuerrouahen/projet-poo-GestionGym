package org.example.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnector {

    // ⚠️ Remplace 'bibliotheque_db' par le VRAI nom de ta base de données
    // AVANT (probablement) :
// private static final String URL = "jdbc:mysql://localhost:3306/gestion_bibliotheque";

    // APRÈS (Correction à faire) :
    private static final String URL = "jdbc:mysql://localhost:3306/projet_poo";

    // ⚠️ Ton utilisateur (souvent 'root')
    private static final String USER = "root";

    // ⚠️ Ton mot de passe (souvent vide "" sur WAMP/XAMPP, ou "root" sur MAMP)
    private static final String PASSWORD = "";

    /**
     * Méthode statique pour obtenir la connexion.
     * Elle sera appelée par tous tes DAO (Membre, Abonnement, Utilisateur).
     */
    public static Connection getConnection() throws SQLException {
        try {
            // Optionnel : Charge explicitement le driver (utile pour certaines versions de Java)
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}
