package app.pmsoft.ispork.transaction

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import app.pmsoft.ispork.data.FullBudgetFlow
import app.pmsoft.ispork.data.FullSubTransaction
import app.pmsoft.ispork.data.MoneyBagWithParticipant
import app.pmsoft.ispork.util.NonNullMutableLiveData
import app.pmsoft.ispork.util.ZeroIsNullLongLiveData
import app.pmsoft.ispork.util.getValue
import app.pmsoft.ispork.util.setValue
import java.util.*

class SubTransactionEditWrapper(
  private val transactionEditWrapper: TransactionEditWrapper,
  val originalData: FullSubTransaction
) {

  val amountData = ZeroIsNullLongLiveData(originalData.amountInBookedCurrency)
  var amount: Long? by amountData

  val moneyBagData = NonNullMutableLiveData(originalData.moneyBag)
  var moneyBag: MoneyBagWithParticipant by moneyBagData

  private val bookingDateData = MutableLiveData(originalData.bookingDate)
  var bookingDate: Date? by bookingDateData

  private val notesData = MutableLiveData(originalData.notes)
  var notes: String? by notesData

  val budgetFlowsData = NonNullMutableLiveData<List<BudgetFlowEditWrapper>>(emptyList())
  var budgetFlows: List<BudgetFlowEditWrapper> by budgetFlowsData

  private val _suggestedAmountData = ZeroIsNullLongLiveData()
  val suggestedAmountData: LiveData<Long?> = _suggestedAmountData
  val suggestedAmount: Long? by suggestedAmountData

  var isIncome: Boolean = false

  private val triggerSuggestionUpdateObserver: Observer<Long?> = Observer {
    transactionEditWrapper.updateSuggestions()
  }

  init {
    budgetFlows = originalData.budgetFlows.map { fullBudgetFlow ->
      BudgetFlowEditWrapper(
        this,
        fullBudgetFlow
      ).also {
        addObserverToChild(it)
      }
    }
    updateSuggestedAmount()
  }

  fun updateSuggestedAmount() {
    if (amount != null) {
      return
    }
    val value1 = getSuggestedAmountThroughBudgetFlows()
    val value2 = getSuggestedAmountThroughTransaction()
    var newValue = value1 ?: value2
    // if value1 and value2 do not agree, do not set a suggestion
    if (newValue != value2 ?: value1) {
      newValue = null
    }
    if (newValue != suggestedAmount) {
      _suggestedAmountData.value = newValue
      budgetFlows.forEach(BudgetFlowEditWrapper::updateSuggestedAmount)
    }
  }


  private fun getSuggestedAmountThroughTransaction(): Long? =
    transactionEditWrapper.getSiblingSubTransactions(this)
      // if any other sub transaction does not have an amount, no suggestion is possible
      .map { it.amount ?: it.getSuggestedAmountThroughBudgetFlows() ?: return null }
      .sum()
      // this sub transaction must compensate all others, therefore invert the sum
      .unaryMinus()

  /**
   * Gets the amount inferred for this sub transaction by the values entered in its budget pot flows.
   *
   * @return `null` when any of the budget flows does not have an amount.
   */
  private fun getSuggestedAmountThroughBudgetFlows(): Long? =
    budgetFlows
      .takeUnless { it.isEmpty() }
      ?.map { it.amountInTransaction ?: return null }
      ?.sum()

  fun deleteBudgetFlow(budgetFlowEditWrapper: BudgetFlowEditWrapper) {
    budgetFlowEditWrapper.amountInTransactionData.removeObserver(triggerSuggestionUpdateObserver)
    this.budgetFlows -= budgetFlowEditWrapper
    transactionEditWrapper.updateSuggestions()
  }

  fun addNewBudgetFlow() {
    this.budgetFlows +=
      BudgetFlowEditWrapper(
        this,
        FullBudgetFlow()
      ).also {
        addObserverToChild(it)
      }
    transactionEditWrapper.updateSuggestions()
  }

  private fun addObserverToChild(child: BudgetFlowEditWrapper) {
    child.amountInTransactionData.observeForever(triggerSuggestionUpdateObserver)
  }

  fun extractSubTransaction(): FullSubTransaction {
    val actualBudgetFlows = if (isIncome) emptyList() else budgetFlows.map { it.extractBudgetFlow() }
    if (actualBudgetFlows.size == 1) {
      actualBudgetFlows[0].amountInTransaction = amount ?: suggestedAmount ?: 0L
    }
    return FullSubTransaction(
      originalData.id,
      amount ?: suggestedAmount ?: 0L,
      amount ?: suggestedAmount ?: 0L,
      transactionEditWrapper.originalData.id,
      moneyBag,
      actualBudgetFlows,
      bookingDate,
      notes
    )
  }

  fun getSiblingFlows(target: BudgetFlowEditWrapper): List<BudgetFlowEditWrapper> {
    return budgetFlows - target
  }

  fun destroy() {
    budgetFlows.forEach {
      it.amountInTransactionData.removeObserver(triggerSuggestionUpdateObserver)
    }
    budgetFlows = emptyList()
  }
}
