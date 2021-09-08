package hu.bme.aut.nagyhazi.activity

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import androidx.annotation.DrawableRes
import androidx.recyclerview.widget.RecyclerView
import hu.bme.aut.nagyhazi.R
import hu.bme.aut.nagyhazi.data.RecipeItem
import kotlinx.android.synthetic.main.recipe_row.view.*

class RecipeAdapter internal constructor(private val listener: MainActivity) :

    RecyclerView.Adapter<RecipeAdapter.FoodViewHolder>() {
    private var recipes: MutableList<RecipeItem>
    private val comp = Comparator { a : RecipeItem, b: RecipeItem->
        a.name.compareTo(b.name)
    }

    interface OnFoodSelectedListener {
        fun onRecipeSelected(recipe: RecipeItem?)
        fun onRecipeRemoved(recipe: RecipeItem?)
        fun onRecipeChanged(recipe: RecipeItem?)
    }
    fun restoreObjects(newFoods: MutableList<RecipeItem>) {
        this.recipes = newFoods
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FoodViewHolder {
        val view: View =
            LayoutInflater.from(parent.context).inflate(R.layout.recipe_row, parent, false)
        return FoodViewHolder(view)
    }

    override fun onBindViewHolder(holder: FoodViewHolder, position: Int) {
        val item = recipes[position]
        holder.nameTextView.text = recipes[position].name
        holder.categoryTextView.text = recipes[position].category
        holder.checkBoxFav.isChecked = recipes[position].isFavourite
        holder.icon.setImageResource( getImageResource(recipes[position].category))
        holder.item = item
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
    override fun getItemCount(): Int {
        return recipes.size
    }

    fun addRecipe(newRedcipe: RecipeItem) {
        recipes.add(newRedcipe)
        recipes.sortWith(comp)
        notifyDataSetChanged()
    }
    fun removeAll(){
        for (recipe in recipes){
            listener?.onRecipeRemoved(recipe)
        }
        recipes.clear()
        notifyDataSetChanged()
    }
    fun getFavourites():MutableList<RecipeItem>{

        val list : MutableList<RecipeItem>  = ArrayList<RecipeItem>()
        for (i in recipes){
            if (i.isFavourite){
                list.add(i.copy())
            }
        }

        return list
    }
    fun removeRecipe(newRedcipe: RecipeItem){
        recipes.remove(newRedcipe)
        recipes.sortWith(comp)
        notifyDataSetChanged()
    }

    fun update(recipeList: List<RecipeItem>) {
        recipes.clear()
        recipes.addAll(recipeList)
        recipes.sortWith(comp)
        notifyDataSetChanged()
    }


    inner class FoodViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nameTextView = itemView.row_food_name
        val categoryTextView = itemView.row_food_category
        val removeButton = itemView.removeButton
        val checkBoxFav = itemView.checkBox
        val icon = itemView.food_icon
        var item: RecipeItem? = null
        var pos: Int? = null

        init {
            itemView.setOnClickListener { listener?.onRecipeSelected(item) }
            removeButton.setOnClickListener {
                item?.let { it1 -> removeRecipe(it1) }

                item?.let { it1 ->
                    listener.onRecipeRemoved(it1)
                }

                notifyDataSetChanged()
            }
            checkBoxFav.setOnCheckedChangeListener(CompoundButton.OnCheckedChangeListener { buttonView, isChecked ->
                item?.let {
                    val newItem = it.copy(
                        isFavourite = isChecked
                    )
                    recipes.remove(it)
                    item = newItem
                    recipes.add(newItem)
                    listener?.onRecipeChanged(newItem)
                }

            })
        }
    }
    init {
        recipes = ArrayList()
    }
}