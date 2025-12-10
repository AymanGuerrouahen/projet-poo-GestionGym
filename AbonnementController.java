package org.example.controller;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import org.example.dao.AbonnementDAOImpl;
import org.example.model.Abonnement;

import java.io.IOException;
import java.util.Optional;

public class AbonnementController {

    @FXML private TableView<Abonnement> table;
    @FXML private TableColumn<Abonnement, String> colLibelle;
    @FXML private TableColumn<Abonnement, Double> colPrix;
    @FXML private TableColumn<Abonnement, Integer> colDuree;

    @FXML private TextField txtLibelle;
    @FXML private TextField txtPrix;
    @FXML private TextField txtDuree;

    // Boutons pour gérer l'état (Ajout vs Modif)
    @FXML private Button btnAjouter;
    @FXML private Button btnModifier;

    private final AbonnementDAOImpl dao = new AbonnementDAOImpl();
    private Abonnement abonnementEnEdition = null; // Stocke l'abonnement qu'on modifie

    @FXML
    public void initialize() {
        colLibelle.setCellValueFactory(new PropertyValueFactory<>("libelle"));
        colPrix.setCellValueFactory(new PropertyValueFactory<>("prix"));
        colDuree.setCellValueFactory(new PropertyValueFactory<>("dureeMois"));

        // LISTENER : Quand on clique sur une ligne, on remplit le formulaire
        table.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                remplirFormulaire(newSelection);
            }
        });

        // Au démarrage, le bouton modifier est désactivé
        btnModifier.setDisable(true);
        refresh();
    }

    private void remplirFormulaire(Abonnement abo) {
        txtLibelle.setText(abo.getLibelle());
        txtPrix.setText(String.valueOf(abo.getPrix()));
        txtDuree.setText(String.valueOf(abo.getDureeMois()));

        abonnementEnEdition = abo;
        btnAjouter.setDisable(true);   // On bloque l'ajout
        btnModifier.setDisable(false); // On active la modif
    }

    @FXML
    private void handleAjouter() {
        if (validation()) {
            dao.save(txtLibelle.getText(), Double.parseDouble(txtPrix.getText()), Integer.parseInt(txtDuree.getText()));
            resetForm();
        }
    }

    @FXML
    private void handleValiderModif() {
        if (abonnementEnEdition != null && validation()) {
            // Mise à jour de l'objet
            abonnementEnEdition.setLibelle(txtLibelle.getText());
            abonnementEnEdition.setPrix(Double.parseDouble(txtPrix.getText()));
            abonnementEnEdition.setDureeMois(Integer.parseInt(txtDuree.getText()));

            // Mise à jour en BDD
            dao.update(abonnementEnEdition);

            showAlert("Succès", "Modification enregistrée !");
            resetForm();
        }
    }

    @FXML
    private void handleAnnuler() {
        resetForm();
        table.getSelectionModel().clearSelection();
    }

    @FXML
    private void handleSupprimer() {
        Abonnement selected = table.getSelectionModel().getSelectedItem();
        if (selected != null) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Supprimer l'offre " + selected.getLibelle() + " ?");
            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                dao.delete(selected.getId());
                resetForm();
            }
        } else {
            showAlert("Attention", "Sélectionnez une ligne à supprimer.");
        }
    }

    @FXML
    private void handleRetour(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/menu.fxml"));
        Scene scene = ((Node) event.getSource()).getScene();
        scene.setRoot(root); // Garde le plein écran
    }

    private void refresh() {
        table.setItems(FXCollections.observableArrayList(dao.findAll()));
    }

    private void resetForm() {
        txtLibelle.clear();
        txtPrix.clear();
        txtDuree.clear();
        abonnementEnEdition = null;

        // On remet les boutons en mode "Ajout"
        btnAjouter.setDisable(false);
        btnModifier.setDisable(true);
        refresh();
    }

    private boolean validation() {
        try {
            if(txtLibelle.getText().isEmpty()) {
                showAlert("Erreur", "Le nom est obligatoire");
                return false;
            }
            Double.parseDouble(txtPrix.getText());
            Integer.parseInt(txtDuree.getText());
            return true;
        } catch (Exception e) {
            showAlert("Erreur", "Prix et Durée doivent être des chiffres valides.");
            return false;
        }
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}