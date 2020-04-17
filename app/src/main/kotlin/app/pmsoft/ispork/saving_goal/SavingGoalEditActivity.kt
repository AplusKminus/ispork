package app.pmsoft.ispork.saving_goal

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import app.pmsoft.ispork.R
import app.pmsoft.ispork.RequestCodes
import app.pmsoft.ispork.category.CategoryPickingActivity
import app.pmsoft.ispork.data.FullBudgetPot
import app.pmsoft.ispork.data.FullSavingGoal
import app.pmsoft.ispork.util.dpToPx
import com.google.android.material.card.MaterialCardView

class SavingGoalEditActivity : AppCompatActivity() {

  private lateinit var selectedLayoutParams: LinearLayout.LayoutParams
  private lateinit var offeredLayoutParams: LinearLayout.LayoutParams
  private lateinit var remainingLayoutParams: LinearLayout.LayoutParams

  private lateinit var nameField: EditText
  private lateinit var notesField: EditText
  private lateinit var categoryField: EditText
  private lateinit var selectedCardLayout: LinearLayout
  private lateinit var offeredCardLayout: LinearLayout
  private lateinit var savingRateCard: MaterialCardView
  private lateinit var savingAmountCard: MaterialCardView
  private lateinit var savingDateCard: MaterialCardView
  private val selectedCards = mutableListOf<MaterialCardView>()
  private val offeredCards = mutableListOf<MaterialCardView>()
  private lateinit var selectionPlaceholder: View
  private lateinit var offeredPlaceholder: View

  private lateinit var savingGoal: FullSavingGoal

  private val cardClickListener = { view: View ->
    if ((view as MaterialCardView).isChecked) {
      deselectCard(view)
    } else {
      selectCard(view)
    }
    view.isChecked = !view.isChecked
    updateViewFromData()
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_saving_goal_edit)
    staticLayoutSetup()
    selectionPlaceholder = FrameLayout(applicationContext).also { it.layoutParams = selectedLayoutParams }
    offeredPlaceholder = FrameLayout(applicationContext).also { it.layoutParams = offeredLayoutParams }

    nameField = findViewById(R.id.saving_goal_edit_name_field)
    notesField = findViewById(R.id.saving_goal_edit_notes_field)
    categoryField = findViewById(R.id.saving_goal_edit_category_field)
    selectedCardLayout = findViewById(R.id.saving_goal_edit_selected_criteria_layout)
    offeredCardLayout = findViewById(R.id.saving_goal_edit_offered_criteria_layout)
    savingRateCard = findViewById(R.id.saving_goal_edit_rate_card)
    savingAmountCard = findViewById(R.id.saving_goal_edit_target_amount_card)
    savingDateCard = findViewById(R.id.saving_goal_edit_target_date_card)
    offeredCards.add(savingRateCard)
    offeredCards.add(savingAmountCard)
    offeredCards.add(savingDateCard)
    offeredCards.forEach {
      it.isCheckable = true
      it.setOnClickListener(cardClickListener)
    }

    title = intent.getStringExtra("title")

    when (intent.getIntExtra(
      "request_code",
      0
    )) {
      RequestCodes.SAVING_GOAL_CREATION_REQUEST_CODE -> {
        savingGoal = FullSavingGoal()
      }
      RequestCodes.SAVING_GOAL_EDITING_REQUEST_CODE -> {
        savingGoal = intent.getParcelableExtra("savingGoal")
      }
    }
    if (savingGoal.budgetPot == null) {
      savingGoal.budgetPot = FullBudgetPot()
    }
    updateViewFromData()
  }

  private fun staticLayoutSetup() {
    selectedLayoutParams = LinearLayout.LayoutParams(
      LinearLayout.LayoutParams.MATCH_PARENT,
      LinearLayout.LayoutParams.MATCH_PARENT,
      1f / 2
    ).also {
      it.leftMargin = 4.dpToPx(resources)
      it.rightMargin = it.leftMargin
    }
    offeredLayoutParams = LinearLayout.LayoutParams(
      LinearLayout.LayoutParams.MATCH_PARENT,
      LinearLayout.LayoutParams.MATCH_PARENT,
      1f / 3
    ).also {
      it.leftMargin = 4.dpToPx(resources)
      it.rightMargin = it.leftMargin
    }
    remainingLayoutParams = LinearLayout.LayoutParams(
      LinearLayout.LayoutParams.MATCH_PARENT,
      LinearLayout.LayoutParams.MATCH_PARENT,
      1f
    ).also {
      it.leftMargin = 4.dpToPx(resources)
      it.rightMargin = it.leftMargin
    }
  }

  private fun selectCard(card: MaterialCardView) {
    selectedCards.add(card)
    offeredCards.remove(card)
  }

  private fun deselectCard(card: MaterialCardView) {
    selectedCards.remove(card)
    val index: Int = when (card) {
      savingRateCard -> 0
      savingAmountCard -> if (offeredCards.contains(savingRateCard)) 1 else 0
      else -> offeredCards.size
    }
    offeredCards.add(
      index,
      card
    )
  }

  private fun updateViewFromData() {
    nameField.text.clear()
    nameField.text.insert(
      0,
      savingGoal.name
    )
    notesField.text.clear()
    notesField.text.insert(
      0,
      savingGoal.notes ?: ""
    )
    categoryField.text.clear()
    categoryField.text.insert(
      0,
      savingGoal.budgetPot?.category?.name ?: ""
    )
    selectedCardLayout.removeAllViews()
    offeredCardLayout.removeAllViews()
    when (selectedCards.size) {
      0 -> {
      }
      1 -> {
        selectedCardLayout.addView(selectedCards[0].also { it.layoutParams = selectedLayoutParams })
        selectedCardLayout.addView(selectionPlaceholder)
      }
      else -> {
        for (i in 0..1) {
          selectedCardLayout.addView(selectedCards[i].also { it.layoutParams = selectedLayoutParams })
        }
      }
    }
    when (offeredCards.size) {
      0 -> {
      }
      1 -> {
        offeredCardLayout.addView(offeredCards[0].also {
          it.layoutParams = remainingLayoutParams
          it.setOnClickListener(null)
        })
      }
      else -> {
        // at most 1 of the else clauses can be triggered.
        // due to the other 2 when-branches, the size of offeredCards must be 2 or 3 at this time
        if (offeredCards.contains(savingRateCard)) {
          offeredCardLayout.addView(savingRateCard.also {
            it.layoutParams = offeredLayoutParams
            it.setOnClickListener(cardClickListener)
          })
        } else {
          offeredCardLayout.addView(offeredPlaceholder)
        }
        if (offeredCards.contains(savingAmountCard)) {
          offeredCardLayout.addView(savingAmountCard.also {
            it.layoutParams = offeredLayoutParams
            it.setOnClickListener(cardClickListener)
          })
        } else {
          offeredCardLayout.addView(offeredPlaceholder)
        }
        if (offeredCards.contains(savingDateCard)) {
          offeredCardLayout.addView(savingDateCard.also {
            it.layoutParams = offeredLayoutParams
            it.setOnClickListener(cardClickListener)
          })
        } else {
          offeredCardLayout.addView(offeredPlaceholder)
        }
      }
    }
  }

  @SuppressWarnings("UNUSED_PARAMETER")
  fun pickCategory(view: View) {
    val intent = Intent(
      this,
      CategoryPickingActivity::class.java
    )
    this.startActivityForResult(
      intent,
      RequestCodes.CATEGORY_SELECTION_REQUEST_CODE
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
        RequestCodes.CATEGORY_SELECTION_REQUEST_CODE -> {
          savingGoal.budgetPot?.category = intent.getParcelableExtra("category")
          updateViewFromData()
        }
      }
    }
  }

  override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
    R.id.action_save_saving_goal -> {
      savingGoal.name = nameField.text.toString().trim()
      savingGoal.notes = notesField.text.toString().trim()
      intent.putExtra(
        "savingGoal",
        savingGoal
      )
      setResult(
        AppCompatActivity.RESULT_OK,
        intent
      )
      finish()
      true
    }
    else -> {
      super.onOptionsItemSelected(item)
    }
  }

  override fun onCreateOptionsMenu(menu: Menu?): Boolean {
    menuInflater.inflate(
      R.menu.saving_goal_edit_menu,
      menu
    )
    return true
  }
}
