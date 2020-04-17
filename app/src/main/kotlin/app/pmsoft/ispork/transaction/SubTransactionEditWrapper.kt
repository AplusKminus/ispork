package app.pmsoft.ispork.transaction

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import app.pmsoft.ispork.data.FullBudgetPotAnnotation
import app.pmsoft.ispork.data.FullSubTransaction
import app.pmsoft.ispork.data.Participant
import app.pmsoft.ispork.util.NonNullMutableLiveData
import app.pmsoft.ispork.util.ZeroIsNullLongLiveData
import app.pmsoft.ispork.util.getValue
import app.pmsoft.ispork.util.setValue
import java.util.*

class SubTransactionEditWrapper(
  val transactionEditWrapper: TransactionEditWrapper,
  val originalData: FullSubTransaction
) {

  val amountData = ZeroIsNullLongLiveData(originalData.amount)
  var amount: Long? by amountData

  val participantData = NonNullMutableLiveData(originalData.participant)
  var participant: Participant by participantData

  val bookingDateData = MutableLiveData(originalData.bookingDate)
  var bookingDate: Date? by bookingDateData

  val notesData = MutableLiveData(originalData.notes)
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

  private fun updateSuggestedAmount() {
    updateSuggestedAmountThroughCategories()
    updateSuggestedAmountThroughTransaction()
  }

  fun updateSuggestedAmountThroughCategories() {
    val newValue = if (amount == null) {
      getSuggestedAmountThroughCategories()
    } else {
      null
    }
    if (newValue != suggestedAmount) {
      _suggestedAmountData.value = newValue
    }
  }

  fun updateSuggestedAmountThroughTransaction() {
    val newValue = if (amount == null) {
      getSuggestedAmountThroughTransaction()
    } else {
      null
    }
    if (newValue != suggestedAmount) {
      _suggestedAmountData.value = newValue
    }
  }


  fun getSuggestedAmountThroughTransaction(): Long? {
    if (transactionEditWrapper.subTransactions.all { it.participant.type.internal }) {
      val noAmount = transactionEditWrapper.subTransactions.filter { it.amount == null }
      if (noAmount.size == 1 && noAmount[0] === this) {
        return -transactionEditWrapper.subTransactions.mapNotNull { it.amount }.sum()
      }
      return null
    } else {
      val amountToBaseSuggestionOn = transactionEditWrapper.amount
        ?: if (this.participant.type.internal) {
          transactionEditWrapper.getSuggestedAmountThroughExternalSubTransactions()
        } else {
          transactionEditWrapper.getSuggestedAmountThroughInternalSubTransactions()
        }
        ?: return null
      // the suggestion value depends on siblings

      return if (participant.type.internal) {
        amountToBaseSuggestionOn - transactionEditWrapper.getSiblings(this)
          .filter { it.participant.type.internal }
          .map { it.amount ?: it.suggestedAmount ?: return null }
          .sum()
      } else {
        // there can only be one external sub transaction, therefore the total amount is the negated suggested value
        // it is negated because a negative value in the transaction means a positive value for the external participant
        -amountToBaseSuggestionOn
      }
    }
  }

  /**
   * Gets the amount inferred for this sub transaction by the values entered in its budgetPot annotations.
   *
   * @return `null` when no inferred value can be calculated.
   */
  fun getSuggestedAmountThroughCategories(): Long? =
    budgetPotAnnotations
      .takeUnless { it.isEmpty() }
      ?.map { it.amount ?: return null }
      ?.sum()

  fun deleteBudgetPotAnnotation(budgetPotAnnotationEditWrapper: BudgetPotAnnotationEditWrapper) {
    budgetPotAnnotationEditWrapper.amountData.removeObserver(triggerSuggestionUpdateObserver)
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
    child.amountData.observeForever(triggerSuggestionUpdateObserver)
  }

  fun extractSubTransaction(): FullSubTransaction {
    val actualBudgetPotAnnotations = budgetPotAnnotations.map { it.extractBudgetPotAnnotation() }
    if (actualBudgetPotAnnotations.size == 1) {
      actualBudgetPotAnnotations[0].amount = amount ?: suggestedAmount ?: 0L
    }
    return FullSubTransaction(
      originalData.id,
      amount ?: suggestedAmount ?: 0L,
      transactionEditWrapper.originalData.id,
      participant,
      bookingDate,
      notes,
      actualBudgetPotAnnotations
    )
  }

  fun destroy() {
    budgetPotAnnotations.forEach {
      it.amountData.removeObserver(triggerSuggestionUpdateObserver)
    }
    budgetPotAnnotations = emptyList()
  }
}
