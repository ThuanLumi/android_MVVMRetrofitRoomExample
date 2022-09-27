package com.office.mvvmretrofitroomexample.activities

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.office.mvvmretrofitroomexample.R
import com.office.mvvmretrofitroomexample.databinding.ActivityMealBinding
import com.office.mvvmretrofitroomexample.db.MealDatabase
import com.office.mvvmretrofitroomexample.fragments.HomeFragment
import com.office.mvvmretrofitroomexample.pojo.Meal
import com.office.mvvmretrofitroomexample.viewmodel.MealViewModel
import com.office.mvvmretrofitroomexample.viewmodel.MealViewModelFactory

class MealActivity : AppCompatActivity() {
   private lateinit var binding: ActivityMealBinding

   private lateinit var mealId : String
   private lateinit var mealName : String
   private lateinit var mealThumb : String
   private lateinit var youtubeLink : String

   private lateinit var mealMvvm : MealViewModel

   private var mealToSave : Meal? = null

   override fun onCreate(savedInstanceState: Bundle?) {
      super.onCreate(savedInstanceState)
      binding = ActivityMealBinding.inflate(layoutInflater)
      setContentView(binding.root)

      val mealDatabase  = MealDatabase.getInstance(this)
      val viewModelFactory = MealViewModelFactory(mealDatabase)
      mealMvvm = ViewModelProvider(this, viewModelFactory)[MealViewModel::class.java]

      getMealInformationFromIntent()
      setInformationInView()

      loadingCase()

      mealMvvm.getMealDetail(mealId)
      observeMealDetailLiveData()

      onYoutubeImageClick()
      onFavoriteClick()
   }

   private fun getMealInformationFromIntent() {
      val intent = intent
      mealId = intent.getStringExtra(HomeFragment.MEAL_ID)!!
      mealName = intent.getStringExtra(HomeFragment.MEAL_NAME)!!
      mealThumb = intent.getStringExtra(HomeFragment.MEAL_THUMB)!!
   }

   private fun setInformationInView() {
      Glide.with(applicationContext)
         .load(mealThumb)
         .into(binding.imgMealDetail)

      binding.collapsingToolbar.title = mealName
   }

   private fun observeMealDetailLiveData() {
      mealMvvm.observeMealDetailLiveData().observe(this, object : Observer<Meal>{
         override fun onChanged(t: Meal?) {
            onResponseCase()

            val meal = t

            mealToSave = meal

            binding.tvCategories.text = "Category : ${meal!!.strCategory}"
            binding.tvArea.text = "Area : ${meal!!.strArea}"
            binding.tvInstructionStep.text = meal.strInstructions

            youtubeLink = meal.strYoutube.toString()
         }
      })
   }

   private fun onYoutubeImageClick() {
      binding.imgYoutube.setOnClickListener {
         val intent = Intent(Intent.ACTION_VIEW, Uri.parse(youtubeLink))
         startActivity(intent)
      }
   }

   private fun onFavoriteClick() {
      binding.btnAddToFavorite.setOnClickListener {
         mealToSave?.let {
            mealMvvm.insertMeal(it)
            Toast.makeText(this, "Meal Saved", Toast.LENGTH_SHORT).show()
         }
      }
   }

   private fun loadingCase() {
      binding.progressBar.visibility = View.VISIBLE
      binding.btnAddToFavorite.visibility = View.INVISIBLE
      binding.tvInstruction.visibility = View.INVISIBLE
      binding.tvCategories.visibility = View.INVISIBLE
      binding.tvArea.visibility = View.INVISIBLE
      binding.imgYoutube.visibility = View.INVISIBLE
   }

   private fun onResponseCase() {
      binding.progressBar.visibility = View.INVISIBLE
      binding.btnAddToFavorite.visibility = View.VISIBLE
      binding.tvInstruction.visibility = View.VISIBLE
      binding.tvCategories.visibility = View.VISIBLE
      binding.tvArea.visibility = View.VISIBLE
      binding.imgYoutube.visibility = View.VISIBLE
   }
}