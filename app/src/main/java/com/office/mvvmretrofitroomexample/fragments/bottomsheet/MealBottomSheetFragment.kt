package com.office.mvvmretrofitroomexample.fragments.bottomsheet

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.office.mvvmretrofitroomexample.R
import com.office.mvvmretrofitroomexample.activities.MainActivity
import com.office.mvvmretrofitroomexample.activities.MealActivity
import com.office.mvvmretrofitroomexample.databinding.FragmentMealBottomSheetBinding
import com.office.mvvmretrofitroomexample.fragments.HomeFragment
import com.office.mvvmretrofitroomexample.viewmodel.HomeViewModel

private const val MEAL_ID = "param1"

class MealBottomSheetFragment : BottomSheetDialogFragment() {
   private var mealId: String? = null
   private var mealName: String? = null
   private var mealThumb: String? = null

   private lateinit var binding: FragmentMealBottomSheetBinding
   private lateinit var viewModel: HomeViewModel

   override fun onCreate(savedInstanceState: Bundle?) {
      super.onCreate(savedInstanceState)
      arguments?.let {
         mealId = it.getString(MEAL_ID)
      }

      viewModel = (activity as MainActivity).viewModel
   }

   override fun onCreateView(
      inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
   ): View? {
      binding = FragmentMealBottomSheetBinding.inflate(inflater, container, false)
      return binding.root
//      // Inflate the layout for this fragment
//      return inflater.inflate(R.layout.fragment_meal_bottom_sheet, container, false)
   }

   override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
      super.onViewCreated(view, savedInstanceState)

      mealId?.let {
         viewModel.getMealById(it)
      }

      observeBottomSheetMeal()

      obBottomSheetDialogClick()
   }

   private fun obBottomSheetDialogClick() {
      binding.bottomSheet.setOnClickListener {
         if (mealName != null && mealThumb != null) {
            val intent = Intent(activity, MealActivity::class.java)

            intent.apply {
               putExtra(HomeFragment.MEAL_ID, mealId)
               putExtra(HomeFragment.MEAL_NAME, mealName)
               putExtra(HomeFragment.MEAL_THUMB, mealThumb)
            }

            startActivity(intent)
         }
      }
   }

   private fun observeBottomSheetMeal() {
      viewModel.observeBottomSheetMealLiveData().observe(viewLifecycleOwner, Observer { meal ->
         Glide.with(this).load(meal.strMealThumb).into(binding.imgBottomSheet)

         binding.tvBottomSheetArea.text = meal.strArea
         binding.tvBottomSheetCategory.text = meal.strCategory
         binding.tvBottomSheetMealName.text = meal.strMeal

         mealName = meal.strMeal
         mealThumb = meal.strMealThumb
      })
   }

   companion object {
      @JvmStatic
      fun newInstance(param1: String) = MealBottomSheetFragment().apply {
         arguments = Bundle().apply {
            putString(MEAL_ID, param1)
         }
      }
   }
}