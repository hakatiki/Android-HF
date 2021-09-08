package hu.bme.aut.nagyhazi.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import androidx.annotation.DrawableRes
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import hu.bme.aut.nagyhazi.R
import hu.bme.aut.nagyhazi.data.RecipeItem
import hu.bme.aut.nagyhazi.data.RecipeItemDatabase
import kotlinx.android.synthetic.main.activity_favourite.*
import kotlinx.android.synthetic.main.content_recipe.*
import kotlinx.android.synthetic.main.recipe_row.view.*
import kotlin.concurrent.thread

class FavouriteActivity : AppCompatActivity() {

    private var items: List<RecipeItem> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favourite)
        Log.d("logging",Database.database.isOpen().toString())
        loadItemsInBackground()
        Log.d("logging", items.size.toString())

    }
    @DrawableRes
    private fun getImageResource(category: String): Int {
        if (  category == "Dessert")
            return R.drawable.dessert
        if (  category == "Soup")
            return R.drawable.soup
        if (  category == "Pizza")
            return R.drawable.pizza
        return 0
    }
    private fun loadItemsInBackground() {
        thread {
            val temp = Database.database.recipeItemDao().getAll()
            runOnUiThread {
                items = temp
                items.forEach { i ->
                    if (i.isFavourite){
                        val rowItem = LayoutInflater.from(this).inflate(R.layout.favourite_row, null)
                        rowItem.food_icon.setImageResource(getImageResource(i.category))
                        rowItem.row_food_name.text = i.name;
                        rowItem.row_food_category.text = i.category;

                        favourite_row.addView(rowItem)
                    }
                }
            }
         }
    }
}