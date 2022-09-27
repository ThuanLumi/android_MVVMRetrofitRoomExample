package com.office.mvvmretrofitroomexample.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.office.mvvmretrofitroomexample.db.MealDatabase
import com.office.mvvmretrofitroomexample.pojo.*
import com.office.mvvmretrofitroomexample.retrofit.RetrofitInstance
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeViewModel(private val mealDatabase: MealDatabase) : ViewModel() {
   private var randomMealLiveData = MutableLiveData<Meal>()
   private var popularItemLiveData = MutableLiveData<List<MealsByCategory>>()
   private var categoriesLiveData = MutableLiveData<List<Category>>()
   private var bottomSheetMealLiveData = MutableLiveData<Meal>()
   private var searchMealLiveData = MutableLiveData<List<Meal>>()

   private var favoriteMealsLiveData = mealDatabase.mealDao().getAllMeals()

   private var savedStateRandomMeal: Meal? = null

   fun getRandomMeal() {
      savedStateRandomMeal?.let { randomMeal ->
         randomMealLiveData.postValue(randomMeal)
         return
      }

      RetrofitInstance.api.getRandomMeal().enqueue(object : Callback<MealList> {
         override fun onResponse(call: Call<MealList>, response: Response<MealList>) {
            if (response.body() != null) {
               val randomMeal: Meal = response.body()!!.meals[0]
//                    Log.d("TEST", "meal id : ${randomMeal.idMeal} meal name : ${randomMeal.strMeal}")
               randomMealLiveData.value = randomMeal
               savedStateRandomMeal = randomMeal
            } else {
               return
            }
         }

         override fun onFailure(call: Call<MealList>, t: Throwable) {
            Log.d("HomeFragment", t.message.toString())
         }
      })
   }

   fun getPopularItems() {
      RetrofitInstance.api.getPopularItems("Seafood")
         .enqueue(object : Callback<MealsByCategoryList> {
            override fun onResponse(
               call: Call<MealsByCategoryList>, response: Response<MealsByCategoryList>
            ) {
               if (response.body() != null) {
                  popularItemLiveData.value = response.body()!!.meals
               } else {
                  return
               }
            }

            override fun onFailure(call: Call<MealsByCategoryList>, t: Throwable) {
               Log.d("HomeFragment", t.message.toString())
            }
         })
   }

   fun getCategories() {
      RetrofitInstance.api.getCategories().enqueue(object : Callback<CategoryList> {
         override fun onResponse(call: Call<CategoryList>, response: Response<CategoryList>) {
            response.body()?.let { categoryList ->
               categoriesLiveData.postValue(categoryList.categories)
            }
         }

         override fun onFailure(call: Call<CategoryList>, t: Throwable) {
            Log.e("HomeViewModel", t.message.toString())
         }
      })
   }

   fun getMealById(id: String) {
      RetrofitInstance.api.getMealDetail(id).enqueue(object : Callback<MealList> {
         override fun onResponse(call: Call<MealList>, response: Response<MealList>) {
            val meal = response.body()?.meals?.get(0)

            meal?.let { meal ->
               bottomSheetMealLiveData.postValue(meal)
            }
         }

         override fun onFailure(call: Call<MealList>, t: Throwable) {
            Log.e("HomeViewModel", t.message.toString())
         }
      })
   }

   fun insertMeal(meal: Meal) {
      viewModelScope.launch {
         mealDatabase.mealDao().upsert(meal)
      }
   }

   fun searchMeal(searchQuery: String) =
      RetrofitInstance.api.searchMeal(searchQuery).enqueue(object : Callback<MealList> {
         override fun onResponse(call: Call<MealList>, response: Response<MealList>) {
            val mealList = response.body()?.meals

            mealList?.let {
               searchMealLiveData.postValue(it)
            }
         }

         override fun onFailure(call: Call<MealList>, t: Throwable) {
            Log.e("HomeViewModel", t.message.toString())
         }
      })

   fun deleteMeal(meal: Meal) {
      viewModelScope.launch {
         mealDatabase.mealDao().deleteMeal(meal)
      }
   }

   fun observeRandomMealLiveData(): LiveData<Meal> {
      return randomMealLiveData
   }

   fun observePopularItemLiveData(): LiveData<List<MealsByCategory>> {
      return popularItemLiveData
   }

   fun observeCategoryLiveData(): LiveData<List<Category>> {
      return categoriesLiveData
   }

   fun observeFavoriteMealsLiveData(): LiveData<List<Meal>> {
      return favoriteMealsLiveData
   }

   fun observeBottomSheetMealLiveData(): LiveData<Meal> {
      return bottomSheetMealLiveData
   }

   fun observeSearchMealLiveData(): LiveData<List<Meal>> {
      return searchMealLiveData
   }
}