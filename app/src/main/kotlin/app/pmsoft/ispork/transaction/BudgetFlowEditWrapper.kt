package app.pmsoft.ispork.transaction

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import app.pmsoft.ispork.data.Amount
import app.pmsoft.ispork.data.FullBudgetFlow
import app.pmsoft.ispork.data.FullBudgetPot
import app.pmsoft.ispork.data.MoneyBagWithParticipant
import app.pmsoft.ispork.util.ZeroIsNullLongLiveData
import app.pmsoft.ispork.util.getValue
import app.pmsoft.ispork.util.setValue

class BudgetFlowEditWrapper(
  val subTransactionEditWrapper: SubTransactionEditWrapper,
  private val originalData: FullBudgetFlow
) {

  val amountInBudgetData = ZeroIsNullLongLiveData(originalData.amountInBudget)
  var amountInBudget: Amount? by amountInBudgetData

  val amountInTransactionData = ZeroIsNullLongLiveData(originalData.amountInTransaction)
  var amountInTransaction: Amount? by amountInTransactionData

  val budgetPotData = MutableLiveData(originalData.budgetPot)
  var budgetPot: FullBudgetPot? by budgetPotData

  private val notesData = MutableLiveData(originalData.notes)
  var notes: String? by notesData

  val moneyBag: MoneyBagWithParticipant by subTransactionEditWrapper.moneyBagData

  private val _suggestedAmount = ZeroIsNullLongLiveData()
  val suggestedAmountData: LiveData<Long?> = _suggestedAmount
  private val suggestedAmount: Long? by suggestedAmountData

  init {
    amountInTransactionData.observeForever { subTransactionEditWrapper.updateSuggestedAmount() }
    updateSuggestedAmount()
  }

  fun updateSuggestedAmount() {
    val newValue = if (amountInTransaction == null) {
      getSuggestedAmountThroughSubTransaction()
    } else {
      null
    }
    if (newValue != suggestedAmount) {
      _suggestedAmount.value = newValue
    }
  }

  private fun getSuggestedAmountThroughSubTransaction(): Amount? {
    // base the suggestion on either amount or suggestion from the sub transaction
    return (subTransactionEditWrapper.amount ?: subTransactionEditWrapper.suggestedAmount)
      // if a suggestion is possible, subtract the sum of the other flows
      ?.minus(
        subTransactionEditWrapper.getSiblingFlows(this)
          // if a sibling flow does not have an amount, no suggestion is possible -> abort with null
          .map { it.amountInTransaction ?: return null }
          .sum()
      )
  }

  fun delete() {
    subTransactionEditWrapper.deleteBudgetFlow(this)
  }

  fun extractBudgetFlow(): FullBudgetFlow {
    return FullBudgetFlow(
      originalData.id,
      subTransactionEditWrapper.originalData.id,
      amountInTransaction ?: suggestedAmount ?: 0L,
      amountInTransaction ?: suggestedAmount ?: 0L,
      budgetPot,
      notes
    )
  }
}