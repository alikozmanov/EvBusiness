package fr.fms.entities;

public class Formation {
	// Les attributs
    private int idFormation;
    private String name;
    private String description;
    private int durationDays;
    private boolean faceToFace;
    private double price;
    private String categoryName;

    // Constructeur
    public Formation(int idFormation, String name, String description, int durationDays, boolean faceToFace, double price, String categoryName) {
        this.idFormation = idFormation;
        this.name = name;
        this.description = description;
        this.durationDays = durationDays;
        this.faceToFace = faceToFace;
        this.price = price;
        this.categoryName = categoryName;
    }

    // Getters
    public int getIdFormation() {
        return idFormation;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public int getDurationDays() {
        return durationDays;
    }

    public boolean isFaceToFace() {
        return faceToFace;
    }

    public double getPrice() {
        return price;
    }

    public String getCategoryName() {
        return categoryName;
    }
}
