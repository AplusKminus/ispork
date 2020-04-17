package app.pmsoft.ispork.transaction

import android.text.SpannableStringBuilder
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import app.pmsoft.ispork.R
import app.pmsoft.ispork.data.FullBudgetPot
import app.pmsoft.ispork.view.AmountInputView

class BudgetPotAnnotationListAdapter(
  private val annotationHandler: BudgetPotAnnotationListHandler
) : RecyclerView.Adapter<BudgetPotAnnotationListAdapter.ViewHolder>() {

  private var dataSet: List<BudgetPotAnnotationEditWrapper> = emptyList()

  class ViewHolder(
    view: View,
    private val annotationHandler: BudgetPotAnnotationListHandler
  ) : RecyclerView.ViewHolder(view) {
    private val budgetPotField: EditText = view.findViewById(R.id.budgetPot_annotation_edit_budgetPot_field)
    private val amountField: AmountInputView = view.findViewById(R.id.budgetPot_annotation_edit_amount_field)
    private val notesField: EditText = view.findViewById(R.id.budgetPot_annotation_edit_notes_field)
    private val deleteIcon: ImageView = view.findViewById(R.id.budgetPot_annotation_edit_delete_icon)
    private val addIcon: ImageView = view.findViewById(R.id.budgetPot_annotation_edit_add_icon)

    private lateinit var data: BudgetPotAnnotationEditWrapper
    private val budgetPotObserver: Observer<FullBudgetPot?> = Observer { budgetPot ->
      budgetPotField.text = budgetPot?.category?.name?.let { SpannableStringBuilder(it) }
    }
    private var last: Boolean = false

    init {
      budgetPotField.setOnClickListener {
        annotationHandler.selectBudgetPotFor(data)
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
      annotation: BudgetPotAnnotationEditWrapper,
      last: Boolean
    ) {
      if (::data.isInitialized) {
        data.budgetPotData.removeObserver(budgetPotObserver)
      }
      this.data = annotation
      this.last = last
      this.data.budgetPotData.observeForever(budgetPotObserver)
      amountField.amountData = data.amountData
      amountField.suggestedAmountData = data.suggestedAmountData
      updateViewFromData()
    }

    private fun updateViewFromData() {
      if (data.budgetPot != null) {
        budgetPotField.text = SpannableStringBuilder(constructName(data.budgetPot!!))
      } else {
        budgetPotField.text = null
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

    private fun constructName(fullBudgetPot: FullBudgetPot): String? {
      return fullBudgetPot.category?.name
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
        R.layout.budget_pot_annotation_edit_fragment,
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

  fun setData(newData: List<BudgetPotAnnotationEditWrapper>) {
    dataSet = newData
    notifyDataSetChanged()
  }
}
