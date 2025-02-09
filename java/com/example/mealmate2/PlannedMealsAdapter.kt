package com.example.mealmate2

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.mealmate2.databinding.ItemPlannedMealBinding

class PlannedMealsAdapter(private val onDeleteClick: (PlannedMeal) -> Unit) :
    RecyclerView.Adapter<PlannedMealsAdapter.MealViewHolder>() {

    private var plannedMealsList: List<PlannedMeal> = listOf()

    fun submitList(plannedMeals: List<PlannedMeal>) {
        plannedMealsList = plannedMeals
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MealViewHolder {
        val binding = ItemPlannedMealBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MealViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MealViewHolder, position: Int) {
        holder.bind(plannedMealsList[position])
    }

    override fun getItemCount(): Int = plannedMealsList.size

    inner class MealViewHolder(private val binding: ItemPlannedMealBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(plannedMeal: PlannedMeal) {
            binding.textMealName.text = plannedMeal.mealName
            binding.textMealDate.text = "Date: ${plannedMeal.date}"

            Glide.with(binding.imageMeal.context)
                .load(plannedMeal.mealImage)
                .placeholder(R.drawable.placeholder_image)
                .into(binding.imageMeal)

            binding.buttonDeleteMeal.setOnClickListener {
                onDeleteClick(plannedMeal)
            }
        }
    }
}
