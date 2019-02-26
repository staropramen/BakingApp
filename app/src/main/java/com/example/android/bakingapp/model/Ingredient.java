package com.example.android.bakingapp.model;

public class Ingredient {

    private int quantity;
    private String measure;
    private String ingrediant;

    public Ingredient(int quantity, String measure, String ingrediant) {
        this.quantity = quantity;
        this.measure = measure;
        this.ingrediant = ingrediant;
    }

    public int getQuantity() {
        return quantity;
    }

    public String getMeasure() {
        return measure;
    }

    public String getIngrediant() {
        return ingrediant;
    }
}
