package com.example.mealmate2

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mealmate2.databinding.ActivityMealsBinding
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class Mealc : AppCompatActivity() {
    private lateinit var binding: ActivityMealsBinding
    private lateinit var mealsAdapter: MealsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMealsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val countryName = intent.getStringExtra("COUNTRY_NAME") ?: ""
        binding.tvCategoryName.text = countryName

        setupRecyclerView()
        fetchMealsByCountry(countryName)
    }

    private fun setupRecyclerView() {
        mealsAdapter  = MealsAdapter(
            onMealClick = { meal -> openMealDetails(meal) },
            onAddToFavoritesClick = { meal ->
                FavoritesManager.toggleFavorite(meal)
            }
        )

        binding.rvMeals.apply {
            layoutManager = LinearLayoutManager(this@Mealc)
            adapter = mealsAdapter
        }
    }

    private fun fetchMealsByCountry(countryName: String) {
        lifecycleScope.launch {
            try {
                val response = RetrofitInstance.api.getMealsByCountry(countryName)
                if (response.isSuccessful) {
                    val meals = response.body()?.meals ?: emptyList()
                    println("Fetched meals: $meals")
                    mealsAdapter.submitList(meals)
                } else {
                    println("API Error: ${response.errorBody()?.string()}")
                }
            } catch (e: Exception) {
                println("Error fetching meals: ${e.message}")
            }
        }
    }

    private fun openMealDetails(meal: Meal) {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://www.themealdb.com/api/json/v1/1/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val api = retrofit.create(MealApiService::class.java)

        lifecycleScope.launch {
            try {
                val response = api.getMealDetails(meal.idMeal ?: "")
                if (response.isSuccessful) {
                    response.body()?.meals?.firstOrNull()?.let { detailedMeal ->
                        // Open the MealDetailsActivity with complete details
                        val intent = Intent(this@Mealc, MealDetailsActivity::class.java).apply {
                            putExtra("MEAL_ID", detailedMeal.idMeal)
                            putExtra("MEAL_NAME", detailedMeal.strMeal)
                            putExtra("MEAL_IMAGE", detailedMeal.strMealThumb)
                            putExtra("MEAL_INSTRUCTIONS", detailedMeal.strInstructions)
                            putExtra("MEAL_YOUTUBE", detailedMeal.strYoutube)
                        }
                        startActivity(intent)
                    }
                } else {
                    Toast.makeText(this@Mealc, "Failed to load meal details", Toast.LENGTH_LONG).show()
                }
            } catch (e: Exception) {
                e.printStackTrace()
                Toast.makeText(this@Mealc, "Error fetching meal details", Toast.LENGTH_LONG).show()
            }
}}}
