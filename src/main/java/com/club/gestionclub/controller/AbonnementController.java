package com.club.gestionclub.controller;

import com.club.gestionclub.dao.AbonnementDAO;
import com.club.gestionclub.model.Abonnement;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label; // Import pour le texte
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.List;

public class AbonnementController {

    @FXML
    private TableView<Abonnement> tableAbonnements;
    @FXML
    private TableColumn<Abonnement, String> colType;
    @FXML
    private TableColumn<Abonnement, Double> colPrix;
    @FXML
    private TableColumn<Abonnement, Integer> colDuree;

    // Le nouveau Label pour le total
    @FXML
    private Label lblTotal;

    private AbonnementDAO abonnementDAO;

    public void initialize() {
        abonnementDAO = new AbonnementDAO();

        // Configuration des colonnes
        colType.setCellValueFactory(new PropertyValueFactory<>("type"));
        colPrix.setCellValueFactory(new PropertyValueFactory<>("prix"));
        colDuree.setCellValueFactory(new PropertyValueFactory<>("duree"));

        // Chargement des données
        chargerDonnees();
    }

    private void chargerDonnees() {
        // 1. Remplir le tableau
        List<Abonnement> listeSQL = abonnementDAO.getAll();
        ObservableList<Abonnement> listeJavaFX = FXCollections.observableArrayList(listeSQL);
        tableAbonnements.setItems(listeJavaFX);

        // 2. Calculer et afficher le Chiffre d'Affaires (Ta mission !)
        double total = abonnementDAO.getChiffreAffairesTotal();

        // On met à jour le texte en bas (ex: "CA Total : 329.99 €")
        lblTotal.setText("💰 CA Total : " + total + " €");
    }
}