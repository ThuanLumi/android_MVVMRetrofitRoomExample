package com.office.mvvmretrofitroomexample.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.office.mvvmretrofitroomexample.activities.MainActivity
import com.office.mvvmretrofitroomexample.adapters.MealsAdapter
import com.office.mvvmretrofitroomexample.databinding.FragmentFavoriteBinding
import com.office.mvvmretrofitroomexample.viewmodel.HomeViewModel

class FavoriteFragment : Fragment() {
   private lateinit var binding: FragmentFavoriteBinding
   private lateinit var viewModel: HomeViewModel
   private lateinit var favoriteMealsAdapter: MealsAdapter

   override fun onCreate(savedInstanceState: Bundle?) {
      super.onCreate(savedInstanceState)
      viewModel = (activity as MainActivity).viewModel
   }

   override fun onCreateView(
      inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
   ): View? {
      binding = FragmentFavoriteBinding.inflate(inflater, container, false)
      return binding.root
//        // Inflate the layout for this fragment
//        return inflater.inflate(R.layout.fragment_favorite, container, false)
   }

   override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
      super.onViewCreated(view, savedInstanceState)

      prepareRecyclerView()

      observeFavorites()

      val itemTouchHelper = object : ItemTouchHelper.SimpleCallback(
         ItemTouchHelper.UP or ItemTouchHelper.DOWN, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
      ) {
         override fun onMove(
            recyclerView: RecyclerView,
            viewHolder: RecyclerView.ViewHolder,
            target: RecyclerView.ViewHolder
         ): Boolean {
            return true
         }

         override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
            val position = viewHolder.adapterPosition
            viewModel.deleteMeal(favoriteMealsAdapter.differ.currentList[position])

            Snackbar.make(requireView(), "Meal Deleted", Snackbar.LENGTH_LONG)
               .setAction("Undo", View.OnClickListener {
                  viewModel.insertMeal(favoriteMealsAdapter.differ.currentList[position])
               })
               .show()
         }
      }

      ItemTouchHelper(itemTouchHelper).attachToRecyclerView(binding.rvFavorites)
   }

   private fun prepareRecyclerView() {
      favoriteMealsAdapter = MealsAdapter()
      binding.rvFavorites.apply {
         layoutManager = GridLayoutManager(context, 2, GridLayoutManager.VERTICAL, false)
         adapter = favoriteMealsAdapter
      }
   }

   private fun observeFavorites() {
      viewModel.observeFavoriteMealsLiveData().observe(requireActivity(), Observer { meals ->
         favoriteMealsAdapter.differ.submitList(meals)
      })
   }
}