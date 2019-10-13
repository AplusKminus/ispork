package app.pmsoft.ispork.category

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import app.pmsoft.ispork.R
import app.pmsoft.ispork.data.Category

class CategoryPickingViewAdapter(val activity: CategoryPickingActivity) :
  RecyclerView.Adapter<CategoryPickingViewAdapter.ViewHolder>(),
  Observer<List<Category>> {

  private var dataSet: List<Category> = emptyList()

  inner class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {
    private val itemTitleView: TextView = v.findViewById(R.id.category_list_item_name_label)

    private lateinit var data: Category

    init {
      itemView.setOnClickListener {
        activity.intent.putExtra(
          "category",
          data
        )
        activity.setResult(
          Activity.RESULT_OK,
          activity.intent
        )
        activity.finish()
      }
    }

    fun setData(category: Category) {
      this.data = category
      updateViewFromData()
    }

    private fun updateViewFromData() {
      itemTitleView.text = data.name
    }
  }

  override fun onCreateViewHolder(
    parent: ViewGroup,
    viewType: Int
  ): ViewHolder {
    val v = LayoutInflater.from(parent.context).inflate(
      R.layout.list_item_category,
      parent,
      false
    )
    return ViewHolder(v)
  }

  override fun onBindViewHolder(
    viewHolder: ViewHolder,
    position: Int
  ) {
    viewHolder.setData(dataSet[position])
  }

  override fun getItemCount(): Int = dataSet.size

  override fun onChanged(t: List<Category>?) {
    dataSet = t ?: emptyList()
    notifyDataSetChanged()
  }
}
