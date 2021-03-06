package app.pmsoft.ispork.transaction

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.DatePicker
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import app.pmsoft.ispork.R
import app.pmsoft.ispork.RequestCodes
import app.pmsoft.ispork.category.CategoryPickingActivity
import app.pmsoft.ispork.data.Category
import app.pmsoft.ispork.data.FullCategoryAnnotation
import app.pmsoft.ispork.data.FullTransaction
import app.pmsoft.ispork.data.Participant
import app.pmsoft.ispork.participant.ParticipantPickingActivity
import app.pmsoft.ispork.util.CurrencyHandler
import app.pmsoft.ispork.util.DateHandler
import app.pmsoft.ispork.view.DatePickerFragment
import java.util.*

class TransactionEditActivity : AppCompatActivity(),
  DatePickerDialog.OnDateSetListener,
  SubTransactionListHandler {

  private lateinit var detailsAdapter: SubTransactionsListAdapter
  private lateinit var detailsViewManager: RecyclerView.LayoutManager

  private lateinit var notesField: EditText
  private lateinit var entryDateView: TextView
  private lateinit var detailsView: RecyclerView
  private lateinit var participantsButton: Button

  private lateinit var data: TransactionEditWrapper

  private var locale: Locale = Locale.getDefault()
    set(value) {
      field = value
      CurrencyHandler.locale = value
      DateHandler.locale = value
    }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_transaction_edit)

    val requestCode = intent.getIntExtra(
      "request_code",
      1
    )
    when (requestCode) {
      RequestCodes.TRANSACTION_CREATION_REQUEST_CODE -> {
        data = TransactionEditWrapper(FullTransaction())
      }
      RequestCodes.TRANSACTION_EDITING_REQUEST_CODE -> {
        data = TransactionEditWrapper(intent.getParcelableExtra("transaction"))
      }
    }
    locale = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
      resources.configuration.locales[0]
    } else {
      @Suppress("DEPRECATION")
      resources.configuration.locale
    }

    detailsViewManager = LinearLayoutManager(this)
    detailsAdapter = SubTransactionsListAdapter(this)
    data.subTransactionsData.observe(
      this,
      detailsAdapter
    )
    detailsView = findViewById(R.id.transaction_edit_sub_transactions_view)
    detailsView.setHasFixedSize(true)
    detailsView.layoutManager = detailsViewManager
    detailsView.adapter = detailsAdapter

    notesField = findViewById(R.id.transaction_edit_notes_field)

    entryDateView = findViewById(R.id.transaction_edit_date_field)

    participantsButton = findViewById(R.id.transaction_edit_pick_participants_button)

    updateViewFromData()

    title = intent.getStringExtra("title")
  }

  @Suppress("UNUSED_PARAMETER")
  fun pickParticipants(view: View) {
    val intent = Intent(
      this,
      ParticipantPickingActivity::class.java
    )
    startActivityForResult(
      intent,
      RequestCodes.TRANSACTION_PARTICIPANTS_SELECTION_REQUEST_CODE
    )
  }

  override fun onActivityResult(
    requestCode: Int,
    resultCode: Int,
    intent: Intent?
  ) {
    super.onActivityResult(
      requestCode,
      resultCode,
      intent
    )
    if (intent != null) {
      when (requestCode) {
        RequestCodes.TRANSACTION_PARTICIPANTS_SELECTION_REQUEST_CODE -> {
          val participants = intent.getParcelableArrayListExtra<Participant>("participants").sortedBy { it.type.internal }
          data.createSubTransactionsFor(participants)
        }
        RequestCodes.CATEGORY_SELECTION_REQUEST_CODE -> {
          val subTransactionIndex = intent.getIntExtra(
            "sub_transaction_index",
            -1
          )
          val categoryAnnotationIndex = intent.getIntExtra(
            "category_annotation_index",
            -1
          )
          val category = intent.getParcelableExtra<Category>("category")
          val categoryAnnotation: CategoryAnnotationEditWrapper
          val subTransaction = this.data.subTransactions[subTransactionIndex]
          if (categoryAnnotationIndex < 0) {
            categoryAnnotation = CategoryAnnotationEditWrapper(
              subTransaction,
              FullCategoryAnnotation()
            )
            subTransaction.categoryAnnotations = listOf(categoryAnnotation)
          } else {
            categoryAnnotation = subTransaction.categoryAnnotations[categoryAnnotationIndex]
          }
          categoryAnnotation.category = category
        }
      }
    }
  }

  @Suppress("UNUSED_PARAMETER")
  fun pickEntryDate(view: View) {
    showDateSelector(
      data.entryDate,
      this
    )
  }

  override fun onDateSet(
    view: DatePicker?,
    year: Int,
    month: Int,
    dayOfMonth: Int
  ) {
    data.entryDate = DateHandler.create(
      year,
      month,
      dayOfMonth
    )
    updateViewFromData()
  }

  private fun updateViewFromData() {
    val entryDateString = DateHandler.format(data.entryDate)
    entryDateView.text = entryDateString
    notesField.text.clear()
    if (data.notes != null) {
      notesField.text.insert(
        0,
        data.notes
      )
    }
  }

  override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
    R.id.action_save_transaction -> {
      returnResultToCaller()
      true
    }
    else -> {
      super.onOptionsItemSelected(item)
    }
  }

  private fun returnResultToCaller() {
    persistInputs()
    intent.putExtra(
      "transaction",
      data.extractTransaction()
    )
    setResult(
      RESULT_OK,
      intent
    )
    finish()
  }

  private fun persistInputs() {
    data.notes = notesField.text.toString().trim().takeIf { it.isNotEmpty() }
    for (index in data.subTransactions.indices) {
      (detailsView.findViewHolderForAdapterPosition(index) as? SubTransactionsListAdapter.ViewHolder)
        ?.persistInputs()
    }
  }

  override fun onCreateOptionsMenu(menu: Menu?): Boolean {
    menuInflater.inflate(
      R.menu.transaction_edit_menu,
      menu
    )
    return true
  }

  override fun startCategoryPicking(
    subTransactionEditWrapper: SubTransactionEditWrapper,
    categoryAnnotationIndex: Int?
  ) {
    val intent = Intent(
      this,
      CategoryPickingActivity::class.java
    )
    intent.putExtra(
      "sub_transaction_index",
      data.subTransactions.indexOf(subTransactionEditWrapper)
    )
    if (categoryAnnotationIndex != null) {
      intent.putExtra(
        "category_annotation_index",
        categoryAnnotationIndex
      )
    }
    this.startActivityForResult(
      intent,
      RequestCodes.CATEGORY_SELECTION_REQUEST_CODE
    )
  }

  override fun showDateSelector(
    startDate: Date,
    callback: DatePickerDialog.OnDateSetListener
  ) {
    val fragment = DatePickerFragment()
    fragment.setResultHandler(callback)
    fragment.setDate(startDate)
    fragment.show(
      supportFragmentManager,
      "transaction_edit_activity_date_picker"
    )
  }

  override fun onPause() {
    persistInputs()
    super.onPause()
  }
}
