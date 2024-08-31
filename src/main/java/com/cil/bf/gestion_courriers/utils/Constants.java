package com.cil.bf.gestion_courriers.utils;

public final class Constants {

    // Regex for acceptable logins
    public static final String LOGIN_REGEX = "^(?>[a-zA-Z0-9!$&*+=?^_`{|}~.-]+@[a-zA-Z0-9-]+(?:\\.[a-zA-Z0-9-]+)*)|(?>[_.@A-Za-z0-9-]+)$";
    public static String SYSTEM_ACCOUNT = "system";

    // privileges du systeme
    public static final String ADMIN = "ROLE_ADMIN";
    public static final String USER = "ROLE_USER";

    private Constants() {
    }

    public static final String QUELQUE_CHOSE_S_EST_PASSE = "Quelque chose s'est mal passé.";
    public static final String DONNEES_INVALIDES = "Données invalides.";
    public static final String DONNEES_NOT_FOUND = "Données n'existent pas.";
    public static final String ACCES_NON_AUTORISE = "Accès non autorisé.";
    public static final String STORE_LOCATION = "/home/issaka/SpringBootProject";
}
