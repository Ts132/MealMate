package com.example.mealmate2

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.mealmate2.databinding.ActivityCreateMealBinding

class CreateMealActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCreateMealBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateMealBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupUI()
    }

    private fun setupUI() {
        binding.btnSaveMeal.setOnClickListener {
         }
    }
}
