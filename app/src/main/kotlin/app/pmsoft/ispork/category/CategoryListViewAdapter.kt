package app.pmsoft.ispork.category

import android.view.LayoutInflater
import android.view.ViewGroup
import app.pmsoft.ispork.AbstractListViewAdapter
import app.pmsoft.ispork.R
import app.pmsoft.ispork.SelectionHandler
import app.pmsoft.ispork.data.Category

class CategoryListViewAdapter(private val selectionHandler: SelectionHandler<Category>) : AbstractListViewAdapter<Category, CategoryListViewHolder>() {
  override fun onCreateViewHolder(
    parent: ViewGroup,
    viewType: Int
  ): CategoryListViewHolder {
    val view = LayoutInflater.from(parent.context).inflate(
      R.layout.list_item_category,
      parent,
      false
    )
    return CategoryListViewHolder(
      view,
      selectionHandler
    )
  }
}
