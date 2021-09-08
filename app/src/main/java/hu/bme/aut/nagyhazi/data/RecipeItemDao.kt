package hu.bme.aut.nagyhazi.data

import androidx.room.*
import hu.bme.aut.nagyhazi.data.RecipeItem

@Dao
interface RecipeItemDao {
    @Query("SELECT * FROM recipeitem")
    fun getAll(): List<RecipeItem>

    @Insert
    fun insert(shoppingItems: RecipeItem): Long

    @Update
    fun update(shoppingItem: RecipeItem)

    @Delete
    fun deleteItem(shoppingItem: RecipeItem)
}