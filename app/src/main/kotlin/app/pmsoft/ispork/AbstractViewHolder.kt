package app.pmsoft.ispork

import android.os.Build
import android.view.View
import androidx.recyclerview.widget.RecyclerView

abstract class AbstractViewHolder<E : Any>(
  view: View,
  private val selectionHandler: SelectionHandler<E>
) : RecyclerView.ViewHolder(view) {

  abstract val backgroundView: View

  private lateinit var data: E

  init {
    itemView.setOnClickListener {
      selectionHandler.onClick(data)
      updateBackgroundColor()
    }
    itemView.setOnLongClickListener {
      selectionHandler.onLongClick(data)
      updateBackgroundColor()
      true
    }
  }

  fun setData(e: E) {
    this.data = e
    updateViewFromData(e)
    updateBackgroundColor()
  }

  abstract fun updateViewFromData(e: E)

  private fun updateBackgroundColor() {
    if (selectionHandler.isSelected(data)) {
      backgroundView.background = itemView.context.getDrawable(R.drawable.list_item_selected_background)
    } else {
      backgroundView.background = itemView.context.getDrawable(R.drawable.list_item_background)
    }
  }

  protected fun getColor(id: Int): Int {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
      itemView.context.resources.getColor(
        id,
        itemView.context.theme
      )
    } else {
      @Suppress("DEPRECATION")
      itemView.context.resources.getColor(id)
    }
  }
}
