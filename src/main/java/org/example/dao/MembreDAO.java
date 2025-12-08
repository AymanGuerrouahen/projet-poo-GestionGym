package org.example.dao;

import org.example.model.Membre;
import java.util.List;

/**
 * Interface définissant les opérations CRUD pour l'entité Membre.
 */
public interface MembreDAO {

    // Créer un nouveau membre
    void save(Membre membre);

    // Trouver un membre par son ID
    Membre findById(int id);

    // Trouver tous les membres (pour l'affichage liste)
    List<Membre> findAll();

    // Mettre à jour un membre existant
    void update(Membre membre);

    // Supprimer un membre par son ID
    void delete(int id);

    // Rechercher dynamique (pour ta partie UI spécifique)
    List<Membre> search(String keyword);
}