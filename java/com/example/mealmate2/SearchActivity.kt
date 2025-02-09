package com.example.mealmate2

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mealmate2.databinding.ActivitySearchBinding
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class SearchActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySearchBinding
    private lateinit var mealAdapter: MealsAdapter

    private val api: MealApiService by lazy {
        Retrofit.Builder()
            .baseUrl("https://www.themealdb.com/api/json/v1/1/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(MealApiService::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupRecyclerView()

        // ✅ التنقل إلى صفحات التصنيفات والدول
        binding.btnCategories.setOnClickListener {
            startActivity(Intent(this, CategoriesActivity::class.java))
        }

        binding.btnCountries.setOnClickListener {
            startActivity(Intent(this, CountryActivity::class.java))
        }

        binding.btnSearch.setOnClickListener {
            searchMeals(binding.etSearch.text.toString())
        }

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
            onMealClick = { meal -> openMealDetails(meal) }, // ✅ تغيير اسم المعامل إلى onMealClick
            onAddToFavoritesClick = { meal ->               // ✅ تغيير اسم المعامل إلى onAddToFavoritesClick
                FavoritesManager.toggleFavorite(meal)
            }
        )


        binding.rvMeals.apply {
            adapter = mealAdapter
            layoutManager = LinearLayoutManager(this@SearchActivity)
        }
    }

    private fun toggleFavorite(meal: Meal) {
        if (FavoritesManager.isFavorite(meal)) {
            FavoritesManager.removeFromFavorites(meal)
            Toast.makeText(this, "${meal.strMeal} removed from favorites", Toast.LENGTH_SHORT).show()
        } else {
            FavoritesManager.addToFavorites(meal)
            Toast.makeText(this, "${meal.strMeal} added to favorites", Toast.LENGTH_SHORT).show()
        }
    }

    private fun searchMeals(query: String) {
        lifecycleScope.launch {
            try {
                val response = api.searchMeals(query)
                if (response.isSuccessful) {
                    val meals = response.body()?.meals ?: emptyList() // ✅ استخدم قائمة فارغة إذا كانت القيمة null
                    mealAdapter.submitList(meals)
                } else {
                    Toast.makeText(this@SearchActivity, "Failed to fetch meals", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Toast.makeText(this@SearchActivity, "Error: ${e.message}", Toast.LENGTH_LONG).show()
            }
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
