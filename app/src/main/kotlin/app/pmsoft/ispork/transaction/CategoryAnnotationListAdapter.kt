package app.pmsoft.ispork.transaction

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import app.pmsoft.ispork.R
import app.pmsoft.ispork.data.Category
import app.pmsoft.ispork.view.AmountInputView

class CategoryAnnotationListAdapter(
  private val annotationHandler: CategoryAnnotationListHandler
) : RecyclerView.Adapter<CategoryAnnotationListAdapter.ViewHolder>() {

  private var dataSet: List<CategoryAnnotationEditWrapper> = emptyList()

  class ViewHolder(
    view: View,
    private val annotationHandler: CategoryAnnotationListHandler
  ) : RecyclerView.ViewHolder(view) {
    private val categoryField: TextView = view.findViewById(R.id.category_annotation_edit_category_field)
    private val amountField: AmountInputView = view.findViewById(R.id.category_annotation_edit_amount_field)
    private val notesField: EditText = view.findViewById(R.id.category_annotation_edit_notes_field)
    private val deleteIcon: ImageView = view.findViewById(R.id.category_annotation_edit_delete_icon)
    private val addIcon: ImageView = view.findViewById(R.id.category_annotation_edit_add_icon)

    private lateinit var data: CategoryAnnotationEditWrapper
    private val categoryObserver: Observer<Category?> = Observer {
      categoryField.text = it?.name
    }
    private var last: Boolean = false

    init {
      categoryField.setOnClickListener {
        annotationHandler.selectCategoryFor(data)
      }
      deleteIcon.setOnClickListener {
        data.delete()
      }
      addIcon.setOnClickListener {
        data.addSibling()
      }
      notesField.onFocusChangeListener = View.OnFocusChangeListener { _, hasFocus ->
        if (!hasFocus) {
          data.notes = notesField.text.toString().trim().takeIf { it.isNotBlank() }
        }
      }
    }

    fun setData(
      annotation: CategoryAnnotationEditWrapper,
      last: Boolean
    ) {
      if (::data.isInitialized) {
        data.categoryData.removeObserver(categoryObserver)
      }
      this.data = annotation
      this.last = last
      this.data.categoryData.observeForever(categoryObserver)
      amountField.amountData = data.amountData
      amountField.suggestedAmountData = data.suggestedAmountData
      updateViewFromData()
    }

    private fun updateViewFromData() {
      if (data.category != null) {
        categoryField.text = data.category!!.name
      } else {
        categoryField.text = itemView.context.resources.getString(R.string.pick_category)
      }
      amountField.suggestedAmount = data.suggestedAmount
      amountField.amount = data.amount
      amountField.positiveFlowString = itemView.context.resources.getString(
        R.string.payment_to,
        data.participant.name
      )
      amountField.negativeFlowString = itemView.context.resources.getString(
        R.string.payment_from,
        data.participant.name
      )
      notesField.text.clear()
      if (data.notes != null) {
        notesField.text.insert(
          0,
          data.notes
        )
      }
      addIcon.visibility = if (last) View.VISIBLE else View.GONE
    }

    fun persistInputs() {
      data.amount = amountField.amount ?: amountField.suggestedAmount ?: 0L
      data.notes = notesField.text.toString().trim().takeIf { it.isNotBlank() }
    }
  }

  override fun getItemCount(): Int = dataSet.size

  override fun onCreateViewHolder(
    parent: ViewGroup,
    viewType: Int
  ): ViewHolder {
    return ViewHolder(
      LayoutInflater.from(parent.context).inflate(
        R.layout.category_annotation_edit_fragment,
        parent,
        false
      ),
      annotationHandler
    )
  }

  override fun onBindViewHolder(
    viewHolder: ViewHolder,
    position: Int
  ) {
    viewHolder.setData(
      dataSet[position],
      position == dataSet.lastIndex
    )
  }

  fun setData(newData: List<CategoryAnnotationEditWrapper>) {
    dataSet = newData
    notifyDataSetChanged()
  }
}
