package com.example.mealmate2

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class MealViewModel(application: Application) : AndroidViewModel(application) {

    private val mealRepository: MealRepository = MealRepository(application)

    val allMeals: LiveData<List<Meal>> = mealRepository.allMeals
    val favoriteMeals: LiveData<List<Meal>> = mealRepository.favoriteMeals

    fun getPlannedMealsForDate(date: String): LiveData<List<PlannedMeal>> {
        return mealRepository.getPlannedMealsForDate(date)
    }

    fun getMealsByIds(mealIds: List<String>): LiveData<List<Meal>> {
        return mealRepository.getMealsByIds(mealIds)
    }

    fun insert(meal: Meal) = viewModelScope.launch {
        mealRepository.insert(meal)
    }
    fun getMealById(mealId: String): LiveData<Meal> {
        return mealRepository.getMealById(mealId)
    }

    fun deleteById(mealId: String) = viewModelScope.launch {
        mealRepository.deleteById(mealId)
    }

    fun insertPlannedMeal(plannedMeal: PlannedMeal) = viewModelScope.launch {
        mealRepository.insertPlannedMeal(plannedMeal)
    }

    fun deletePlannedMeal(mealId: String, date: String) = viewModelScope.launch {
        mealRepository.deletePlannedMeal(mealId, date)
    }
}
