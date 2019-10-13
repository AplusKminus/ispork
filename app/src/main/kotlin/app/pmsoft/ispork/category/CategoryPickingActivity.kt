package app.pmsoft.ispork.category

import android.os.Bundle
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import app.pmsoft.ispork.R
import app.pmsoft.ispork.util.TextWatcherAdapter

class CategoryPickingActivity : AppCompatActivity() {

  private lateinit var viewModel: CategoryListViewModel
  private lateinit var viewManager: RecyclerView.LayoutManager
  private lateinit var viewAdapter: CategoryPickingViewAdapter

  private lateinit var recyclerView: RecyclerView
  private lateinit var searchField: EditText

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_category_picking)

    viewManager = LinearLayoutManager(this)

    viewModel = ViewModelProvider(this).get(CategoryListViewModel::class.java)
    viewAdapter = CategoryPickingViewAdapter(this)
    viewModel.categories.observe(
      this,
      viewAdapter
    )

    recyclerView = findViewById<RecyclerView>(R.id.category_picking_recycler_view).apply {
      setHasFixedSize(true)
      layoutManager = viewManager
      adapter = viewAdapter
    }

    searchField = findViewById(R.id.category_picking_search_field)
    searchField.addTextChangedListener(object : TextWatcherAdapter() {
      override fun onTextChanged(
        s: CharSequence?,
        start: Int,
        before: Int,
        count: Int
      ) {
        viewModel.filterString = s?.toString() ?: ""
      }
    })
  }
}
