package hu.bme.aut.nagyhazi.activity

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room.*
import com.google.android.material.snackbar.Snackbar
import hu.bme.aut.nagyhazi.R
import hu.bme.aut.nagyhazi.data.RecipeItem
import hu.bme.aut.nagyhazi.data.RecipeItemDatabase
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_recipe.*
import kotlin.concurrent.thread

/*
*
*
*  Link ami bemutatja az alkalmazást:
*
*  https://drive.google.com/file/d/1WxzLHa8c0q9Q03s94OgLiB5--BCn9ta9/view?usp=sharing
*
*
*
*
*
 */

class MainActivity : AppCompatActivity(), RecipeAdapter.OnFoodSelectedListener, AddRecipeDialogFragment.AddRecipeDialogListener {

    private lateinit var adapter: RecipeAdapter
    // private lateinit var database: RecipeItemDatabase
    private lateinit var recyclerView: RecyclerView

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate( R.menu.menu_main, menu)
        return super.onCreateOptionsMenu(menu)
    }
    private fun loadItemsInBackground() {
        thread {
            val items = Database.database.recipeItemDao().getAll()
            runOnUiThread {
                adapter.update(items)
            }
        }
    }

    override fun onPause() {
        super.onPause()
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId){
            R.id.delete_all ->{
                val snack = Snackbar.make(findViewById(android.R.id.content), "Are you sure?", Snackbar.LENGTH_SHORT)
                snack.setAction("Yes", { adapter.removeAll() })
                snack.show()
                true
            }
            R.id.show_favourites ->{
                //dapter.update(adapter.getFavourites())
                val showDetailsIntent = Intent()
                showDetailsIntent.setClass(this@MainActivity, FavouriteActivity::class.java)

                startActivity(showDetailsIntent)
                true
            }
            R.id.new_recipe->{
                AddRecipeDialogFragment().show(supportFragmentManager, AddRecipeDialogFragment::class.java.simpleName)
                true
            }else-> super.onOptionsItemSelected(item)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recipe_button.setOnClickListener {
            AddRecipeDialogFragment().show(supportFragmentManager, AddRecipeDialogFragment::class.java.simpleName)
        }
        Database.database = databaseBuilder(
            applicationContext,
            RecipeItemDatabase::class.java,
            "recipe-list"
        ).build()
        initRecyclerView()
    }
    private fun initRecyclerView() {
        recyclerView = MainRecyclerView

        adapter = RecipeAdapter(this)

        loadItemsInBackground()
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter


        MainRecyclerView.layoutManager = LinearLayoutManager(this)
    }
    override fun onRecipeAdded(recipe: RecipeItem) {
        thread {
            // recipe.isFavourite = favouritesDisplayed
            val newId = Database.database.recipeItemDao().insert(recipe)
            val newRecipeItem = recipe.copy(
                id = newId
            )
            runOnUiThread {
                adapter.addRecipe(newRecipeItem)
            }
        }
    }

    override fun onRecipeSelected(recipe: RecipeItem?) {
        val showDetailsIntent = Intent()
        showDetailsIntent.setClass(this@MainActivity, RecipeActivity::class.java)
        if (recipe != null) {
            showDetailsIntent.putExtra(RecipeActivity.EXTRA_RECIPE_NAME, recipe.name)
            showDetailsIntent.putExtra(RecipeActivity.EXTRA_RECIPE_CATEGORY, recipe.category)
            showDetailsIntent.putExtra(RecipeActivity.EXTRA_RECIPE_INSTRUCTIONS, recipe.recipe)

        }
        startActivity(showDetailsIntent)
    }

    @SuppressLint("ShowToast", "ResourceType")
    override fun onRecipeRemoved(recipe: RecipeItem?) {

        thread{
            if (recipe != null) {

                Database.database.recipeItemDao().deleteItem(recipe)
            }
        }
        // Toast.makeText(this,"Recipe Removed!", Toast.LENGTH_SHORT).show()
        Snackbar.make(findViewById(android.R.id.content), "Recipe Removed!", Snackbar.LENGTH_SHORT).show()
    }

    override fun onRecipeChanged(recipe: RecipeItem?) {
        thread {
            if (recipe != null) {
                Database.database.recipeItemDao().update(recipe)
            }
            Log.d("MainActivity", "RecipeItem update was successful")
        }
    }

}