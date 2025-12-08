package org.example.dao;
import org.example.model.Abonnement;
import java.util.List;

public interface AbonnementDAO {
    List<Abonnement> findAll();
    Abonnement findById(int id);
}