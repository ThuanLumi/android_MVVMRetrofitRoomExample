package com.office.mvvmretrofitroomexample.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.office.mvvmretrofitroomexample.R
import com.office.mvvmretrofitroomexample.db.MealDatabase
import com.office.mvvmretrofitroomexample.viewmodel.HomeViewModel
import com.office.mvvmretrofitroomexample.viewmodel.HomeViewModelFactory

class MainActivity : AppCompatActivity() {
   val viewModel: HomeViewModel by lazy {
      val mealDatabase = MealDatabase.getInstance(this)
      val homeViewModelProviderFactory = HomeViewModelFactory(mealDatabase)
      ViewModelProvider(this, homeViewModelProviderFactory)[HomeViewModel::class.java]
   }

   override fun onCreate(savedInstanceState: Bundle?) {
      super.onCreate(savedInstanceState)
      setContentView(R.layout.activity_main)

      val bottomNavigation = findViewById<BottomNavigationView>(R.id.btm_nav)
      val navController = Navigation.findNavController(this, R.id.host_fragment)

      NavigationUI.setupWithNavController(bottomNavigation, navController)
   }
}