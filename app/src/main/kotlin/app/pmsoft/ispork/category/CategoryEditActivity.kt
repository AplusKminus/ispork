package app.pmsoft.ispork.category

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import app.pmsoft.ispork.R
import app.pmsoft.ispork.RequestCodes
import app.pmsoft.ispork.data.Category

class CategoryEditActivity : AppCompatActivity() {

  private lateinit var nameField: EditText
  private lateinit var category: Category

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_category_edit)
    nameField = findViewById(R.id.category_edit_name_field)
    title = intent.getStringExtra("title")

    when (intent.getIntExtra(
      "request_code",
      0
    )) {
      RequestCodes.CATEGORY_CREATION_REQUEST_CODE -> category = Category()
      RequestCodes.CATEGORY_EDITING_REQUEST_CODE -> category = intent.getParcelableExtra("category")
    }
    nameField.text.insert(
      0,
      category.name
    )
  }

  override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
    R.id.action_save_category -> {
      category.name = nameField.text.toString().trim()
      intent.putExtra(
        "category",
        category
      )
      setResult(
        RESULT_OK,
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
      R.menu.category_edit_menu,
      menu
    )
    return true
  }
}
