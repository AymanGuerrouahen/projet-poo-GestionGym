package org.example.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import org.example.dao.AbonnementDAOImpl;
import org.example.dao.MembreDAOImpl;
import org.example.model.Abonnement;
import org.example.model.Membre;

import java.util.Optional;

public class MembreController {

    // --- Éléments de l'interface (Vue) ---
    @FXML private TextField searchField;
    @FXML private TableView<Membre> membreTable;
    @FXML private TableColumn<Membre, String> colNom;
    @FXML private TableColumn<Membre, String> colPrenom;
    @FXML private TableColumn<Membre, String> colEmail;
    @FXML private TableColumn<Membre, String> colTel;
    @FXML private TableColumn<Membre, Integer> colAbo;

    // Champs du formulaire
    @FXML private TextField txtNom;
    @FXML private TextField txtPrenom;
    @FXML private TextField txtEmail;
    @FXML private TextField txtTel;
    @FXML private ComboBox<Abonnement> comboAbo;

    // --- Données et Outils ---
    private final ObservableList<Membre> masterData = FXCollections.observableArrayList();
    private final MembreDAOImpl membreDAO = new MembreDAOImpl();
    private final AbonnementDAOImpl abonnementDAO = new AbonnementDAOImpl();

    // Variable pour savoir si on est en train de modifier un membre existant (et lequel)
    private Membre membreEnEdition = null;

    @FXML
    public void initialize() {
        // 1. Configuration des colonnes
        colNom.setCellValueFactory(new PropertyValueFactory<>("nom"));
        colPrenom.setCellValueFactory(new PropertyValueFactory<>("prenom"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        colTel.setCellValueFactory(new PropertyValueFactory<>("telephone"));
        colAbo.setCellValueFactory(new PropertyValueFactory<>("idAbonnement"));

        // 2. Chargement des Abonnements dans la liste déroulante
        ObservableList<Abonnement> listeAbos = FXCollections.observableArrayList(abonnementDAO.findAll());
        comboAbo.setItems(listeAbos);

        // 3. Chargement initial des membres
        refreshTable();

        // 4. Configuration de la Recherche Dynamique
        FilteredList<Membre> filteredData = new FilteredList<>(masterData, p -> true);
        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(membre -> {
                if (newValue == null || newValue.isEmpty()) return true;
                String lowerCaseFilter = newValue.toLowerCase();
                return membre.getNom().toLowerCase().contains(lowerCaseFilter)
                        || membre.getPrenom().toLowerCase().contains(lowerCaseFilter);
            });
        });
        SortedList<Membre> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(membreTable.comparatorProperty());
        membreTable.setItems(sortedData);
    }

    // --- ACTION : BOUTON "NOUVEAU" ---
    @FXML
    private void handleNouveau() {
        membreEnEdition = null; // On passe en mode création
        clearForm();
        txtNom.requestFocus();
    }

    // --- ACTION : BOUTON "MODIFIER SÉLECTION" ---
    @FXML
    private void handleEditer() {
        Membre selected = membreTable.getSelectionModel().getSelectedItem();
        if (selected != null) {
            membreEnEdition = selected; // On passe en mode édition de ce membre
            fillForm(selected);
        } else {
            showAlert("Attention", "Veuillez sélectionner un membre à modifier.");
        }
    }

    // --- ACTION : BOUTON "SUPPRIMER SÉLECTION" ---
    @FXML
    private void handleSupprimer() {
        Membre selected = membreTable.getSelectionModel().getSelectedItem();
        if (selected != null) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmation");
            alert.setHeaderText("Supprimer " + selected.getNom() + " ?");
            Optional<ButtonType> result = alert.showAndWait();

            if (result.isPresent() && result.get() == ButtonType.OK) {
                membreDAO.delete(selected.getId());
                refreshTable();
                clearForm();
            }
        } else {
            showAlert("Attention", "Veuillez sélectionner un membre à supprimer.");
        }
    }

    // --- ACTION : BOUTON "ENREGISTRER" (Le cœur du CRUD) ---
    @FXML
    private void handleEnregistrer() {
        if (isFormValid()) {
            // Récupération des données
            String nom = txtNom.getText();
            String prenom = txtPrenom.getText();
            String email = txtEmail.getText();
            String tel = txtTel.getText();
            Abonnement abo = comboAbo.getValue();

            if (membreEnEdition == null) {
                // CAS 1 : CRÉATION (Ajout)
                Membre nouveau = new Membre(nom, prenom, email, tel, abo.getId());
                membreDAO.save(nouveau);
            } else {
                // CAS 2 : MODIFICATION (Update)
                membreEnEdition.setNom(nom);
                membreEnEdition.setPrenom(prenom);
                membreEnEdition.setEmail(email);
                membreEnEdition.setTelephone(tel);
                membreEnEdition.setIdAbonnement(abo.getId());

                membreDAO.update(membreEnEdition);
                membreEnEdition = null; // Retour en mode création après modif
            }

            refreshTable();
            clearForm();
        }
    }

    // --- Méthodes Utilitaires ---

    private void refreshTable() {
        masterData.clear();
        masterData.addAll(membreDAO.findAll());
    }

    private void clearForm() {
        txtNom.clear();
        txtPrenom.clear();
        txtEmail.clear();
        txtTel.clear();
        comboAbo.getSelectionModel().clearSelection();
    }

    private void fillForm(Membre m) {
        txtNom.setText(m.getNom());
        txtPrenom.setText(m.getPrenom());
        txtEmail.setText(m.getEmail());
        txtTel.setText(m.getTelephone());

        // Sélectionner le bon abonnement dans la liste
        for (Abonnement abo : comboAbo.getItems()) {
            if (abo.getId() == m.getIdAbonnement()) {
                comboAbo.setValue(abo);
                break;
            }
        }
    }

    private boolean isFormValid() {
        if (txtNom.getText().isEmpty() || txtPrenom.getText().isEmpty() || comboAbo.getValue() == null) {
            showAlert("Erreur", "Nom, Prénom et Abonnement sont obligatoires.");
            return false;
        }
        return true;
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}