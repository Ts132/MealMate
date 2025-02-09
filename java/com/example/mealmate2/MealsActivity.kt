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

class MealsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMealsBinding
    private lateinit var mealsAdapter: MealsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMealsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Receive the category name from the intent
        val categoryName = intent.getStringExtra("CATEGORY_NAME") ?: ""
        binding.tvCategoryName.text = categoryName

        // Set up RecyclerView
        setupRecyclerView()

        // Fetch meals based on category
        fetchMealsByCategory(categoryName)
    }

    private fun setupRecyclerView() {
        mealsAdapter = MealsAdapter(
            onMealClick = { meal -> openMealDetails(meal) },
            onAddToFavoritesClick = { meal ->
                FavoritesManager.toggleFavorite(meal)
            }
        )


        binding.rvMeals.apply {
            layoutManager = LinearLayoutManager(this@MealsActivity)
            adapter = mealsAdapter
        }
    }

    private fun fetchMealsByCategory(category: String) {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://www.themealdb.com/api/json/v1/1/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val api = retrofit.create(MealApiService::class.java)

        lifecycleScope.launch {
            try {
                val response = api.getMealsByCategory(category)
                if (response.isSuccessful) {
                    response.body()?.meals?.let { mealsAdapter.submitList(it) }
                } else {
                    Toast.makeText(this@MealsActivity, "Failed to load meals", Toast.LENGTH_LONG).show()
                }
            } catch (e: Exception) {
                e.printStackTrace()
                Toast.makeText(this@MealsActivity, "Error fetching meals", Toast.LENGTH_LONG).show()
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
                        val intent = Intent(this@MealsActivity, MealDetailsActivity::class.java).apply {
                            putExtra("MEAL_ID", detailedMeal.idMeal)
                            putExtra("MEAL_NAME", detailedMeal.strMeal)
                            putExtra("MEAL_IMAGE", detailedMeal.strMealThumb)
                            putExtra("MEAL_INSTRUCTIONS", detailedMeal.strInstructions)
                            putExtra("MEAL_YOUTUBE", detailedMeal.strYoutube)
                        }
                        startActivity(intent)
                    }
                } else {
                    Toast.makeText(this@MealsActivity, "Failed to load meal details", Toast.LENGTH_LONG).show()
                }
            } catch (e: Exception) {
                e.printStackTrace()
                Toast.makeText(this@MealsActivity, "Error fetching meal details", Toast.LENGTH_LONG).show()
            }
        }
    }
}
