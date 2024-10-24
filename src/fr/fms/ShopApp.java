package fr.fms;

import fr.fms.entities.Formation;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ShopApp {

    private static List<Formation> cart = new ArrayList<>(); 
    private static Scanner scan = new Scanner(System.in); 

    public static void main(String[] args) {
        int choice = 0;
        while(choice != 4) {
            displayMenu(); 
            choice = scanInt(); // Récupère le choix de l'utilisateur
            switch(choice) {
                case 1: addFormation(); 
                    break;
                case 2: removeFormation(); 
                    break;
                case 3:displayCart(); // Affiche le panier
                    break;
                case 4:
                    System.out.println("À bientôt dans notre boutique !");
                    break;
                default:
                    System.out.println("Veuillez entrer une valeur entre 1 et 4.");
            }
        }
    }

    // Méthode pour afficher le menu
    public static void displayMenu() {
        System.out.println("Bienvenue dans la boutique de formations !");
        System.out.println("1 : Ajouter une formation au panier");
        System.out.println("2 : Retirer une formation du panier");
        System.out.println("3 : Afficher le panier");
        System.out.println("4 : Quitter");
    }

    // Méthode pour ajouter un article
    public static void addFormation() {
        System.out.println("Saisissez l'ID de la formation à ajouter au panier :");
        int id = scanInt(); // L'utilisateur entre l'ID de la formation
        Formation formation = getFormationById(id);
        if(formation != null) {
            cart.add(formation);
            System.out.println("Formation ajoutée au panier : " + formation.getName());
        } else {
            System.out.println("Formation introuvable.");
        }
    }

    // Méthode pour retirer un article
    public static void removeFormation() {
        System.out.println("Saisissez l'ID de la formation à retirer du panier :");
        int id = scanInt(); // L'utilisateur entre l'ID de la formation
        Formation formationToRemove = null;
        for(Formation formation : cart) {
            if(formation.getIdFormation() == id) {
                formationToRemove = formation; // Identifie la formation à retirer
                break;
            }
        }
    }

    // Méthode pour afficher le contenu du panier
    public static void displayCart() {
        if(cart.isEmpty()) {
            System.out.println("Votre panier est vide.");
        } else {
            System.out.println("\nContenu du panier :");
            for(Formation formation : cart) {
                System.out.println(formation.getIdFormation() + " - " + formation.getName());
            }
        }
    }

    // La récupération d'une formation par ID 
    private static Formation getFormationById(int id) {
        List<Formation> availableFormations = new ArrayList<>(); 
        availableFormations.add(new Formation(1, "Java Avancé", "Formation sur Java", 5, true, 450.0, "Développement"));
        availableFormations.add(new Formation(2, "Spring Boot", "Framework Spring", 3, false, 300.0, "Développement"));
        availableFormations.add(new Formation(3, "SQL Avancé", "Optimisation de requêtes", 4, true, 400.0, "Base de données"));
        for(Formation formation : availableFormations) {
            if(formation.getIdFormation() == id) {
                return formation; 
            }
        }
        return null; // ucune formation ne correspond à l'ID
    }

    // Méthode pour scanner et retourner un entier
    public static int scanInt() {
        while(!scan.hasNextInt()) {
            System.out.println("Saisissez une valeur entière, svp.");
            scan.next(); // Ignore l'entrée non valide
        }
        return scan.nextInt(); // Retourne l'entier saisi
    }
}
