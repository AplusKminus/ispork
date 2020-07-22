package app.pmsoft.ispork.transaction

import android.app.DatePickerDialog
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Switch
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import app.pmsoft.ispork.R
import app.pmsoft.ispork.participant.ParticipantTypeIcon
import app.pmsoft.ispork.util.DateHandler
import app.pmsoft.ispork.view.AmountInputView
import java.util.*

class SubTransactionsListAdapter(
  private val subTransactionListHandler: SubTransactionListHandler
) : RecyclerView.Adapter<SubTransactionsListAdapter.ViewHolder>(),
  Observer<List<SubTransactionEditWrapper>> {

  private var dataSet: List<SubTransactionEditWrapper> = emptyList()

  class ViewHolder(
    view: View,
    private val subTransactionListHandler: SubTransactionListHandler
  ) : RecyclerView.ViewHolder(view),
    BudgetPotAnnotationListHandler {

    private val participantField: TextView = view.findViewById(R.id.sub_transaction_participant_field)
    private val dateField: TextView = view.findViewById(R.id.sub_transaction_date_field)
    private val amountField: AmountInputView = view.findViewById(R.id.sub_transaction_amount_field)
    private val notesField: EditText = view.findViewById(R.id.sub_transaction_notes_field)
    private val notesLayout: ViewGroup = view.findViewById(R.id.sub_transaction_notes_layout)
    private val dateSwitch: Switch = view.findViewById(R.id.sub_transaction_booking_date_switch)
    private val addSplitButton: Button = view.findViewById(R.id.sub_transaction_budget_pot_split_button)
    private val participantTypeIcon: ParticipantTypeIcon = view.findViewById(R.id.sub_transaction_participant_type_icon)

    private val annotationsAdapter: BudgetPotAnnotationListAdapter = BudgetPotAnnotationListAdapter(this)
    private val annotationsView: RecyclerView = view.findViewById<RecyclerView>(R.id.sub_transaction_budget_pot_list_view).also {
      it.layoutManager = LinearLayoutManager(view.context)
      it.adapter = annotationsAdapter
    }
    private lateinit var data: SubTransactionEditWrapper
    private val budgetPotAnnotationsObserver: Observer<List<BudgetPotAnnotationEditWrapper>> = Observer {
      annotationsAdapter.setData(it)
      updateViewFromData()
    }

    init {
      notesField.onFocusChangeListener = View.OnFocusChangeListener { _, hasFocus ->
        if (!hasFocus) {
          data.notes = notesField.text.toString().trim().takeIf { it.isNotBlank() }
        }
      }
      addSplitButton.setOnClickListener {
        data.addNewBudgetPotAnnotation()
        updateViewFromData()
      }
      dateSwitch.setOnCheckedChangeListener { _, isChecked ->
        data.bookingDate = if (isChecked) {
          Date()
        } else {
          null
        }
        dateField.text = DateHandler.format(data.bookingDate)
      }
      dateField.setOnClickListener {
        val date = data.bookingDate
        if (date != null) {
          subTransactionListHandler.showDateSelector(
            date,
            DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
              data.bookingDate = DateHandler.create(
                year,
                month,
                dayOfMonth
              )
              updateViewFromData()
            }
          )
        }
      }
    }

    fun setData(
      subTransaction: SubTransactionEditWrapper
    ) {
      if (::data.isInitialized) {
        data.budgetPotAnnotationsData.removeObserver(budgetPotAnnotationsObserver)
      }
      this.data = subTransaction
      annotationsAdapter.setData(subTransaction.budgetPotAnnotations)
      this.data.budgetPotAnnotationsData.observeForever(budgetPotAnnotationsObserver)
      amountField.amountData = data.amountData
      amountField.suggestedAmountData = data.suggestedAmountData
      updateViewFromData()
    }

    private fun updateViewFromData() {
      if (data.moneyBag.participant.type.internal) {
        // this branch is for accounts and persons
        amountField.positiveFlowString = itemView.context.resources.getString(R.string.inflow)
        amountField.negativeFlowString = itemView.context.resources.getString(R.string.outflow)
      } else {
        // this branch is for external participants
        amountField.positiveFlowString = itemView.context.resources.getString(
          R.string.payment_to,
          data.moneyBag.participant.name
        )
        amountField.negativeFlowString = itemView.context.resources.getString(
          R.string.payment_from,
          data.moneyBag.participant.name
        )
      }
      participantTypeIcon.setType(data.moneyBag.participant.type)
      notesField.text.clear()
      if (data.notes != null) {
        notesField.text.insert(
          0,
          data.notes
        )
      }
      participantField.text = data.moneyBag.participant.name
      dateField.text = DateHandler.format(data.bookingDate)
      dateSwitch.isChecked = data.bookingDate != null
      if (data.moneyBag.participant.type.internal) {
        notesLayout.visibility = View.VISIBLE
        annotationsView.visibility = View.GONE
        addSplitButton.visibility = View.GONE
      } else {
        notesLayout.visibility = View.GONE
        annotationsView.visibility = View.VISIBLE
        addSplitButton.visibility = View.VISIBLE
      }
    }

    override fun selectBudgetPotFor(budgetPotAnnotationEditWrapper: BudgetPotAnnotationEditWrapper) {
      startCategoryPicking(budgetPotAnnotationEditWrapper)
    }

    private fun startCategoryPicking(budgetPotAnnotationEditWrapper: BudgetPotAnnotationEditWrapper?) {
      subTransactionListHandler.startCategoryPicking(
        data,
        budgetPotAnnotationEditWrapper?.let { data.budgetPotAnnotations.indexOf(it) }
      )
    }

    fun persistInputs() {
      if (data.moneyBag.participant.type.internal) {
        data.notes = notesField.text.toString()
          .trim()
          .takeIf { it.isNotBlank() }
      } else {
        data.notes = data.budgetPotAnnotations.mapNotNull { it.notes }
          .joinToString(", ")
      }
      for (index in data.budgetPotAnnotations.indices) {
        (annotationsView.findViewHolderForAdapterPosition(index) as? BudgetPotAnnotationListAdapter.ViewHolder)
          ?.persistInputs()
      }
    }
  }

  override fun getItemCount(): Int = dataSet.size

  override fun onCreateViewHolder(
    parent: ViewGroup,
    viewType: Int
  ): ViewHolder {
    return ViewHolder(
      LayoutInflater.from(parent.context).inflate(
        R.layout.sub_transaction_edit_fragment,
        parent,
        false
      ),
      subTransactionListHandler
    )
  }

  override fun onBindViewHolder(
    viewHolder: ViewHolder,
    position: Int
  ) {
    viewHolder.setData(
      dataSet[position]
    )
  }

  override fun onChanged(newData: List<SubTransactionEditWrapper>) {
    dataSet = newData
    notifyDataSetChanged()
  }
}
