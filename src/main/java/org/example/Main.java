package org.example;

import org.example.dao.*;
import org.example.model.*;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        System.out.println("=== TEST FINAL DE TA PARTIE ===");

        // 1. Test des Abonnements (Ce que tu as déjà fait)
        System.out.println("\n--- 1. Lecture des Abonnements ---");
        AbonnementDAO aboDAO = new AbonnementDAOImpl();
        List<Abonnement> abos = aboDAO.findAll();
        for (Abonnement a : abos) {
            System.out.println("   -> " + a.toString());
        }

        // 2. Test Ajout Membre (Ce que tu as fait avant)
        System.out.println("\n--- 2. Ajout d'un membre avec Abonnement ---");
        MembreDAO membreDAO = new MembreDAOImpl();
        // On teste avec l'abonnement ID 2 (Premium)
        Membre nouveau = new Membre("Benzema", "Karim", "kb9@saudi.com", "0600000009", 2);
        membreDAO.save(nouveau);
        System.out.println("✅ Membre ajouté avec succès (ID: " + nouveau.getId() + ")");

        // 3. Test Login (Ce qu'on vient de faire)
        System.out.println("\n--- 3. Test de Connexion (Login) ---");
        UtilisateurDAO userDAO = new UtilisateurDAO();
        Utilisateur admin = userDAO.getUtilisateur("admin", "1234");

        if (admin != null) {
            System.out.println("✅ Connexion réussie ! Utilisateur : " + admin.getUsername());
        } else {
            System.out.println("❌ Échec de connexion.");
        }
    }
}