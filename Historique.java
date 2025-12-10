package org.example.model;

public class Historique {
    private String username;
    private String role;
    private String dateHeure;

    public Historique(String username, String role, String dateHeure) {
        this.username = username;
        this.role = role;
        this.dateHeure = dateHeure;
    }

    public String getUsername() { return username; }
    public String getRole() { return role; }
    public String getDateHeure() { return dateHeure; }
}