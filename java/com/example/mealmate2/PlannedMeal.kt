package com.example.mealmate2

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "planned_meal_table")
data class PlannedMeal(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val mealId: String,
    val mealName: String,
    val mealImage: String,
    val date: String
)
