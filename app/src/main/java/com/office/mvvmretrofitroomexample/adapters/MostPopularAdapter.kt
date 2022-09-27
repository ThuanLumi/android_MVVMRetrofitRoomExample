package com.office.mvvmretrofitroomexample.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.office.mvvmretrofitroomexample.databinding.PopularItemBinding
import com.office.mvvmretrofitroomexample.pojo.MealsByCategory

class MostPopularAdapter() : RecyclerView.Adapter<MostPopularAdapter.PopularMealViewHolder>() {
   private var mealsList = ArrayList<MealsByCategory>()
   lateinit var onItemClick: ((MealsByCategory) -> Unit)
   var onLongItemClick: ((MealsByCategory) -> Unit)? = null

   override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PopularMealViewHolder {
      return PopularMealViewHolder(
         PopularItemBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
         )
      )
   }

   override fun onBindViewHolder(holder: PopularMealViewHolder, position: Int) {
      Glide.with(holder.itemView)
         .load(mealsList[position].strMealThumb)
         .into(holder.binding.imgPopularMealItem)

      holder.itemView.setOnClickListener {
         onItemClick.invoke(mealsList[position])
      }

      holder.itemView.setOnLongClickListener {
         onLongItemClick?.invoke(mealsList[position])
         true
      }
   }

   override fun getItemCount(): Int {
      return mealsList.size
   }

   fun setMeals(mealsList: ArrayList<MealsByCategory>) {
      this.mealsList = mealsList
      notifyDataSetChanged()
   }

   inner class PopularMealViewHolder(val binding: PopularItemBinding) :
      RecyclerView.ViewHolder(binding.root)
}