package com.example.mealmate2

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.PopupMenu
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mealmate2.databinding.ActivityCalendarBinding
import java.text.SimpleDateFormat
import java.util.*
import com.example.mealmate2.MealViewModel

class CalendarActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCalendarBinding
    private lateinit var plannedMealsAdapter: PlannedMealsAdapter
    private val mealViewModel: MealViewModel by viewModels()
    private var selectedDate: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCalendarBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupRecyclerView()
        setupCalendarView()
        setupAddMealButton()
    }

    private fun setupRecyclerView() {
        plannedMealsAdapter = PlannedMealsAdapter { plannedMeal ->
            mealViewModel.deletePlannedMeal(plannedMeal.mealId, plannedMeal.date)
            fetchPlannedMealsForDate(selectedDate)
        }

        binding.recyclerViewMeals.apply {
            layoutManager = LinearLayoutManager(this@CalendarActivity)
            adapter = plannedMealsAdapter
        }
    }

    private fun setupCalendarView() {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

        binding.calendarView.setOnDateChangeListener { _, year, month, dayOfMonth ->
            selectedDate = "$year-${month + 1}-$dayOfMonth"
            fetchPlannedMealsForDate(selectedDate)
        }

        selectedDate = dateFormat.format(Date())
        fetchPlannedMealsForDate(selectedDate)
    }

    private fun fetchPlannedMealsForDate(date: String) {
        mealViewModel.getPlannedMealsForDate(date).observe(this, Observer { plannedMeals ->
            plannedMealsAdapter.submitList(plannedMeals)
        })
    }

    private fun setupAddMealButton() {
        binding.fabAddMeal.setOnClickListener { view ->
            showAddMealPopup(view)
        }
    }

    private fun showAddMealPopup(view: View) {
        val popupMenu = PopupMenu(this, view)
        popupMenu.menuInflater.inflate(R.menu.add_meal_menu, popupMenu.menu)

        popupMenu.setOnMenuItemClickListener { menuItem: MenuItem ->
            when (menuItem.itemId) {
                R.id.menu_add_saved -> {
                    val intent = Intent(this, FavoritsActivity::class.java)
                    intent.putExtra("selected_date", selectedDate)
                    startActivityForResult(intent, REQUEST_CODE_FAVORITES)
                }
                R.id.menu_search_meal -> {
                    val intent = Intent(this, SearchActivity::class.java)
                    intent.putExtra("selected_date", selectedDate)
                    startActivityForResult(intent, REQUEST_CODE_SEARCH)
                }
            }
            true
        }

        popupMenu.show()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == RESULT_OK && data != null) {
            val mealId = data.getStringExtra("meal_id")
            if (mealId != null) {
                mealViewModel.getMealById(mealId).observe(this, Observer { meal ->
                    if (meal != null) {
                        val plannedMeal = PlannedMeal(
                            mealId = meal.idMeal,
                            mealName = meal.strMeal,
                            mealImage = meal.strMealThumb,
                            date = selectedDate
                        )
                        mealViewModel.insertPlannedMeal(plannedMeal)
                        fetchPlannedMealsForDate(selectedDate)
                    }
                })
            }
        }
    }

    companion object {
        private const val REQUEST_CODE_FAVORITES = 1001
        private const val REQUEST_CODE_SEARCH = 1002
    }
}
