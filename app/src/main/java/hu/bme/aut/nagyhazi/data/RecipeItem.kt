package hu.bme.aut.nagyhazi.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter

@Entity(tableName = "recipeitem")
data class RecipeItem (
    @ColumnInfo(name = "id") @PrimaryKey(autoGenerate = true) val id: Long?,
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "recipe") val recipe: String,
    @ColumnInfo(name = "category") val category: String,
    @ColumnInfo(name = "estimated_price") val estimatedPrice: Int,
    @ColumnInfo(name = "is_saved") var isFavourite: Boolean
)