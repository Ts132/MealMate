package com.example.mealmate2

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "meal_table")
data class Meal(
    @PrimaryKey @SerializedName("idMeal") val idMeal: String = "",
    @SerializedName("strMeal") val strMeal: String = "Unknown Meal",
    var img: String = "",
    @SerializedName("strArea") val strArea: String = "Unknown Area",
    @SerializedName("strCategory") val strCategory: String = "Unknown Category",
    @SerializedName("strYoutube") val strYoutube: String = "",
    @SerializedName("strMealThumb") val strMealThumb: String = "",
    @SerializedName("strInstructions") val strInstructions: String = "No instructions available.",
    @SerializedName("date") val date: String = "",
    var isFavorite: Boolean = false
)
