package app.pmsoft.ispork.saving_goal

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import app.pmsoft.ispork.R
import app.pmsoft.ispork.RequestCodes
import app.pmsoft.ispork.category.CategoryPickingActivity
import app.pmsoft.ispork.data.Category
import app.pmsoft.ispork.data.FullSavingGoal

class SavingGoalEditActivity : AppCompatActivity() {

  private lateinit var nameField: EditText
  private lateinit var notesField: EditText
  private lateinit var categoryField: EditText
  private lateinit var savingGoal: FullSavingGoal

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_saving_goal_edit)
    nameField = findViewById(R.id.saving_goal_edit_name_field)
    notesField = findViewById(R.id.saving_goal_edit_notes_field)
    categoryField = findViewById(R.id.saving_goal_edit_category_field)
    title = intent.getStringExtra("title")

    when (intent.getIntExtra(
      "request_code",
      0
    )) {
      RequestCodes.SAVING_GOAL_CREATION_REQUEST_CODE -> savingGoal = FullSavingGoal()
      RequestCodes.SAVING_GOAL_EDITING_REQUEST_CODE -> savingGoal = intent.getParcelableExtra("savingGoal")
    }
    updateViewFromData()
  }

  fun updateViewFromData() {
    nameField.text.clear()
    nameField.text.insert(
      0,
      savingGoal.name
    )
    notesField.text.clear()
    notesField.text.insert(
      0,
      savingGoal.notes ?: ""
    )
    categoryField.text.clear()
    categoryField.text.insert(
      0,
      savingGoal.category?.name ?: ""
    )
  }

  @SuppressWarnings("UNUSED_PARAMETER")
  fun pickCategory(view: View) {
    val intent = Intent(
      this,
      CategoryPickingActivity::class.java
    )
    this.startActivityForResult(
      intent,
      RequestCodes.CATEGORY_SELECTION_REQUEST_CODE
    )
  }

  override fun onActivityResult(
    requestCode: Int,
    resultCode: Int,
    intent: Intent?
  ) {
    super.onActivityResult(
      requestCode,
      resultCode,
      intent
    )
    if (intent != null) {
      when (requestCode) {
        RequestCodes.CATEGORY_SELECTION_REQUEST_CODE -> {
          val category = intent.getParcelableExtra<Category>("category")
          savingGoal.category = category
          updateViewFromData()
        }
      }
    }
  }

  override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
    R.id.action_save_saving_goal -> {
      savingGoal.name = nameField.text.toString().trim()
      savingGoal.notes = notesField.text.toString().trim()
      intent.putExtra(
        "savingGoal",
        savingGoal
      )
      setResult(
        AppCompatActivity.RESULT_OK,
        intent
      )
      finish()
      true
    }
    else -> {
      super.onOptionsItemSelected(item)
    }
  }

  override fun onCreateOptionsMenu(menu: Menu?): Boolean {
    menuInflater.inflate(
      R.menu.saving_goal_edit_menu,
      menu
    )
    return true
  }
}
