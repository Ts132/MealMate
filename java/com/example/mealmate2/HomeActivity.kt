package com.example.mealmate2

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mealmate2.databinding.ActivityHomeBinding
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding
    private lateinit var mealAdapter: MealsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupRecyclerView()
        fetchMeals()

        binding.bottomNavigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_favorites -> startActivity(Intent(this, FavoritsActivity::class.java))
                R.id.nav_search -> startActivity(Intent(this, SearchActivity::class.java))
                R.id.nav_plan -> startActivity(Intent(this, CalendarActivity::class.java))
            }
            true
        }
    }

    private fun setupRecyclerView() {
        mealAdapter = MealsAdapter(
            onMealClick = { meal -> openMealDetails(meal) },
            onAddToFavoritesClick = { meal ->
                FavoritesManager.toggleFavorite(meal)
            }
        )



        binding.rvMeals.apply {
            adapter = mealAdapter
            layoutManager = LinearLayoutManager(this@HomeActivity)
        }
    }

    private fun fetchMeals() {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://www.themealdb.com/api/json/v1/1/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val api = retrofit.create(MealApiService::class.java)

        lifecycleScope.launch {
            try {
                val mealsList = mutableListOf<Meal>()
                repeat(5) {  // Fetch 5 random meals
                    val response = api.getMealOfTheDay()
                    if (response.isSuccessful) {
                        response.body()?.meals?.let { mealsList.addAll(it) }
                    }
                }
                mealAdapter.submitList(mealsList)
            } catch (e: Exception) {
                e.printStackTrace()
                Toast.makeText(this@HomeActivity, "Error fetching meals: ${e.message}", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun openMealDetails(meal: Meal) {
        val intent = Intent(this, MealDetailsActivity::class.java).apply {
            putExtra("MEAL_ID", meal.idMeal)
            putExtra("MEAL_NAME", meal.strMeal)
            putExtra("MEAL_IMAGE", meal.strMealThumb)
            putExtra("MEAL_INSTRUCTIONS", meal.strInstructions)
            putExtra("MEAL_YOUTUBE", meal.strYoutube)
        }
        startActivity(intent)
    }
}
