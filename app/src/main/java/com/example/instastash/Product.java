package com.example.instastash;

public class Product {
    private int id;
    private String name;
    private String description;
    private double price;
    private String barcode;
    private byte[] image;

    // Default constructor
    public Product() {
    }

    // Constructor without id (for inserting new products)
    public Product(String name, String description, double price, String barcode, byte[] image) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.barcode = barcode;
        this.image = image;
    }

    // Constructor with id (for retrieving products from the database)
    public Product(int id, String name, String description, double price, String barcode, byte[] image) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.barcode = barcode;
        this.image = image;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }
}
