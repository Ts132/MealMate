package com.example.mealmate2

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.mealmate2.databinding.ActivityMealDetailsBinding

class MealDetailsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMealDetailsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMealDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // استلام البيانات من الـ Intent
        val mealName = intent.getStringExtra("MEAL_NAME") ?: "No Name"
        val mealImage = intent.getStringExtra("MEAL_IMAGE")
        val mealInstructions = intent.getStringExtra("MEAL_INSTRUCTIONS") ?: "No Instructions"
        val mealYoutube = intent.getStringExtra("MEAL_YOUTUBE") ?: ""

        // عرض البيانات في الواجهة
        binding.tvMealTitle.text = mealName
        binding.tvMealInstructions.text = mealInstructions

        // تمكين التمرير في TextView الخاص بالتعليمات
        binding.tvMealInstructions.movementMethod = ScrollingMovementMethod()

        // تحميل الصورة باستخدام Glide
        Glide.with(this)
            .load(mealImage)
            .into(binding.ivMealImage)

        // عرض رابط اليوتيوب كنص قابل للنقر
        if (mealYoutube.isNotEmpty()) {
            binding.tvMealYoutube.text = "Watch Video: $mealYoutube"
            binding.tvMealYoutube.setOnClickListener {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(mealYoutube))
                startActivity(intent)
            }
        } else {
            binding.tvMealYoutube.text = "No video available"
        }
    }
}
