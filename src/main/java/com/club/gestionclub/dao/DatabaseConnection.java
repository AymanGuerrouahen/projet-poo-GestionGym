package com.club.gestionclub.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    // Infos de connexion XAMPP par défaut
    private static final String URL = "jdbc:mysql://localhost:3306/gestion_club";
    private static final String USER = "root";
    private static final String PASSWORD = ""; // Vide sous XAMPP

    private static Connection connection;

    private DatabaseConnection() { }

    public static Connection getConnection() {
        if (connection == null) {
            try {
                connection = DriverManager.getConnection(URL, USER, PASSWORD);
                System.out.println("✅ Connexion à la BDD réussie !");
            } catch (SQLException e) {
                System.err.println("❌ Erreur de connexion : " + e.getMessage());
                e.printStackTrace();
            }
        }
        return connection;
    }
}