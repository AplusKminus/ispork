package app.pmsoft.ispork.transaction

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import app.pmsoft.ispork.data.FullBudgetPot
import app.pmsoft.ispork.data.FullBudgetPotAnnotation
import app.pmsoft.ispork.data.Participant
import app.pmsoft.ispork.util.ZeroIsNullLongLiveData
import app.pmsoft.ispork.util.getValue
import app.pmsoft.ispork.util.setValue

class BudgetPotAnnotationEditWrapper(
  val subTransactionEditWrapper: SubTransactionEditWrapper,
  val originalData: FullBudgetPotAnnotation
) {

  val amountData = ZeroIsNullLongLiveData(originalData.amount)
  var amount: Long? by amountData

  val budgetPotData = MutableLiveData(originalData.budgetPot)
  var budgetPot: FullBudgetPot? by budgetPotData

  val notesData = MutableLiveData(originalData.notes)
  var notes: String? by notesData

  val participantData: LiveData<Participant> = subTransactionEditWrapper.participantData
  val participant: Participant by subTransactionEditWrapper.participantData

  private val _suggestedAmount = ZeroIsNullLongLiveData()
  val suggestedAmountData: LiveData<Long?> = _suggestedAmount
  val suggestedAmount: Long? by suggestedAmountData

  init {
    updateSuggestedAmount()
  }

  fun updateSuggestedAmount() {
    val newValue = if (amount == null) {
      getSuggestedAmountThroughSubTransaction()
    } else {
      null
    }
    if (newValue != suggestedAmount) {
      _suggestedAmount.value = newValue
    }
  }

  private fun getSuggestedAmountThroughSubTransaction(): Long? {
    val amountToBaseSuggestionOn = subTransactionEditWrapper.amount
      ?: subTransactionEditWrapper.getSuggestedAmountThroughTransaction()
      ?: return null
    return amountToBaseSuggestionOn - subTransactionEditWrapper.budgetPotAnnotations
      .filter { it !== this }
      .map { it.amount ?: return null }
      .sum()
  }

  fun delete() {
    subTransactionEditWrapper.deleteBudgetPotAnnotation(this)
  }

  fun addSibling() {
    subTransactionEditWrapper.addNewBudgetPotAnnotation()
  }

  fun extractBudgetPotAnnotation(): FullBudgetPotAnnotation {
    return FullBudgetPotAnnotation(
      originalData.id,
      subTransactionEditWrapper.originalData.id,
      amount ?: suggestedAmount ?: 0L,
      budgetPot,
      notes
    )
  }
}
