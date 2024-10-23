package fr.fms;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import fr.fms.entities.Formation;

public class CreateConfigFile {

    public static void main(String[] args) {
        try {
            // Crée le fichier de configuration s'il n'existe pas déjà
            createConfigFile();

            // Charge les propriétés depuis le fichier de configuration
            Properties prop = config("lib/config.properties");

            // Connexion à la base de données
            Class.forName(prop.getProperty("db.driver.class"));
            String url = prop.getProperty("db.url");
            String login = prop.getProperty("db.login");
            String password = prop.getProperty("db.password");

            try (Connection connection = DriverManager.getConnection(url, login, password)) {
                
                // Affiche toutes les formations
                System.out.println("\n\033[1m=== TOUTES LES FORMATIONS DISPONIBLES ===\033[0m");
                List<Formation> allformations = getAllFormations(connection); // Récupère toutes les formations
                displayFormations(allformations); // Affiche les formation récupérée

                // Affiche les formations filtrées par catégorie
                System.out.println("\n\033[1m=== FORMATIONS FILTRÉES PAR CATÉGORIE ===\033[0m");
                List<Formation> webFormations = getFormationsByCategory(connection, "Développement Web");
                displayFormations(webFormations);

                // Affiche les formations contenant un mot-clé
                System.out.println("\n\033[1m=== FORMATIONS CONTENANT LE MOT-CLÉ ===\033[0m");
                List<Formation> keyword = getFormationsByKeyword(connection, "Spring");
                displayFormations(keyword);

                // Affiche les formations en présentiel
                System.out.println("\n\033[1m=== FORMATIONS EN PRÉSENTIEL ===\033[0m");
                List<Formation> faceToFace = getFormationsByMode(connection, true);
                displayFormations(faceToFace);

                // Affiche les formations en distanciel
                System.out.println("\n\033[1m=== FORMATIONS EN DISTANCIEL ===\033[0m");
                List<Formation> remote = getFormationsByMode(connection, false);
                displayFormations(remote);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Récupère toutes les formations disponibles
    private static List<Formation> getAllFormations(Connection connection) throws SQLException {
        String strSql = "SELECT F.IdFormation, F.Name, F.Description, F.DurationDays, F.FaceToFace, F.Price, C.CategoryName "
                      + "FROM T_Formations F JOIN T_Categories C ON F.IdCategory = C.IdCategory";
        try (PreparedStatement statement = connection.prepareStatement(strSql);
             ResultSet resultSet = statement.executeQuery()) {
            return extractFormations(resultSet);
        }
    }

    // Récupère les formations par catégorie
    private static List<Formation> getFormationsByCategory(Connection connection, String category) throws SQLException {
        String strSql = "SELECT F.IdFormation, F.Name, F.Description, F.DurationDays, F.FaceToFace, F.Price, C.CategoryName "
                      + "FROM T_Formations F JOIN T_Categories C ON F.IdCategory = C.IdCategory WHERE C.CategoryName = ?";
        try (PreparedStatement statement = connection.prepareStatement(strSql)) {
            statement.setString(1, category);
            try (ResultSet resultSet = statement.executeQuery()) {
                return extractFormations(resultSet);
            }
        }
    }

    // Récupère les formations contenant un mot-clé
    private static List<Formation> getFormationsByKeyword(Connection connection, String keyword) throws SQLException {
        String strSql = "SELECT F.IdFormation, F.Name, F.Description, F.DurationDays, F.FaceToFace, F.Price, C.CategoryName "
                      + "FROM T_Formations F JOIN T_Categories C ON F.IdCategory = C.IdCategory WHERE F.Description LIKE ?";
        try (PreparedStatement statement = connection.prepareStatement(strSql)) {
            statement.setString(1, "%" + keyword + "%");
            try (ResultSet resultSet = statement.executeQuery()) {
                return extractFormations(resultSet);
            }
        }
    }

    // Récupère les formations selon le mode (présentiel ou distanciel)
    private static List<Formation> getFormationsByMode(Connection connection, boolean faceToFace) throws SQLException {
        String strSql = "SELECT F.IdFormation, F.Name, F.Description, F.DurationDays, F.FaceToFace, F.Price, C.CategoryName "
                      + "FROM T_Formations F JOIN T_Categories C ON F.IdCategory = C.IdCategory WHERE F.FaceToFace = ?";
        try (PreparedStatement statement = connection.prepareStatement(strSql)) {
            statement.setBoolean(1, faceToFace);
            try (ResultSet resultSet = statement.executeQuery()) {
                return extractFormations(resultSet);
            }
        }
    }

    // Méthode pour extraire les formations depuis un ResultSet
    private static List<Formation> extractFormations(ResultSet resultSet) throws SQLException {
        List<Formation> formations = new ArrayList<>();
        while (resultSet.next()) {
            formations.add(new Formation(
                resultSet.getInt("IdFormation"),
                resultSet.getString("Name"),
                resultSet.getString("Description"),
                resultSet.getInt("DurationDays"),
                resultSet.getBoolean("FaceToFace"),
                resultSet.getDouble("Price"),
                resultSet.getString("CategoryName")
            ));
        }
        return formations;
    }

    // Méthode pour afficher la liste des formations
    private static void displayFormations(List<Formation> formations) {
        formations.forEach(f -> System.out.println(
            f.getIdFormation() + " - " + f.getName() + " - " + f.getDescription() + " - " +
            f.getDurationDays() + " jours - " + (f.isFaceToFace() ? "Présentiel" : "Distanciel") + " - " +
            f.getPrice() + " € - Catégorie: " + f.getCategoryName()
        ));
    }

    // Méthode pour charger les propriétés depuis un fichier
    private static Properties config(String filePath) throws IOException {
        Properties prop = new Properties();
        try (FileInputStream fis = new FileInputStream(filePath)) {
            prop.load(fis);
        }
        return prop;
    }

    // Méthode pour créer un fichier de configuration
    private static void createConfigFile() throws IOException {
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
