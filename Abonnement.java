package org.example.model;

public class Abonnement {
    private int id;
    private String libelle;
    private double prix;
    private int dureeMois;

    public Abonnement(int id, String libelle, double prix, int dureeMois) {
        this.id = id;
        this.libelle = libelle;
        this.prix = prix;
        this.dureeMois = dureeMois;
    }

    // --- GETTERS (Pour lire) ---
    public int getId() { return id; }
    public String getLibelle() { return libelle; }
    public double getPrix() { return prix; }
    public int getDureeMois() { return dureeMois; }

    // --- SETTERS (Pour modifier - C'est ça qui manquait !) ---
    public void setId(int id) { this.id = id; }
    public void setLibelle(String libelle) { this.libelle = libelle; }
    public void setPrix(double prix) { this.prix = prix; }
    public void setDureeMois(int dureeMois) { this.dureeMois = dureeMois; }

    @Override
    public String toString() {
        return libelle + " (" + prix + "€)";
    }
}