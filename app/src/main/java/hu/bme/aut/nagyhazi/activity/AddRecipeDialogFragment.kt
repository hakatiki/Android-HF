package hu.bme.aut.nagyhazi.activity

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Spinner
import androidx.annotation.Nullable
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatDialogFragment
import hu.bme.aut.nagyhazi.R
import hu.bme.aut.nagyhazi.data.RecipeItem

class AddRecipeDialogFragment : AppCompatDialogFragment() {
    private lateinit var listener: AddRecipeDialogListener
    private lateinit var editTextName: EditText
    private lateinit var categorySpinnerField: Spinner
    private lateinit var editTextRecipe: EditText

    interface AddRecipeDialogListener {
        fun onRecipeAdded(recipe: RecipeItem)
    }

    override fun onCreate(@Nullable savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        listener = if (activity is AddRecipeDialogListener) {
            (activity as AddRecipeDialogListener?)!!
        } else {
            throw RuntimeException("Activity must implement AddRecipeDialogListener interface!")
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return AlertDialog.Builder(requireContext())
            .setTitle(getString(R.string.new_recipe))
            .setView(contentView)
            .setPositiveButton(getString(R.string.ok)) { _, _ ->
                categorySpinnerField?.selectedItemPosition?.let {
                    IntToCategory(
                        it
                    )
                }?.let {
                    RecipeItem(
                        id = null,
                        name = editTextName?.text.toString(),
                        estimatedPrice = 0,
                        recipe = editTextRecipe?.text.toString(),
                        category = it,
                        isFavourite = false
                    )
                }?.let {
                    listener?.onRecipeAdded(
                        it

                    )
                }
            }
            .setNegativeButton(R.string.cancel, null)
            .create()
    }

    private val contentView: View
        get() {
            val view: View = LayoutInflater.from(context).inflate(R.layout.dialog_new_recipe, null)
            editTextName = view.findViewById(R.id.NewRecipeDialogEditText)
            categorySpinnerField = view.findViewById(R.id.CategorySpinner)
            categorySpinnerField.setAdapter(
                ArrayAdapter(
                    requireContext(),
                    android.R.layout.simple_spinner_dropdown_item,
                    resources.getStringArray(R.array.category_items)
                )
            )
            editTextRecipe = view.findViewById(R.id.RecipeEditText)
            return view
        }
    private fun IntToCategory(num: Int) :String{
        return resources.getStringArray(R.array.category_items)[num]
    }
}