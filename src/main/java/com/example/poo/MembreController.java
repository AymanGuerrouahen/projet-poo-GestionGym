package com.example.poo;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import java.util.ArrayList;
import java.util.List;

public class MembreController {

    // --- 1. LES LIENS AVEC L'INTERFACE (FXML) ---
    @FXML
    private TextField txtNom;
    @FXML
    private TextField txtPrenom;
    @FXML
    private TextField txtEmail;
    @FXML
    private TextField txtTel;
    @FXML
    private TextField txtRecherche;       // Barre de recherche
    @FXML
    private ComboBox<Integer> comboAbo;   // Liste déroulante
    @FXML
    private ListView<Membre> listeMembres; // La liste des gens
    @FXML
    private Button btnSupprimer;          // Le bouton rouge (pour le cacher)

    // Le lien avec la base de données
    private MembreDao dao = new MembreDao();

    // --- 2. AU DÉMARRAGE DE LA PAGE ---
    @FXML
    public void initialize() {
        // A. Remplir la liste déroulante des abonnements
        comboAbo.setItems(FXCollections.observableArrayList(1, 2, 3));
        comboAbo.getSelectionModel().selectFirst();

        // B. Quand on clique sur quelqu'un dans la liste -> Remplir les champs (pour modifier)
        listeMembres.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                txtNom.setText(newVal.getNom());
                txtPrenom.setText(newVal.getPrenom());
                txtEmail.setText(newVal.getEmail());
                txtTel.setText(newVal.getTelephone());
                comboAbo.setValue(newVal.getIdAbonnement());
            }
        });

        // C. SÉCURITÉ : Si ce n'est pas un ADMIN, on cache le bouton Supprimer
        // (On vérifie la variable globale qu'on a mise dans HelloApplication)
        if (!HelloApplication.ROLE_CONNECTE.equals("ADMIN")) {
            btnSupprimer.setVisible(false);
        }

        // D. Charger la liste
        onRafraichirClick();
    }

    // --- 3. BOUTON VERT : AJOUTER ---
    @FXML
    public void onAjouterClick() {
        String nom = txtNom.getText();
        String prenom = txtPrenom.getText();
        String email = txtEmail.getText();
        String tel = txtTel.getText();
        Integer idAbo = comboAbo.getValue();

        if (!nom.isEmpty() && !prenom.isEmpty()) {
            dao.ajouter(nom, prenom, email, tel, idAbo);
            viderChamps();
            onRafraichirClick();
        } else {
            System.out.println("Erreur : Nom et Prénom obligatoires");
        }
    }

    // --- 4. BOUTON ORANGE : MODIFIER ---
    @FXML
    public void onModifierClick() {
        Membre selection = listeMembres.getSelectionModel().getSelectedItem();

        if (selection != null) {
            // On crée un membre avec les nouvelles infos MAIS le même ID
            Membre m = new Membre(
                    selection.getId(),
                    txtNom.getText(),
                    txtPrenom.getText(),
                    txtEmail.getText(),
                    txtTel.getText(),
                    comboAbo.getValue()
            );

            dao.modifier(m);
            viderChamps();
            onRafraichirClick();
        } else {
            System.out.println("Sélectionnez quelqu'un à modifier !");
        }
    }

    // --- 5. BOUTON ROUGE : SUPPRIMER ---
    @FXML
    public void onSupprimerClick() {
        Membre selection = listeMembres.getSelectionModel().getSelectedItem();

        if (selection != null) {
            dao.supprimer(selection.getId());
            viderChamps();
            onRafraichirClick();
        } else {
            System.out.println("Veuillez sélectionner quelqu'un !");
        }
    }

    private void onRafraichirClick() {
    }

    private void viderChamps() {

    }
}