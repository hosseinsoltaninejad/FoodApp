package ir.hossein.foodapp.room

import androidx.room.*

@Dao
interface FoodDao {

    // insert Or Update
    @Insert( onConflict = OnConflictStrategy.REPLACE )
    fun insertOrUpdate( food: Food)

    @Insert
    fun insertFood ( food: Food )

    @Insert
    fun insertAllFoods ( data : List<Food> )

    @Update
    fun updateFood ( food: Food )

    @Delete
    fun deleteFood ( food: Food )

    @Query( "DELETE FROM table_food" )
    fun deleteAllFoods ()

    @Query( "SELECT * FROM table_food" )
    fun getAllFoods () : List<Food>

    @Query( "SELECT * FROM table_food WHERE txtSubject LIKE '%' || :searching || '%'" )
    fun searchFoods ( searching : String ) : List<Food>

}