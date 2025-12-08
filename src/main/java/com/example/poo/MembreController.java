package com.example.poo;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;

public class MembreController {

    // Les liens avec le fichier FXML (le design)
    @FXML private TextField txtNom;
    @FXML private TextField txtPrenom;
    @FXML private TextField txtEmail;
    @FXML private TextField txtTel;
    @FXML private ComboBox<Integer> comboAbo; // Pour choisir l'abonnement
    @FXML private ListView<Membre> listeMembres;

    private MembreDao dao = new MembreDao();

    // Fonction qui se lance toute seule au démarrage de la fenêtre
    @FXML
    public void initialize() {
        // On remplit la liste déroulante avec des ID d'abonnements fictifs (1, 2, 3)
        comboAbo.setItems(FXCollections.observableArrayList(1, 2, 3));
        comboAbo.getSelectionModel().selectFirst(); // Sélectionne le 1 par défaut

        onRafraichirClick(); // On charge la liste dès l'ouverture
    }

    // Quand on clique sur "Ajouter"
    @FXML
    public void onAjouterClick() {
        String nom = txtNom.getText();
        String prenom = txtPrenom.getText();
        String email = txtEmail.getText();
        String tel = txtTel.getText();
        Integer idAbo = comboAbo.getValue();

        if (!nom.isEmpty() && !prenom.isEmpty()) {
            dao.ajouter(nom, prenom, email, tel, idAbo);

            // On vide les champs
            txtNom.clear();
            txtPrenom.clear();
            txtEmail.clear();
            txtTel.clear();

            onRafraichirClick(); // On met à jour la liste affichée
        } else {
            System.out.println("Erreur : Nom et Prénom obligatoires");
        }
    }

    // Quand on clique sur "Rafraîchir"
    @FXML
    public void onRafraichirClick() {
        listeMembres.getItems().clear();
        listeMembres.getItems().addAll(dao.lister());
    }
}