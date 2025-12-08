package com.example.poo;


import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import java.util.ArrayList;
import java.util.List;

public class MembreController {

    // --- LIENS AVEC LE FICHIER FXML ---
    @FXML private TextField txtNom;
    @FXML private TextField txtPrenom;
    @FXML private TextField txtEmail;
    @FXML private TextField txtTel;
    @FXML private ComboBox<Integer> comboAbo;   // Liste déroulante Abonnement
    @FXML private ListView<Membre> listeMembres; // La liste à l'écran
    @FXML private TextField txtRecherche;       // Barre de recherche

    // Lien avec la base de données
    private MembreDao dao = new MembreDao();

    // --- 1. AU DÉMARRAGE ---
    @FXML
    public void initialize() {
        // On remplit la liste des abonnements avec 1, 2, 3
        comboAbo.setItems(FXCollections.observableArrayList(1, 2, 3));
        comboAbo.getSelectionModel().selectFirst();

        // On charge la liste des membres tout de suite
        onRafraichirClick();
    }

    // --- 2. BOUTON AJOUTER ---
    @FXML
    public void onAjouterClick() {
        String nom = txtNom.getText();
        String prenom = txtPrenom.getText();
        String email = txtEmail.getText();
        String tel = txtTel.getText();
        Integer idAbo = comboAbo.getValue();

        if (!nom.isEmpty() && !prenom.isEmpty()) {
            dao.ajouter(nom, prenom, email, tel, idAbo);

            // On vide les champs pour que ce soit propre
            txtNom.clear();
            txtPrenom.clear();
            txtEmail.clear();
            txtTel.clear();

            // On met à jour la liste
            onRafraichirClick();
        } else {
            System.out.println("Erreur : Nom et Prénom obligatoires");
        }
    }

    // --- 3. BOUTON SUPPRIMER (Rouge) ---
    @FXML
    public void onSupprimerClick() {
        Membre selection = listeMembres.getSelectionModel().getSelectedItem();

        if (selection != null) {
            dao.supprimer(selection.getId());
            onRafraichirClick();
        } else {
            System.out.println("Veuillez sélectionner quelqu'un !");
        }
    }

    // --- 4. BARRE DE RECHERCHE ---
    @FXML
    public void onRechercheTape() {
        String recherche = txtRecherche.getText().toLowerCase();

        // On récupère tout le monde
        List<Membre> toutLeMonde = dao.lister();
        List<Membre> resultats = new ArrayList<>();

        // On filtre
        for (Membre m : toutLeMonde) {
            if (m.getNom().toLowerCase().contains(recherche) ||
                    m.getPrenom().toLowerCase().contains(recherche)) {
                resultats.add(m);
            }
        }

        // On affiche seulement les résultats
        listeMembres.getItems().clear();
        listeMembres.getItems().addAll(resultats);
    }

    // --- 5. FONCTION UTILE POUR RECHARGER LA LISTE ---
    @FXML
    public void onRafraichirClick() {
        listeMembres.getItems().clear();
        listeMembres.getItems().addAll(dao.lister());
    }
}