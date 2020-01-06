package app.pmsoft.ispork.transaction

import android.app.DatePickerDialog
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import app.pmsoft.ispork.R
import app.pmsoft.ispork.data.Category
import app.pmsoft.ispork.data.Participant
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
    CategoryAnnotationListHandler {

    private val participantField: TextView = view.findViewById(R.id.sub_transaction_participant_field)
    private val dateField: TextView = view.findViewById(R.id.sub_transaction_date_field)
    private val amountField: AmountInputView = view.findViewById(R.id.sub_transaction_amount_field)
    private val notesField: EditText = view.findViewById(R.id.sub_transaction_notes_field)
    private val dateSwitch: Switch = view.findViewById(R.id.sub_transaction_booking_date_switch)
    private val categoryLabel: TextView = view.findViewById(R.id.sub_transaction_category_label)
    private val categoryField: TextView = view.findViewById(R.id.sub_transaction_category_field)
    private val splitButton: Button = view.findViewById(R.id.sub_transaction_category_split_button)
    private val participantTypeIcon: ImageView = view.findViewById(R.id.sub_transaction_participant_type_icon)

    private val annotationsAdapter: CategoryAnnotationListAdapter = CategoryAnnotationListAdapter(this)
    private val annotationsView: RecyclerView = view.findViewById<RecyclerView>(R.id.sub_transaction_category_list_view).also {
      it.layoutManager = LinearLayoutManager(view.context)
      it.adapter = annotationsAdapter
    }
    private lateinit var data: SubTransactionEditWrapper
    private val categoryAnnotationsObserver: Observer<List<CategoryAnnotationEditWrapper>> = Observer {
      annotationsAdapter.setData(it)
      if (it.size == 1) {
        it[0].categoryData.observeForever(firstCategoryObserver)
      } else if (it.isNotEmpty()) {
        it[0].categoryData.removeObserver(firstCategoryObserver)
      }
      updateViewFromData()
    }
    private val firstCategoryObserver: Observer<Category?> = Observer {
      categoryField.text = it?.name ?: itemView.context.resources.getString(R.string.pick_category)
    }

    init {
      notesField.onFocusChangeListener = View.OnFocusChangeListener { _, hasFocus ->
        if (!hasFocus) {
          data.notes = notesField.text.toString().trim().takeIf { it.isNotBlank() }
        }
      }
      splitButton.setOnClickListener {
        while (data.categoryAnnotations.size < 2) {
          data.addNewCategoryAnnotation()
        }
        updateViewFromData()
      }
      categoryField.setOnClickListener {
        startCategoryPicking(null)
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
        data.categoryAnnotationsData.removeObserver(categoryAnnotationsObserver)
      }
      this.data = subTransaction
      annotationsAdapter.setData(subTransaction.categoryAnnotations)
      this.data.categoryAnnotationsData.observeForever(categoryAnnotationsObserver)
      if (data.categoryAnnotations.size == 1) {
        data.categoryAnnotations[0].categoryData.observeForever(firstCategoryObserver)
      }
      amountField.amountData = data.amountData
      amountField.suggestedAmountData = data.suggestedAmountData
      updateViewFromData()
    }

    private fun updateViewFromData() {
      if (data.participant.type.internal) {
        // this branch is for accounts and persons
        amountField.positiveFlowString = itemView.context.resources.getString(R.string.inflow)
        amountField.negativeFlowString = itemView.context.resources.getString(R.string.outflow)
      } else {
        // this branch is for external participants
        amountField.positiveFlowString = itemView.context.resources.getString(
          R.string.payment_to,
          data.participant.name
        )
        amountField.negativeFlowString = itemView.context.resources.getString(
          R.string.payment_from,
          data.participant.name
        )
      }
      participantTypeIcon.setImageDrawable(itemView.context.resources.getDrawable(
        when (data.participant.type) {
          Participant.Type.ACCOUNT -> R.drawable.ic_account_white_24dp
          Participant.Type.PERSON -> R.drawable.ic_person_white_24dp
          Participant.Type.PAYEE -> R.drawable.ic_payee_white_24dp
        },
        itemView.context.theme
      ))
      notesField.text.clear()
      if (data.notes != null) {
        notesField.text.insert(
          0,
          data.notes
        )
      }
      participantField.text = data.participant.name
      dateField.text = DateHandler.format(data.bookingDate)
      dateSwitch.isChecked = data.bookingDate != null
      when {
        data.participant.type.internal -> {
          categoryLabel.visibility = View.GONE
          categoryField.visibility = View.GONE
          splitButton.visibility = View.GONE
          annotationsView.visibility = View.GONE
        }
        data.categoryAnnotations.size <= 1 -> {
          categoryLabel.visibility = View.VISIBLE
          categoryField.visibility = View.VISIBLE
          val pickACategoryString = itemView.context.resources.getString(R.string.pick_category)
          if (data.categoryAnnotations.size == 1) {
            categoryField.text = data.categoryAnnotations[0].category?.name ?: pickACategoryString
          } else {
            categoryField.text = pickACategoryString
          }
          splitButton.visibility = View.VISIBLE
          annotationsView.visibility = View.GONE
        }
        else -> {
          categoryLabel.visibility = View.GONE
          categoryField.visibility = View.GONE
          splitButton.visibility = View.GONE
          annotationsView.visibility = View.VISIBLE
        }
      }
    }

    override fun selectCategoryFor(categoryAnnotationEditWrapper: CategoryAnnotationEditWrapper) {
      startCategoryPicking(categoryAnnotationEditWrapper)
    }

    private fun startCategoryPicking(categoryAnnotationEditWrapper: CategoryAnnotationEditWrapper?) {
      subTransactionListHandler.startCategoryPicking(
        data,
        categoryAnnotationEditWrapper?.let { data.categoryAnnotations.indexOf(it) }
      )
    }

    fun persistInputs() {
      data.notes = notesField.text.toString().trim().takeIf { it.isNotBlank() }
      for (index in data.categoryAnnotations.indices) {
        (annotationsView.findViewHolderForAdapterPosition(index) as? CategoryAnnotationListAdapter.ViewHolder)
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
