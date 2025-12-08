package com.club.gestionclub.model;

public class Abonnement {
    private int id;
    private String type;
    private double prix;
    private int duree;

    // Constructeur vide
    public Abonnement() {}

    // Constructeur complet
    public Abonnement(int id, String type, double prix, int duree) {
        this.id = id;
        this.type = type;
        this.prix = prix;
        this.duree = duree;
    }

    // Constructeur sans ID
    public Abonnement(String type, double prix, int duree) {
        this.type = type;
        this.prix = prix;
        this.duree = duree;
    }

    // Getters et Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public double getPrix() { return prix; }
    public void setPrix(double prix) { this.prix = prix; }

    public int getDuree() { return duree; }
    public void setDuree(int duree) { this.duree = duree; }

    @Override
    public String toString() {
        return type;
    }
}