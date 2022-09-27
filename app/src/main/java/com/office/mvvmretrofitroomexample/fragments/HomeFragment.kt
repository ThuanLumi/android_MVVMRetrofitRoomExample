package com.office.mvvmretrofitroomexample.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.office.mvvmretrofitroomexample.R
import com.office.mvvmretrofitroomexample.activities.CategoryMealsActivity
import com.office.mvvmretrofitroomexample.activities.MainActivity
import com.office.mvvmretrofitroomexample.activities.MealActivity
import com.office.mvvmretrofitroomexample.adapters.CategoriesAdapter
import com.office.mvvmretrofitroomexample.adapters.MostPopularAdapter
import com.office.mvvmretrofitroomexample.databinding.FragmentHomeBinding
import com.office.mvvmretrofitroomexample.fragments.bottomsheet.MealBottomSheetFragment
import com.office.mvvmretrofitroomexample.pojo.MealsByCategory
import com.office.mvvmretrofitroomexample.pojo.Meal
import com.office.mvvmretrofitroomexample.viewmodel.HomeViewModel

class HomeFragment : Fragment() {
   private lateinit var binding: FragmentHomeBinding
   private lateinit var viewModel: HomeViewModel
   private lateinit var randomMeal: Meal

   private lateinit var popularItemsAdapter: MostPopularAdapter
   private lateinit var categoriesAdapter: CategoriesAdapter

   companion object {
      const val MEAL_ID = "com.office.mvvmretrofitroomexample.fragments.idMeal"
      const val MEAL_NAME = "com.office.mvvmretrofitroomexample.fragments.nameMeal"
      const val MEAL_THUMB = "com.office.mvvmretrofitroomexample.fragments.thumbMeal"
      const val CATEGORY_NAME = "com.office.mvvmretrofitroomexample.fragments.categoryName"
   }

   override fun onCreate(savedInstanceState: Bundle?) {
      super.onCreate(savedInstanceState)
      viewModel = (activity as MainActivity).viewModel
   }

   override fun onCreateView(
      inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
   ): View? {
      binding = FragmentHomeBinding.inflate(inflater, container, false)
      return binding.root
//        // Inflate the layout for this fragment
//        return inflater.inflate(R.layout.fragment_home, container, false)
   }

   override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
      super.onViewCreated(view, savedInstanceState)

      preparePopularItemsRecyclerView()

      viewModel.getRandomMeal()
      observeRandomMealLiveData()
      onRandomMealClick()

      viewModel.getPopularItems()
      observePopularItemsLiveData()
      onPopularItemClick()
      onPopularItemLongClick()

      prepareCategoriesRecyclerView()

      viewModel.getCategories()
      observeCategoriesLiveData()
      onCategoryClick()

      onSearchIconClick()
   }

   private fun preparePopularItemsRecyclerView() {
      popularItemsAdapter = MostPopularAdapter()

      binding.recViewMealsPopular.apply {
         layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
         adapter = popularItemsAdapter
      }
   }

   private fun prepareCategoriesRecyclerView() {
      categoriesAdapter = CategoriesAdapter()

      binding.recViewCategories.apply {
         layoutManager = GridLayoutManager(activity, 3, GridLayoutManager.VERTICAL, false)
         adapter = categoriesAdapter
      }
   }

   private fun observeRandomMealLiveData() {
      viewModel.observeRandomMealLiveData().observe(viewLifecycleOwner, object : Observer<Meal> {
         override fun onChanged(t: Meal?) {
            val meal = t

            Glide.with(this@HomeFragment).load(meal!!.strMeal).into(binding.imgRandomMeal)

            randomMeal = meal
         }
      })
   }

   private fun observePopularItemsLiveData() {
      viewModel.observePopularItemLiveData()
         .observe(viewLifecycleOwner, object : Observer<List<MealsByCategory>> {
            override fun onChanged(t: List<MealsByCategory>?) {
               popularItemsAdapter.setMeals(t as ArrayList<MealsByCategory>)
            }
         })
   }

   private fun observeCategoriesLiveData() {
      viewModel.observeCategoryLiveData().observe(viewLifecycleOwner, Observer { categories ->
         categoriesAdapter.setCategoryList(categories)
      })
   }

   private fun onRandomMealClick() {
      binding.imgRandomMeal.setOnClickListener {
         val intent = Intent(activity, MealActivity::class.java)
         intent.putExtra(MEAL_ID, randomMeal.idMeal)
         intent.putExtra(MEAL_NAME, randomMeal.strMeal)
         intent.putExtra(MEAL_THUMB, randomMeal.strMealThumb)
         startActivity(intent)
      }
   }

   private fun onPopularItemClick() {
      popularItemsAdapter.onItemClick = { meal ->
         val intent = Intent(activity, MealActivity::class.java)
         intent.putExtra(MEAL_ID, meal.idMeal)
         intent.putExtra(MEAL_NAME, meal.strMeal)
         intent.putExtra(MEAL_THUMB, meal.strMealThumb)
         startActivity(intent)
      }
   }

   private fun onPopularItemLongClick() {
      popularItemsAdapter.onLongItemClick = { meal ->
         val mealBottomSheetFragment = MealBottomSheetFragment.newInstance(meal.idMeal)
         mealBottomSheetFragment.show(childFragmentManager, "Meal Info")
      }
   }

   private fun onCategoryClick() {
      categoriesAdapter.onItemClick = { category ->
         val intent = Intent(activity, CategoryMealsActivity::class.java)
         intent.putExtra(CATEGORY_NAME, category.strCategory)
         startActivity(intent)
      }
   }

   private fun onSearchIconClick() {
      binding.imgSearch.setOnClickListener {
         findNavController().navigate(R.id.action_homeFragment_to_searchFragment)
      }
   }
}
