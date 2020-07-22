package app.pmsoft.ispork.buffer

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.RadioButton
import androidx.appcompat.app.AppCompatActivity
import app.pmsoft.ispork.R
import app.pmsoft.ispork.RequestCodes
import app.pmsoft.ispork.category.CategoryPickingActivity
import app.pmsoft.ispork.data.FullBudgetPot
import app.pmsoft.ispork.data.FullSpendingBuffer
import app.pmsoft.ispork.data.Interval
import app.pmsoft.ispork.util.LocaleHandler
import app.pmsoft.ispork.util.TextWatcherAdapter
import app.pmsoft.ispork.view.AmountInputView
import app.pmsoft.ispork.view.IntervalUnitField
import java.util.*

class BufferEditActivity : AppCompatActivity() {

  private lateinit var categoryField: EditText
  private lateinit var needButton: RadioButton
  private lateinit var wantButton: RadioButton
  private lateinit var priorityNumberLayout: LinearLayout
  private lateinit var priorityNumberField: EditText
  private lateinit var minField: AmountInputView
  private lateinit var maxField: AmountInputView
  private lateinit var rateField: AmountInputView
  private lateinit var intervalLengthField: EditText
  private lateinit var intervalUnitField: IntervalUnitField
  private lateinit var notesField: EditText

  private lateinit var spendingBuffer: FullSpendingBuffer
  private lateinit var budgetPot: FullBudgetPot

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_spending_buffer_edit)
    categoryField = findViewById(R.id.spending_buffer_category_field)
    priorityNumberLayout = findViewById(R.id.spending_buffer_priority_layout)
    priorityNumberField = findViewById(R.id.spending_buffer_priority_number_field)
    needButton = findViewById(R.id.spending_buffer_priority_need_button)
    wantButton = findViewById(R.id.spending_buffer_priority_want_button)
    minField = findViewById(R.id.spending_buffer_min_field)
    minField.allowNegative = false
    minField.positiveFlowString = getString(R.string.ok)
    maxField = findViewById(R.id.spending_buffer_max_field)
    maxField.allowNegative = false
    maxField.positiveFlowString = getString(R.string.ok)
    rateField = findViewById(R.id.spending_buffer_rate_field)
    rateField.allowNegative = false
    rateField.positiveFlowString = getString(R.string.ok)
    intervalLengthField = findViewById(R.id.spending_buffer_n_field)
    intervalUnitField = findViewById(R.id.spending_buffer_unit_field)
    notesField = findViewById(R.id.spending_buffer_edit_notes_field)

    LocaleHandler.locale = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
      resources.configuration.locales[0]
    } else {
      @Suppress("DEPRECATION")
      resources.configuration.locale
    }

    title = intent.getStringExtra("title")
    when (intent.getIntExtra("request_code", RequestCodes.SPENDING_BUFFER_CREATION_REQUEST_CODE)) {
      RequestCodes.SPENDING_BUFFER_CREATION_REQUEST_CODE -> spendingBuffer = FullSpendingBuffer()
      RequestCodes.SPENDING_BUFFER_EDITING_REQUEST_CODE -> spendingBuffer = intent.getParcelableExtra("spending_buffer")
    }
    if (spendingBuffer.budgetPot == null) {
      spendingBuffer.budgetPot = FullBudgetPot(Currency.getInstance(LocaleHandler.locale))
    }
    budgetPot = spendingBuffer.budgetPot!!
    if (spendingBuffer.interval.unit == null) {
      spendingBuffer.interval.unit = Interval.Unit.MONTH
    }

    updateViewFromData()
    minField.setOnAmountChangedListener {
      spendingBuffer.min = it ?: 0
    }
    maxField.setOnAmountChangedListener {
      spendingBuffer.max = it ?: 0
    }
    rateField.setOnAmountChangedListener {
      spendingBuffer.rate = it ?: 0
    }
    intervalLengthField.addTextChangedListener(object : TextWatcherAdapter() {
      override fun afterTextChanged(s: Editable?) {
        spendingBuffer.interval.length = s.toString().toIntOrNull() ?: 0
        intervalUnitField.quantity = spendingBuffer.interval.length
      }
    })
    wantButton.setOnCheckedChangeListener { _, isChecked ->
      priorityNumberLayout.visibility = if (isChecked) {
        VISIBLE
      } else {
        INVISIBLE
      }
    }
  }

  private fun updateViewFromData() {
    categoryField.text.clear()
    categoryField.text.insert(
      0,
      budgetPot.category?.name ?: ""
    )
    if (budgetPot.priority == 0) {
      needButton.isChecked = true
      priorityNumberLayout.visibility = INVISIBLE
    } else {
      wantButton.isChecked = true
      priorityNumberLayout.visibility = VISIBLE
      priorityNumberField.text.clear()
      priorityNumberField.text.insert(
        0,
        budgetPot.priority.toString()
      )
    }
    minField.amount = spendingBuffer.min
    maxField.amount = spendingBuffer.max
    rateField.amount = spendingBuffer.rate
    intervalLengthField.text.clear()
    intervalLengthField.text.insert(
      0,
      spendingBuffer.interval.length.toString()
    )
    intervalUnitField.unit = spendingBuffer.interval.unit ?: Interval.Unit.MONTH
    intervalUnitField.quantity = spendingBuffer.interval.length
    notesField.text.clear()
    notesField.text.insert(
      0,
      spendingBuffer.notes ?: ""
    )
  }

  override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
    R.id.action_save_spending_buffer -> {
      updateDataFromView()
      intent.putExtra("spending_buffer", spendingBuffer)
      setResult(RESULT_OK, intent)
      finish()
      true
    }
    else -> {
      super.onOptionsItemSelected(item)
    }
  }

  private fun updateDataFromView() {
    spendingBuffer.interval.unit = intervalUnitField.unit
    spendingBuffer.interval.length = intervalLengthField.text.toString().toInt()
    spendingBuffer.notes = notesField.text.toString()
  }

  override fun onCreateOptionsMenu(menu: Menu?): Boolean {
    menuInflater.inflate(R.menu.spending_buffer_edit_menu, menu)
    return true
  }

  fun selectCategory(view: View) {
    val intent = Intent(this, CategoryPickingActivity::class.java)
    startActivityForResult(
      intent,
      RequestCodes.CATEGORY_SELECTION_REQUEST_CODE
    )
  }

  override fun onActivityResult(
    requestCode: Int,
    resultCode: Int,
    data: Intent?
  ) {
    super.onActivityResult(
      requestCode,
      resultCode,
      data
    )
    when (requestCode) {
      RequestCodes.CATEGORY_SELECTION_REQUEST_CODE -> {
        budgetPot.category = data?.getParcelableExtra("category")
        updateViewFromData()
      }
    }
  }
}
