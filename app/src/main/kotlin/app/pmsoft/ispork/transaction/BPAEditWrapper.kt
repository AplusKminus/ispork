package app.pmsoft.ispork.transaction

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import app.pmsoft.ispork.data.BaseBudgetPotAnnotation
import app.pmsoft.ispork.data.FullBudgetPot
import app.pmsoft.ispork.data.Participant

interface BPAEditWrapper<BPA : BaseBudgetPotAnnotation> {

  val subTransactionEditWrapper: STEditWrapper<*, BPA>

  val amountData: MutableLiveData<Long?>
  var amount: Long?

  val budgetPotData: LiveData<FullBudgetPot?>
  var budgetPot: FullBudgetPot?

  var notes: String?

  val participant: Participant

  val suggestedAmountData: LiveData<Long?>
  val suggestedAmount: Long?

  fun updateSuggestedAmount()

  fun extractBudgetPotAnnotation(): BPA

  fun delete()
}
