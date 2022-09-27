package com.office.mvvmretrofitroomexample.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.office.mvvmretrofitroomexample.R
import com.office.mvvmretrofitroomexample.activities.MainActivity
import com.office.mvvmretrofitroomexample.adapters.MealsAdapter
import com.office.mvvmretrofitroomexample.databinding.FragmentSearchBinding
import com.office.mvvmretrofitroomexample.viewmodel.HomeViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SearchFragment : Fragment() {
   private lateinit var binding: FragmentSearchBinding
   private lateinit var viewModel: HomeViewModel
   private lateinit var searchRecyclerViewAdapter: MealsAdapter

   override fun onCreate(savedInstanceState: Bundle?) {
      super.onCreate(savedInstanceState)

      viewModel = (activity as MainActivity).viewModel
   }

   override fun onCreateView(
      inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
   ): View? {
      binding = FragmentSearchBinding.inflate(inflater, container, false)
      return binding.root
//      // Inflate the layout for this fragment
//      return inflater.inflate(R.layout.fragment_search, container, false)
   }

   override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
      super.onViewCreated(view, savedInstanceState)

      prepareRecyclerView()

      binding.imgSearchArrow.setOnClickListener {
         searchMeal()
      }

      observeSearchMealLiveData()

      var searchJob: Job? = null
      binding.etSearchBox.addTextChangedListener { searchQuery ->
         searchJob?.cancel()
         searchJob = lifecycleScope.launch {
            delay(500)
            viewModel.searchMeal(searchQuery.toString())
         }
      }
   }

   private fun searchMeal() {
      val searchQuery = binding.etSearchBox.text.toString()

      if (searchQuery.isNotEmpty()) {
         viewModel.searchMeal(searchQuery)
      }
   }

   private fun prepareRecyclerView() {
      searchRecyclerViewAdapter = MealsAdapter()
      binding.rvSearchMeal.apply {
         layoutManager = GridLayoutManager(context, 2, GridLayoutManager.VERTICAL, false)
         adapter = searchRecyclerViewAdapter
      }
   }

   private fun observeSearchMealLiveData() {
      viewModel.observeSearchMealLiveData().observe(viewLifecycleOwner, Observer { mealList ->
         searchRecyclerViewAdapter.differ.submitList(mealList)
      })
   }
}