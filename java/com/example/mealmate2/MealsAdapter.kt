package com.example.mealmate2

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.mealmate2.databinding.ItemMealBinding

class MealsAdapter(
    private val onMealClick: (Meal) -> Unit,
    private val onAddToFavoritesClick: (Meal) -> Unit
) : RecyclerView.Adapter<MealsAdapter.MealViewHolder>() {

    private val meals = mutableListOf<Meal>()

    fun submitList(newMeals: List<Meal>) {
        meals.clear()
        meals.addAll(newMeals)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MealViewHolder {
        val binding = ItemMealBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MealViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MealViewHolder, position: Int) {
        val meal = meals[position]
        holder.bind(meal)
    }

    override fun getItemCount(): Int = meals.size

    inner class MealViewHolder(private val binding: ItemMealBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(meal: Meal) {
            var isFavorite = FavoritesManager.isFavorite(meal)
            updateFavoriteButton(isFavorite)

            binding.tvMealName.text = meal.strMeal
            Glide.with(binding.root.context)
                .load(meal.strMealThumb)
                .placeholder(R.drawable.placeholder_image)
                .into(binding.ivMealImage)

            binding.root.setOnClickListener {
                onMealClick(meal)
            }

            binding.btnAddToFavorite.setOnClickListener {
                isFavorite = !isFavorite
                FavoritesManager.toggleFavorite(meal)
                updateFavoriteButton(isFavorite)
            }
        }

        private fun updateFavoriteButton(isFavorite: Boolean) {
            binding.btnAddToFavorite.text =
                if (isFavorite) "Remove from Favorite" else "Add to Favorite"
        }
    }
}
