package hu.bme.aut.nagyhazi.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import hu.bme.aut.nagyhazi.R
import kotlinx.android.synthetic.main.activity_recipe.*

class RecipeActivity : AppCompatActivity() {
    private var NAME: String? = null
    private var CATEGORY: String? = null
    private var INSTRUCTIONS: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recipe)

        NAME = intent.getStringExtra(EXTRA_RECIPE_NAME)
        CATEGORY = intent.getStringExtra(EXTRA_RECIPE_CATEGORY)
        INSTRUCTIONS = intent.getStringExtra(EXTRA_RECIPE_INSTRUCTIONS)

        recipeName.text = NAME
        recipeCategory.text = CATEGORY
        recipeInstructions.text = INSTRUCTIONS

    }

    companion object {
        private const val TAG = "DetailsActivity"
        const val EXTRA_RECIPE_NAME = "extra.recipe_name"
        const val EXTRA_RECIPE_CATEGORY = "extra.recipe_category"
        const val EXTRA_RECIPE_INSTRUCTIONS = "extra.recipe_instructions"
    }
}