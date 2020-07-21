package app.pmsoft.ispork.transaction

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import app.pmsoft.ispork.data.FullBudgetPot
import app.pmsoft.ispork.data.Participant
import app.pmsoft.ispork.util.ZeroIsNullLongLiveData
import app.pmsoft.ispork.util.getValue
import app.pmsoft.ispork.util.setValue

abstract class AbstractBudgetPotAnnotationEditWrapper<BPA : BaseBudgetPotAnnotation>(
  final override val subTransactionEditWrapper: STEditWrapper<*, BPA>,
  val originalData: BPA
) : BPAEditWrapper<BPA> {

  final override val amountData = ZeroIsNullLongLiveData(originalData.amount)
  override var amount: Long? by amountData

  final override val budgetPotData = MutableLiveData(originalData.budgetPot)
  final override var budgetPot: FullBudgetPot? by budgetPotData

  private val notesData = MutableLiveData(originalData.notes)
  final override var notes: String? by notesData

  final override val participant: Participant by subTransactionEditWrapper.participantData

  private val _suggestedAmount = ZeroIsNullLongLiveData()
  final override val suggestedAmountData: LiveData<Long?> = _suggestedAmount
  final override val suggestedAmount: Long? by suggestedAmountData

  init {
    amountData.observeForever { subTransactionEditWrapper.updateSuggestedAmount() }
    updateSuggestedAmount()
  }

  final override fun updateSuggestedAmount() {
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

  override fun delete() {
    subTransactionEditWrapper.deleteBudgetPotAnnotation(this)
  }
}
