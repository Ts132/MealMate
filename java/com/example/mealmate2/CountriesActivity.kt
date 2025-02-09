package com.example.mealmate2

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mealmate2.databinding.ActivityCountriesBinding
import com.example.mealmate2.models.Country
import kotlinx.coroutines.launch

class CountryActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCountriesBinding
    private lateinit var countryAdapter: CountryAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCountriesBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.bottomNavigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_favorites -> startActivity(Intent(this, FavoritsActivity::class.java))
                R.id.nav_search -> startActivity(Intent(this, SearchActivity::class.java))
                R.id.nav_plan -> startActivity(Intent(this, CalendarActivity::class.java))
            }
            true
            }
        setupRecyclerView()
        fetchCountries()
    }

    private fun setupRecyclerView() {
        countryAdapter = CountryAdapter { country ->
            openMealsByCountry(country)
        }
        binding.rvCountries.apply {
            layoutManager = LinearLayoutManager(this@CountryActivity)
            adapter = countryAdapter
        }
    }

    private fun fetchCountries() {
        lifecycleScope.launch {
            try {
                val response = RetrofitInstance.api.getAllCountries()
                if (response.isSuccessful) {
                    val countries = response.body()?.meals?.map { Country(it.strArea) } ?: emptyList()
                    countryAdapter.submitList(countries)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private fun openMealsByCountry(country: Country) {
        val intent = Intent(this, Mealc::class.java).apply {
            putExtra("COUNTRY_NAME", country.strArea)
        }
        startActivity(intent)
    }
}