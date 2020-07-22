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
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import app.pmsoft.ispork.R
import app.pmsoft.ispork.RequestCodes
import app.pmsoft.ispork.category.CategoryPickingActivity
import app.pmsoft.ispork.data.*
import app.pmsoft.ispork.participant.ParticipantPickingActivity
import app.pmsoft.ispork.util.DateHandler
import app.pmsoft.ispork.util.LocaleHandler
import app.pmsoft.ispork.view.DatePickerFragment
import java.util.*

class TransactionEditActivity : AppCompatActivity(),
  DatePickerDialog.OnDateSetListener,
  SubTransactionListHandler {

  private lateinit var appDatabase: AppDatabase

  private lateinit var detailsAdapter: SubTransactionsListAdapter
  private lateinit var detailsViewManager: RecyclerView.LayoutManager

  private lateinit var entryDateView: TextView
  private lateinit var detailsView: RecyclerView
  private lateinit var participantsButton: Button

  private lateinit var data: TransactionEditWrapper

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    appDatabase = provideDatabase(this.applicationContext)
    setContentView(R.layout.activity_transaction_edit)

    val requestCode = intent.getIntExtra(
      "request_code",
      1
    )
    when (requestCode) {
      RequestCodes.TRANSACTION_CREATION_REQUEST_CODE -> {
        data = TransactionEditWrapper(FullTransactionDefinition())
      }
      RequestCodes.TRANSACTION_EDITING_REQUEST_CODE -> {
        data = TransactionEditWrapper(intent.getParcelableExtra("transaction"))
      }
    }
    LocaleHandler.locale = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
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
          val participants = intent.getParcelableArrayListExtra<Participant>("participants")
            .sortedBy { it.type.internal }
          val currency = Currency.getInstance(LocaleHandler.locale)
          val moneyBags = participants.map {
            appDatabase.subTransactionDao()
              .getMoneyBagFor(it.id, currency) ?: OwnedMoneyBag(currency, it)
          }
          data.createSubTransactionsFor(moneyBags)
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
          val budgetPot = intent.getParcelableExtra<FullBudgetPot>("budgetPot")
          val budgetPotAnnotation: BudgetPotAnnotationEditWrapper
          val subTransaction = this.data.subTransactions[subTransactionIndex]
          if (categoryAnnotationIndex < 0) {
            budgetPotAnnotation = BudgetPotAnnotationEditWrapper(
              subTransaction,
              FullBudgetPotAnnotation()
            )
            subTransaction.budgetPotAnnotations = listOf(budgetPotAnnotation)
          } else {
            budgetPotAnnotation = subTransaction.budgetPotAnnotations[categoryAnnotationIndex]
          }
          budgetPotAnnotation.budgetPot = budgetPot
        }
      }
    }
  }

  @Suppress("UNUSED_PARAMETER")
  fun pickEntryDate(view: View) {
    showDateSelector(
      data.entryDate ?: Date(),
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
