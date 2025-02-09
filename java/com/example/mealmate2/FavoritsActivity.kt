package com.example.mealmate2

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mealmate2.databinding.ActivityFavoritsBinding

class FavoritsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityFavoritsBinding
    private lateinit var mealAdapter: MealsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFavoritsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupBottomNavigation()

        mealAdapter = MealsAdapter(
            onMealClick = { meal -> openMealDetails(meal) },
            onAddToFavoritesClick = { meal ->
                FavoritesManager.toggleFavorite(meal)
                updateFavoritesList()
            }
        )


        binding.rvFavorites.apply {
            adapter = mealAdapter
            layoutManager = LinearLayoutManager(this@FavoritsActivity)
        }

        updateFavoritesList()
    }

    private fun updateFavoritesList() {
        mealAdapter.submitList(FavoritesManager.getFavoriteMeals().toList())
    }

    private fun setupBottomNavigation() {
        binding.bottomNavigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_favorites -> startActivity(Intent(this, FavoritsActivity::class.java))
                R.id.nav_search -> startActivity(Intent(this, SearchActivity::class.java))
                R.id.nav_plan -> startActivity(Intent(this, CalendarActivity::class.java))
            }
            true
        }
    }
    private fun onMealSelected(meal: Meal) {
        val resultIntent = Intent()
        resultIntent.putExtra("meal_id", meal.idMeal)
        setResult(RESULT_OK, resultIntent)
        finish()
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
