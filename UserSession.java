package org.example.util;

import org.example.model.Utilisateur;

public class UserSession {

    private static UserSession instance;
    private Utilisateur user;

    private UserSession() {}

    public static UserSession getInstance() {
        if (instance == null) {
            instance = new UserSession();
        }
        return instance;
    }

    public void setUser(Utilisateur user) {
        this.user = user;
    }

    public Utilisateur getUser() {
        return user;
    }

    public void cleanUserSession() {
        user = null;
    }
}