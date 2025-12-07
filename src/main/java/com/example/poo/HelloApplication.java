package com.example.poo;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class HelloApplication extends Application {

    // Petite classe pour représenter un objet (ex: un Produit ou un Élève)
    public static class Chose {
        String nom;
        String description;

        public Chose(String nom, String description) {
            this.nom = nom;
            this.description = description;
        }

        @Override
        public String toString() {
            return nom + " - " + description;
        }
    }

    @Override
    public void start(Stage stage) {
        // 1. La liste des données
        ObservableList<Chose> maListe = FXCollections.observableArrayList();

        // 2. Les champs pour écrire
        TextField champNom = new TextField();
        champNom.setPromptText("Nom (ex: Produit A)");

        TextField champDesc = new TextField();
        champDesc.setPromptText("Description (ex: En stock)");

        Button boutonAjouter = new Button("Ajouter");
        ListView<Chose> affichageListe = new ListView<>(maListe);

        // 3. L'action du bouton
        boutonAjouter.setOnAction(e -> {
            if (!champNom.getText().isEmpty()) {
                maListe.add(new Chose(champNom.getText(), champDesc.getText()));
                champNom.clear();
                champDesc.clear();
            }
        });

        // 4. La mise en page
        HBox formulaire = new HBox(10, champNom, champDesc, boutonAjouter);
        VBox racine = new VBox(10, new Label("PROJET POO SIMPLE"), formulaire, affichageListe);
        racine.setPadding(new Insets(20));

        // 5. Lancer la fenêtre
        Scene scene = new Scene(racine, 500, 400);
        stage.setTitle("Application POO");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}