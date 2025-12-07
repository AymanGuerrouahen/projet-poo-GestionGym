package com.example.poo;

public class Membre {
    private int id;
    private String nom;
    private String prenom;
    private String email;
    private String telephone;
    private int idAbonnement; // <-- NOUVEAU (Lien avec la table abonnement)

    // Constructeur complet
    public Membre(int id, String nom, String prenom, String email, String telephone, int idAbonnement) {
        this.id = id;
        this.nom = nom;
        this.prenom = prenom;
        this.email = email;
        this.telephone = telephone;
        this.idAbonnement = idAbonnement;
    }

    // Getters
    public int getId() { return id; }
    public String getNom() { return nom; }
    public String getPrenom() { return prenom; }
    public String getEmail() { return email; }
    public String getTelephone() { return telephone; }
    public int getIdAbonnement() { return idAbonnement; }

    @Override
    public String toString() {
        return nom + " " + prenom + " (Abo n°" + idAbonnement + ")";
    }
}