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
  private val subTransactionEditWrapper: SubTransactionEditWrapper,
  private val originalData: FullBudgetPotAnnotation
) {

  val amountData = ZeroIsNullLongLiveData(originalData.amount)
  var amount: Long? by amountData

  val budgetPotData = MutableLiveData(originalData.budgetPot)
  var budgetPot: FullBudgetPot? by budgetPotData

  private val notesData = MutableLiveData(originalData.notes)
  var notes: String? by notesData

  val participant: Participant by subTransactionEditWrapper.participantData

  private val _suggestedAmount = ZeroIsNullLongLiveData()
  val suggestedAmountData: LiveData<Long?> = _suggestedAmount
  private val suggestedAmount: Long? by suggestedAmountData

  init {
    amountData.observeForever { subTransactionEditWrapper.updateSuggestedAmount() }
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
    // base the suggestion on either amount or suggestion from the sub transaction
    return (subTransactionEditWrapper.amount ?: subTransactionEditWrapper.suggestedAmount)
      // if a suggestion is possible, subtract the sum of the other annotations
      ?.minus(
        subTransactionEditWrapper.getSiblingAnnotations(this)
          // if a sibling annotation does not have an amount, no suggestion is possible -> abort with null
          .map { it.amount ?: return null }
          .sum()
      )
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
