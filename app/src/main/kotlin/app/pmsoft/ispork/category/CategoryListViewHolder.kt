package app.pmsoft.ispork.category

import android.view.View
import android.widget.FrameLayout
import android.widget.TextView
import app.pmsoft.ispork.AbstractViewHolder
import app.pmsoft.ispork.R
import app.pmsoft.ispork.SelectionHandler
import app.pmsoft.ispork.data.Category

class CategoryListViewHolder(
  view: View,
  selectionHandler: SelectionHandler<Category>
) : AbstractViewHolder<Category>(
  view,
  selectionHandler
) {
  private val nameLabel: TextView = view.findViewById(R.id.category_list_item_name_label)
  private val layout: FrameLayout = view.findViewById(R.id.category_list_item_layout)

  override val backgroundView: View
    get() = layout

  override fun updateViewFromData(e: Category) {
    nameLabel.text = e.name
  }
}
