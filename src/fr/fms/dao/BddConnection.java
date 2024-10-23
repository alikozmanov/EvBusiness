package fr.fms.dao;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Properties;

public class BddConnection {

    // Méthode pour charger les propriétés depuis un fichier
    public Properties loadConfig(String filePath) throws IOException {
        Properties prop = new Properties();
        try (FileInputStream fis = new FileInputStream(filePath)) {
            prop.load(fis);
        }
        return prop;
    }

    // Méthode pour créer un fichier de configuration si nécessaire
    public void createConfigFile() throws IOException {
        Properties prop = new Properties();
        try (OutputStream output = new FileOutputStream("lib/config.properties")) {
            // Ajoute les propriétés de configuration
            prop.setProperty("db.driver.class", "org.mariadb.jdbc.Driver");
            prop.setProperty("db.url", "jdbc:mariadb://localhost:3306/Business");
            prop.setProperty("db.login", "root");
            prop.setProperty("db.password", "fms2024");

            // Sauvegarde dans le fichier de configuration
            prop.store(output, null);
            System.out.println("Fichier de configuration créé avec succès.");
        }
    }
}
