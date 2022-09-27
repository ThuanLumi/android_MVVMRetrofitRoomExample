package com.office.mvvmretrofitroomexample.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import com.office.mvvmretrofitroomexample.R
import com.office.mvvmretrofitroomexample.activities.MainActivity
import com.office.mvvmretrofitroomexample.adapters.CategoriesAdapter
import com.office.mvvmretrofitroomexample.databinding.FragmentCategoryBinding
import com.office.mvvmretrofitroomexample.viewmodel.HomeViewModel

class CategoryFragment : Fragment() {
   private lateinit var binding: FragmentCategoryBinding
   private lateinit var categoriesAdapter: CategoriesAdapter
   private lateinit var viewModel: HomeViewModel

   override fun onCreate(savedInstanceState: Bundle?) {
      super.onCreate(savedInstanceState)

      viewModel = (activity as MainActivity).viewModel
   }

   override fun onCreateView(
      inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
   ): View? {
      binding = FragmentCategoryBinding.inflate(inflater, container, false)
      return binding.root
//        // Inflate the layout for this fragment
//        return inflater.inflate(R.layout.fragment_category, container, false)
   }

   override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
      super.onViewCreated(view, savedInstanceState)

      prepareRecyclerView()

      observeCategories()
   }

   private fun prepareRecyclerView() {
      categoriesAdapter = CategoriesAdapter()
      binding.rvCategories.apply {
         layoutManager = GridLayoutManager(context, 3, GridLayoutManager.VERTICAL, false)
         adapter = categoriesAdapter
      }
   }

   private fun observeCategories() {
      viewModel.observeCategoryLiveData().observe(viewLifecycleOwner, Observer { categories ->
         categoriesAdapter.setCategoryList(categories)
      })
   }
}