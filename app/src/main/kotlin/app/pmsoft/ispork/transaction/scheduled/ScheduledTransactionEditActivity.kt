package app.pmsoft.ispork.transaction.scheduled

import android.content.Intent
import android.os.Bundle
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.ViewPager
import app.pmsoft.ispork.R
import app.pmsoft.ispork.RequestCodes
import app.pmsoft.ispork.data.FullBudgetPot
import app.pmsoft.ispork.data.FullScheduledTransaction
import app.pmsoft.ispork.data.Participant
import app.pmsoft.ispork.participant.ParticipantPickingActivity
import com.google.android.material.tabs.TabLayout

class ScheduledTransactionEditActivity : AppCompatActivity() {

  private lateinit var nameField: EditText
  private lateinit var nextOccurrenceDateView: TextView
  private lateinit var tabLayout: TabLayout
  private lateinit var viewPager: ViewPager
  private lateinit var viewPagerAdapter: ViewPagerAdapter

  private var data: ScheduledTransactionEditWrapper = ScheduledTransactionEditWrapper(FullScheduledTransaction())

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_scheduled_transaction_edit)
    nameField = findViewById(R.id.scheduled_transaction_edit_name_field)
    nextOccurrenceDateView = findViewById(R.id.scheduled_transaction_edit_next_occurrence_date_label)
    tabLayout = findViewById(R.id.scheduled_transaction_edit_tab_layout)
    viewPager = findViewById(R.id.scheduled_transaction_edit_view_pager)

    tabLayout.setupWithViewPager(viewPager)
    viewPagerAdapter = ViewPagerAdapter(supportFragmentManager, this)
    viewPager.adapter = viewPagerAdapter

    updateViewFromData()
  }

  fun updateViewFromData() {
    viewPagerAdapter.setData(data.subTransactionTemplates, data.rhythms)
  }

  fun pickParticipants() {
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
          val budgetPot = intent.getParcelableExtra<FullBudgetPot>("budgetPot")
          val budgetPotAnnotation: BudgetPotAnnotationTemplateEditWrapper
          val subTransaction = this.data.subTransactionTemplates[subTransactionIndex]
          if (categoryAnnotationIndex < 0) {
            budgetPotAnnotation = BudgetPotAnnotationTemplateEditWrapper(
              subTransaction,
              FullBudgetPotAnnotationTemplate()
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
}
