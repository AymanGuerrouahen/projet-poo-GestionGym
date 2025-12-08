package org.example.model;

public class Abonnement {
    private int id;
    private String libelle; // Ex: "Premium"
    private double prix;
    private int dureeMois;

    public Abonnement() {}

    public Abonnement(int id, String libelle, double prix, int dureeMois) {
        this.id = id;
        this.libelle = libelle;
        this.prix = prix;
        this.dureeMois = dureeMois;
    }

    // Getters / Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getLibelle() { return libelle; }
    public void setLibelle(String libelle) { this.libelle = libelle; }

    public double getPrix() { return prix; }
    public void setPrix(double prix) { this.prix = prix; }

    public int getDureeMois() { return dureeMois; }
    public void setDureeMois(int dureeMois) { this.dureeMois = dureeMois; }

    @Override
    public String toString() {
        return libelle + " (" + prix + "€)";
    }
}
