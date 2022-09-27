package com.office.mvvmretrofitroomexample.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.office.mvvmretrofitroomexample.databinding.MealItemBinding
import com.office.mvvmretrofitroomexample.pojo.Meal

class MealsAdapter : RecyclerView.Adapter<MealsAdapter.FavoriteMealsViewHolder>() {

   private val diffUtil = object : DiffUtil.ItemCallback<Meal>() {
      override fun areItemsTheSame(oldItem: Meal, newItem: Meal): Boolean {
         return oldItem.idMeal == newItem.idMeal
      }

      override fun areContentsTheSame(oldItem: Meal, newItem: Meal): Boolean {
         return oldItem == newItem
      }
   }

   val differ = AsyncListDiffer(this, diffUtil)

   override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteMealsViewHolder {
      return FavoriteMealsViewHolder(
         MealItemBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
         )
      )
   }

   override fun onBindViewHolder(holder: FavoriteMealsViewHolder, position: Int) {
      val meal = differ.currentList[position]
      Glide.with(holder.itemView).load(meal.strMealThumb).into(holder.binding.imgMeal)

      holder.binding.tvMealName.text = meal.strMeal
   }

   override fun getItemCount(): Int {
      return differ.currentList.size
   }

   inner class FavoriteMealsViewHolder(val binding: MealItemBinding) :
      RecyclerView.ViewHolder(binding.root)
}