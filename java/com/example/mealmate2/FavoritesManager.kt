package com.example.mealmate2

import android.util.Log

object FavoritesManager {
    private val favoriteMeals = mutableSetOf<Meal>()

    fun addToFavorites(meal: Meal?) {
        if (meal == null || meal.idMeal.isEmpty()) {
            Log.e("FavoritesManager", "Meal is null or has empty ID! Cannot add to favorites.")
            return
        }
        favoriteMeals.add(meal)
        Log.d("FavoritesManager", "Added to favorites: ${meal.strMeal}")
    }

    fun removeFromFavorites(meal: Meal) {
        favoriteMeals.remove(meal)
        Log.d("FavoritesManager", "Removed from favorites: ${meal.strMeal}")
    }

    fun toggleFavorite(meal: Meal) {
        if (isFavorite(meal)) {
            removeFromFavorites(meal)
        } else {
            addToFavorites(meal)
        }
    }

    fun isFavorite(meal: Meal): Boolean {
        return favoriteMeals.contains(meal)
    }

    fun getFavoriteMeals(): List<Meal> {
        return favoriteMeals.toList()
    }
}
