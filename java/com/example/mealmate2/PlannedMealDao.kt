package com.example.mealmate2

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface PlannedMealDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(plannedMeal: PlannedMeal)

    @Query("SELECT * FROM planned_meal_table WHERE date = :date")
    fun getMealsForDate(date: String): LiveData<List<PlannedMeal>>

    @Query("DELETE FROM planned_meal_table WHERE mealId = :mealId AND date = :date")
    fun deleteMealFromPlan(mealId: String, date: String)
}
