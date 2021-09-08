package hu.bme.aut.nagyhazi.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import hu.bme.aut.nagyhazi.data.RecipeItem
import hu.bme.aut.nagyhazi.data.RecipeItemDao

@Database(entities = [RecipeItem::class], version = 1)
//@TypeConverters(value = [RecipeItem.Category::class])
abstract class RecipeItemDatabase : RoomDatabase() {
    abstract fun recipeItemDao(): RecipeItemDao
}