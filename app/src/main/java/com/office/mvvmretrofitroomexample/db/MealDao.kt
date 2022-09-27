package com.office.mvvmretrofitroomexample.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.office.mvvmretrofitroomexample.pojo.Meal

@Dao
interface MealDao {
   @Insert(onConflict = OnConflictStrategy.REPLACE)
   suspend fun upsert(meal: Meal)

   @Update
   suspend fun updateMeal(meal: Meal)

   @Delete
   suspend fun deleteMeal(meal: Meal)

   @Query("SELECT * FROM mealInformation")
   fun getAllMeals(): LiveData<List<Meal>>
}