package org.example.model;

import java.sql.Date;

public class Membre {

    private int id;
    private String nom;
    private String prenom;
    private String email;
    private String telephone;

    // Nouveaux champs pour correspondre à la BDD
    private int idAbonnement;
    private Date dateInscription;

    public Membre() {
    }

    public Membre(int id, String nom, String prenom, String email, String telephone, int idAbonnement, Date dateInscription) {
        this.id = id;
        this.nom = nom;
        this.prenom = prenom;
        this.email = email;
        this.telephone = telephone;
        this.idAbonnement = idAbonnement;
        this.dateInscription = dateInscription;
    }

    // Constructeur simplifié pour l'ajout (sans ID ni date, gérés par la BDD)
    public Membre(String nom, String prenom, String email, String telephone, int idAbonnement) {
        this.nom = nom;
        this.prenom = prenom;
        this.email = email;
        this.telephone = telephone;
        this.idAbonnement = idAbonnement;
    }

    // --- Getters et Setters ---
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }

    public String getPrenom() { return prenom; }
    public void setPrenom(String prenom) { this.prenom = prenom; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getTelephone() { return telephone; }
    public void setTelephone(String telephone) { this.telephone = telephone; }

    public int getIdAbonnement() { return idAbonnement; }
    public void setIdAbonnement(int idAbonnement) { this.idAbonnement = idAbonnement; }

    public Date getDateInscription() { return dateInscription; }
    public void setDateInscription(Date dateInscription) { this.dateInscription = dateInscription; }

    @Override
    public String toString() {
        return nom + " " + prenom + " (Abo: " + idAbonnement + ")";
    }
}