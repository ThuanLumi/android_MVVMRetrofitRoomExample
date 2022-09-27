package com.office.mvvmretrofitroomexample.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.office.mvvmretrofitroomexample.adapters.CategoryMealsAdapter
import com.office.mvvmretrofitroomexample.databinding.ActivityCategoryMealsBinding
import com.office.mvvmretrofitroomexample.fragments.HomeFragment
import com.office.mvvmretrofitroomexample.viewmodel.CategoryMealsViewModel

class CategoryMealsActivity : AppCompatActivity() {
   lateinit var binding: ActivityCategoryMealsBinding
   lateinit var categoryMealsViewModel: CategoryMealsViewModel
   lateinit var categoryMealsAdapter: CategoryMealsAdapter

   override fun onCreate(savedInstanceState: Bundle?) {
      super.onCreate(savedInstanceState)
      binding = ActivityCategoryMealsBinding.inflate(layoutInflater)
      setContentView(binding.root)

      prepareRecyclerView()

      categoryMealsViewModel = ViewModelProvider(this)[CategoryMealsViewModel::class.java]

      categoryMealsViewModel.getMealsByCategory(intent.getStringExtra(HomeFragment.CATEGORY_NAME)!!)

      categoryMealsViewModel.observeMealsLiveData().observe(this, Observer { mealsList ->
         binding.tvCategoryCount.text = mealsList.size.toString()
         categoryMealsAdapter.setMealsList(mealsList)
      })
   }

   private fun prepareRecyclerView() {
      categoryMealsAdapter = CategoryMealsAdapter()
      binding.rvMeals.apply {
         layoutManager = GridLayoutManager(context, 2, GridLayoutManager.HORIZONTAL, false)
         adapter = categoryMealsAdapter
      }
   }
}