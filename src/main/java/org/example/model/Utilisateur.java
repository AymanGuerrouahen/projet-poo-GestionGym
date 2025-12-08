package org.example.model;

public class Utilisateur {
    private int id;
    private String username;
    private String password;
    private String role; // "ADMIN" ou "USER"

    public Utilisateur() {}

    public Utilisateur(int id, String username, String password, String role) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.role = role;
    }

    // Getters
    public String getUsername() { return username; }
    public String getRole() { return role; }
    public String getPassword() { return password; }

    // Setters... (tu peux les générer avec Alt+Insert si besoin)
}