package com.sirrineprogramming.mealplanner;

import java.math.BigDecimal;

public class PantryItem {

    private String name;
    private String amountInPantry;
    private String calories;
    private String carbs;
    private String fat;
    private String protein;
    private String sugar;
    private String price;

    @SuppressWarnings("unused")
    private PantryItem(){

    }

    PantryItem(String name, String amountInPantry, String calories, String carbs, String fat, String protein, String sugar, String price) {
        this.name = name;
        this.amountInPantry = amountInPantry;
        this.calories = calories;
        this.carbs = carbs;
        this.fat = fat;
        this.protein = protein;
        this.sugar = sugar;
        setPrice(price);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAmountInPantry() {
        return amountInPantry;
    }

    public void setAmountInPantry(String amountInPantry) {
        this.amountInPantry = amountInPantry;
    }

    public String getCalories() {
        return calories;
    }

    public void setCalories(String calories) {
        this.calories = calories;
    }

    public String getCarbs() {
        return carbs;
    }

    public void setCarbs(String carbs) {
        this.carbs = carbs;
    }

    public String getFat() {
        return fat;
    }

    public void setFat(String fat) {
        this.fat = fat;
    }

    public String getProtein() {
        return protein;
    }

    public void setProtein(String protein) {
        this.protein = protein;
    }

    public String getSugar() {
        return sugar;
    }

    public void setSugar(String sugar) {
        this.sugar = sugar;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) { this.price = price; }

    @Override
    public String toString() {
        return "PantryItem{" +
                "name='" + name + '\'' +
                ", amountInPantry=" + amountInPantry +
                ", calories=" + calories +
                ", carbs=" + carbs +
                ", fat=" + fat +
                ", protein=" + protein +
                ", sugar=" + sugar +
                ", price=" + price +
                '}';
    }
}
