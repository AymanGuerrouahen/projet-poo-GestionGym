package org.example.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import org.example.dao.AbonnementDAOImpl;
import org.example.dao.MembreDAOImpl;
import org.example.model.Abonnement;
import org.example.model.Membre;
import org.example.model.Utilisateur; // Import du modèle Utilisateur
import org.example.util.UserSession; // Import de la Session

import java.io.IOException;

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

    // --- NOUVEAU : Références aux boutons pour la sécurité ---
    // ATTENTION : Il faut mettre ces fx:id sur tes boutons dans SceneBuilder !
    @FXML private Button btnEditer;
    @FXML private Button btnSupprimer;
    // @FXML private Button btnEnregistrer; // Décommente si tu veux aussi bloquer l'ajout

    // --- Données et Outils ---
    private final ObservableList<Membre> masterData = FXCollections.observableArrayList();
    private final MembreDAOImpl membreDAO = new MembreDAOImpl();
    private final AbonnementDAOImpl abonnementDAO = new AbonnementDAOImpl();

    private Membre membreEnEdition = null;

    @FXML
    public void initialize() {
        // --- GESTION DE LA SÉCURITÉ (ADMIN vs AUTRES) ---
        Utilisateur currentUser = UserSession.getInstance().getUser();

        if (currentUser != null) {
            String role = currentUser.getRole();

            // Si l'utilisateur N'EST PAS Admin, on désactive les actions dangereuses
            if (!"ADMIN".equalsIgnoreCase(role)) {
                if (btnSupprimer != null) btnSupprimer.setDisable(true); // Grisé
                if (btnEditer != null) btnEditer.setDisable(true);       // Grisé

                // Optionnel : Si tu veux aussi empêcher les Coachs de créer des membres :
                // if (btnEnregistrer != null) btnEnregistrer.setVisible(false);

                System.out.println("Mode Restreint activé pour : " + currentUser.getUsername());
            }
        }

        // 1. Configuration des colonnes
        colNom.setCellValueFactory(new PropertyValueFactory<>("nom"));
        colPrenom.setCellValueFactory(new PropertyValueFactory<>("prenom"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        colTel.setCellValueFactory(new PropertyValueFactory<>("telephone"));
        colAbo.setCellValueFactory(new PropertyValueFactory<>("idAbonnement"));

        // 2. Chargement des Abonnements
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

                if (membre.getNom().toLowerCase().contains(lowerCaseFilter)) return true;
                else if (membre.getPrenom().toLowerCase().contains(lowerCaseFilter)) return true;
                else if (membre.getEmail().toLowerCase().contains(lowerCaseFilter)) return true;
                else return membre.getTelephone().contains(lowerCaseFilter);
            });
        });

        SortedList<Membre> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(membreTable.comparatorProperty());
        membreTable.setItems(sortedData);
    }

    // --- ACTIONS ---
    @FXML
    private void handleNouveau() {
        membreEnEdition = null;
        clearForm();
        txtNom.requestFocus();
    }

    @FXML
    private void handleEditer() {
        // Sécurité supplémentaire (au cas où on force le clic)
        Utilisateur user = UserSession.getInstance().getUser();
        if (user != null && !"ADMIN".equalsIgnoreCase(user.getRole())) {
            showAlert("Accès Refusé", "Seuls les administrateurs peuvent modifier un membre.");
            return;
        }

        Membre selected = membreTable.getSelectionModel().getSelectedItem();
        if (selected != null) {
            membreEnEdition = selected;
            fillForm(selected);
        } else {
            showAlert("Attention", "Veuillez sélectionner un membre à modifier.");
        }
    }

    @FXML
    private void handleSupprimer() {
        // Sécurité supplémentaire
        Utilisateur user = UserSession.getInstance().getUser();
        if (user != null && !"ADMIN".equalsIgnoreCase(user.getRole())) {
            showAlert("Accès Refusé", "Seuls les administrateurs peuvent supprimer un membre.");
            return;
        }

        Membre selected = membreTable.getSelectionModel().getSelectedItem();
        if (selected != null) {
            membreDAO.delete(selected.getId());
            refreshTable();
            clearForm();
        } else {
            showAlert("Attention", "Veuillez sélectionner un membre à supprimer.");
        }
    }

    @FXML
    private void handleEnregistrer() {
        if (isFormValid()) {
            String nom = txtNom.getText();
            String prenom = txtPrenom.getText();
            String email = txtEmail.getText();
            String tel = txtTel.getText();
            Abonnement abo = comboAbo.getValue();

            if (membreEnEdition == null) {
                // CRÉATION
                Membre nouveau = new Membre(nom, prenom, email, tel, abo.getId());
                membreDAO.save(nouveau);
            } else {
                // MODIFICATION (Seulement si Admin normalement, mais géré par le bouton grisé)
                membreEnEdition.setNom(nom);
                membreEnEdition.setPrenom(prenom);
                membreEnEdition.setEmail(email);
                membreEnEdition.setTelephone(tel);
                membreEnEdition.setIdAbonnement(abo.getId());

                membreDAO.update(membreEnEdition);
                membreEnEdition = null;
            }

            showAlert("Succès", "Le membre a été enregistré correctement !");
            refreshTable();
            clearForm();
        }
    }

    @FXML
    private void handleRetour(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/menu.fxml"));
        Scene scene = ((Node) event.getSource()).getScene();
        scene.setRoot(root);
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
        searchField.clear();
    }

    private void fillForm(Membre m) {
        txtNom.setText(m.getNom());
        txtPrenom.setText(m.getPrenom());
        txtEmail.setText(m.getEmail());
        txtTel.setText(m.getTelephone());

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