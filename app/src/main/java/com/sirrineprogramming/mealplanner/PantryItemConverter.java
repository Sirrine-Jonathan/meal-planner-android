package com.sirrineprogramming.mealplanner;

import com.google.firebase.database.DataSnapshot;

public class PantryItemConverter {

    static public PantryItem getNewItem(DataSnapshot child){
        Object amountInPantry = child.child("amountInPantry").getValue();
        Object calories = child.child("calories").getValue();
        Object carbs = child.child("carbs").getValue();
        Object fat = child.child("fat").getValue();
        Object name = child.child("name").getValue();
        Object price = child.child("price").getValue();
        Object protein = child.child("protein").getValue();
        Object servingSize = child.child("servingSize").getValue();
        Object sugar = child.child("sugar").getValue();
        PantryItem nextItem = new PantryItem(name.toString(),
                amountInPantry.toString(),
                calories.toString(),
                carbs.toString(),
                fat.toString(),
                protein.toString(),
                sugar.toString(),
                price.toString());
        return nextItem;
    }
}
