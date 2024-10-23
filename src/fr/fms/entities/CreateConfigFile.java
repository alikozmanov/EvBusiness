package fr.fms.entities;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Properties;

public class CreateConfigFile {

    public static void main(String[] args) {
        ArrayList<Formation> formations = new ArrayList<>(); // Liste pour stocker les formations
        String strSql = "SELECT F.IdFormation, F.Name, F.Description, F.DurationDays, F.FaceToFace, F.Price, C.CategoryName "
                      + "FROM T_Formations F "
                      + "JOIN T_Categories C ON F.IdCategory = C.IdCategory";

        try {
            // Crée le fichier de configuration
            createFile();

            // Charge les propriétés depuis le fichier de config
            Properties prop = config("lib/config.properties");

            // Charge la classe du driver JDBC
            Class.forName(prop.getProperty("db.driver.class"));
            String url = prop.getProperty("db.url");
            String login = prop.getProperty("db.login");
            String password = prop.getProperty("db.password");

            // Connexion à la base de données
            try (Connection connection = DriverManager.getConnection(url, login, password);
                 Statement statement = connection.createStatement();
                 ResultSet resultSet = statement.executeQuery(strSql)) {

                // Parcours des résultats de la requête
                while (resultSet.next()) {
                    int rsIdFormation = resultSet.getInt(1); // Récupère l'ID de la formation
                    String rsName = resultSet.getString(2); // Récupère le nom
                    String rsDescription = resultSet.getString(3); // Récupère la description
                    int rsDurationDays = resultSet.getInt(4); // Récupère la durée en jours
                    boolean rsFaceToFace = resultSet.getBoolean(5); // Récupère le statut présentiel
                    double rsPrice = resultSet.getDouble(6); // Récupère le prix
                    String rsCategoryName = resultSet.getString(7); // Récupère le nom de la catégorie

                    // Ajoute une nouvelle formation à la liste
                    formations.add(new Formation(rsIdFormation, rsName, rsDescription, rsDurationDays, rsFaceToFace, rsPrice, rsCategoryName));
                }

                // Affiche les formations
                formations.forEach(f -> System.out.println(
                        f.getIdFormation() + " - " + f.getName() + " - " + f.getDescription() + " - " +
                        f.getDurationDays() + " jours - " + (f.isFaceToFace() ? "Présentiel" : "Distanciel") + " - " +
                        f.getPrice() + " € - Catégorie: " + f.getCategoryName()));
            } 
        } catch (IOException ioe) {
            ioe.printStackTrace(); // Gestion des erreurs à la lecture et écriture des fichiers
        } catch (ClassNotFoundException e) {
            e.printStackTrace(); 
        } catch (SQLException e) {
            e.printStackTrace(); // Gestion des erreurs à l'exécution des requêtes SQL
        }
    }

    // Méthode pour charger les propriétés depuis un fichier
    private static Properties config(String filePath) throws IOException {
        Properties prop = new Properties();
        // Charge les propriétés à partir du fichier 
        try (FileInputStream fis = new FileInputStream(filePath)) {
            prop.load(fis); // Charge le contenu du fichier
        }
        return prop; // Retourne prop contenant les paramètres 
    }

    // Méthode pour créer un fichier de configuration
    private static void createFile() throws IOException {
        Properties prop = new Properties();
        // Crée un fichier de configuration 
        try (OutputStream output = new FileOutputStream("lib/config.properties")) {
            // Ajoute les propriétés de configuration
            prop.setProperty("db.driver.class", "org.mariadb.jdbc.Driver");
            prop.setProperty("db.url", "jdbc:mariadb://localhost:3306/Business"); 
            prop.setProperty("db.login", "root");
            prop.setProperty("db.password", "fms2024");

            // Sauvegarde dans le fichier
            prop.store(output, null);
            System.out.println("Fichier de configuration créé avec succès :)");
        }
    }
}

