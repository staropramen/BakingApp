package com.example.android.bakingapp.model;

public class Ingredient {

    private double quantity;
    private String measure;
    private String ingrediant;

    public Ingredient(double quantity, String measure, String ingrediant) {
        this.quantity = quantity;
        this.measure = measure;
        this.ingrediant = ingrediant;
    }

    public double getQuantity() {
        return quantity;
    }

    public String getMeasure() {
        return measure;
    }

    public String getIngrediant() {
        return ingrediant;
    }
}
