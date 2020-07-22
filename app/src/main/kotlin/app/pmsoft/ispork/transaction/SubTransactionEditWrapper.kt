package app.pmsoft.ispork.transaction

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import app.pmsoft.ispork.data.ExtendedMoneyBag
import app.pmsoft.ispork.data.FullBudgetPotAnnotation
import app.pmsoft.ispork.data.FullSubTransaction
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
  var moneyBag: ExtendedMoneyBag by moneyBagData

  private val bookingDateData = MutableLiveData(originalData.bookingDate)
  var bookingDate: Date? by bookingDateData

  private val notesData = MutableLiveData(originalData.notes)
  var notes: String? by notesData

  val budgetPotAnnotationsData = NonNullMutableLiveData<List<BudgetPotAnnotationEditWrapper>>(emptyList())
  var budgetPotAnnotations: List<BudgetPotAnnotationEditWrapper> by budgetPotAnnotationsData

  private val _suggestedAmountData = ZeroIsNullLongLiveData()
  val suggestedAmountData: LiveData<Long?> = _suggestedAmountData
  val suggestedAmount: Long? by suggestedAmountData

  private val triggerSuggestionUpdateObserver: Observer<Long?> = Observer {
    transactionEditWrapper.updateSuggestions()
  }

  init {
    budgetPotAnnotations = originalData.budgetPotAnnotations.map { fullBudgetPotAnnotation ->
      BudgetPotAnnotationEditWrapper(
        this,
        fullBudgetPotAnnotation
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
    val value1 = getSuggestedAmountThroughBudgetPots()
    val value2 = getSuggestedAmountThroughTransaction()
    var newValue = value1 ?: value2
    // if value1 and value2 do not agree, do not set a suggestion
    if (newValue != value2 ?: value1) {
      newValue = null
    }
    if (newValue != suggestedAmount) {
      _suggestedAmountData.value = newValue
      budgetPotAnnotations.forEach(BudgetPotAnnotationEditWrapper::updateSuggestedAmount)
    }
  }


  private fun getSuggestedAmountThroughTransaction(): Long? =
    transactionEditWrapper.getSiblingSubTransactions(this)
      // if any other sub transaction does not have an amount, no suggestion is possible
      .map { it.amount ?: it.getSuggestedAmountThroughBudgetPots() ?: return null }
      .sum()
      // this sub transaction must compensate all others, therefore invert the sum
      .unaryMinus()

  /**
   * Gets the amount inferred for this sub transaction by the values entered in its budgetPot annotations.
   *
   * @return `null` when any of the budget pot annotations does not have an amount.
   */
  private fun getSuggestedAmountThroughBudgetPots(): Long? =
    budgetPotAnnotations
      .takeUnless { it.isEmpty() }
      ?.map { it.amountInTransaction ?: return null }
      ?.sum()

  fun deleteBudgetPotAnnotation(budgetPotAnnotationEditWrapper: BudgetPotAnnotationEditWrapper) {
    budgetPotAnnotationEditWrapper.amountInTransactionData.removeObserver(triggerSuggestionUpdateObserver)
    this.budgetPotAnnotations -= budgetPotAnnotationEditWrapper
    transactionEditWrapper.updateSuggestions()
  }

  fun addNewBudgetPotAnnotation() {
    this.budgetPotAnnotations +=
      BudgetPotAnnotationEditWrapper(
        this,
        FullBudgetPotAnnotation()
      ).also {
        addObserverToChild(it)
      }
    transactionEditWrapper.updateSuggestions()
  }

  private fun addObserverToChild(child: BudgetPotAnnotationEditWrapper) {
    child.amountInTransactionData.observeForever(triggerSuggestionUpdateObserver)
  }

  fun extractSubTransaction(): FullSubTransaction {
    val actualBudgetPotAnnotations = budgetPotAnnotations.map { it.extractBudgetPotAnnotation() }
    if (actualBudgetPotAnnotations.size == 1) {
      actualBudgetPotAnnotations[0].amountInTransaction = amount ?: suggestedAmount ?: 0L
    }
    return FullSubTransaction(
      originalData.id,
      amount ?: suggestedAmount ?: 0L,
      amount ?: suggestedAmount ?: 0L,
      transactionEditWrapper.originalData.id,
      moneyBag,
      actualBudgetPotAnnotations,
      bookingDate,
      notes
    )
  }

  fun getSiblingAnnotations(target: BudgetPotAnnotationEditWrapper): List<BudgetPotAnnotationEditWrapper> {
    return budgetPotAnnotations - target
  }

  fun destroy() {
    budgetPotAnnotations.forEach {
      it.amountInTransactionData.removeObserver(triggerSuggestionUpdateObserver)
    }
    budgetPotAnnotations = emptyList()
  }
}
