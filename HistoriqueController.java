package org.example.controller;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import org.example.dao.HistoriqueDAO;
import org.example.model.Historique;

import java.io.IOException;

public class HistoriqueController {

    @FXML private TableView<Historique> table;
    @FXML private TableColumn<Historique, String> colUser;
    @FXML private TableColumn<Historique, String> colRole;
    @FXML private TableColumn<Historique, String> colDate;

    private final HistoriqueDAO dao = new HistoriqueDAO();

    @FXML
    public void initialize() {
        colUser.setCellValueFactory(new PropertyValueFactory<>("username"));
        colRole.setCellValueFactory(new PropertyValueFactory<>("role"));
        colDate.setCellValueFactory(new PropertyValueFactory<>("dateHeure"));

        table.setItems(FXCollections.observableArrayList(dao.findAll()));
    }

    @FXML
    private void handleRetour(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/menu.fxml"));
        Scene scene = ((Node) event.getSource()).getScene();
        scene.setRoot(root);
    }
}