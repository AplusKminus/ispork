package app.pmsoft.ispork.transaction

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import app.pmsoft.ispork.data.BaseBudgetPotAnnotation
import app.pmsoft.ispork.data.BaseSubTransaction
import app.pmsoft.ispork.data.Participant
import app.pmsoft.ispork.util.*

abstract class AbstractSubTransactionEditWrapper<ST : BaseSubTransaction<BPA>, BPA : BaseBudgetPotAnnotation>(
  private val transactionEditWrapper: AbstractTransactionEditWrapper<*, ST, BPA>,
  val originalData: ST
) : STEditWrapper<ST, BPA> {

  final override val amountData = ZeroIsNullLongLiveData(originalData.amount)
  final override var amount: Long? by amountData

  private val _participantData = NonNullMutableLiveData(originalData.participant)
  final override val participantData: NonNullLiveData<Participant> = _participantData.asImmutable()
  final override var participant: Participant by _participantData

  private val notesData = MutableLiveData(originalData.notes)
  final override var notes: String? by notesData

  final override val budgetPotAnnotationsData = NonNullMutableLiveData<List<BPAEditWrapper<BPA>>>(emptyList())
  final override var budgetPotAnnotations: List<BPAEditWrapper<BPA>> by budgetPotAnnotationsData

  private val _suggestedAmountData = ZeroIsNullLongLiveData()
  final override val suggestedAmountData: LiveData<Long?> = _suggestedAmountData
  final override val suggestedAmount: Long? by suggestedAmountData

  private val triggerSuggestionUpdateObserver: Observer<Long?> = Observer {
    transactionEditWrapper.updateSuggestions()
  }

  init {
    budgetPotAnnotations = originalData.budgetPotAnnotations.map { fullBudgetPotAnnotation ->
      createBudgetPotAnnotationWrapper(fullBudgetPotAnnotation).also {
        addObserverToChild(it)
      }
    }
    updateSuggestedAmount()
  }

  abstract fun createBudgetPotAnnotationWrapper(fullBudgetPotAnnotation: BPA): BPAEditWrapper<BPA>

  abstract fun createNewBudgetPotAnnotation(): BPA

  override fun updateSuggestedAmount() {
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
      budgetPotAnnotations.forEach(BPAEditWrapper<BPA>::updateSuggestedAmount)
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
  override fun getSuggestedAmountThroughBudgetPots(): Long? =
    budgetPotAnnotations
      .takeUnless { it.isEmpty() }
      ?.map { it.amount ?: return null }
      ?.sum()

  override fun deleteBudgetPotAnnotation(bpaEditWrapper: BPAEditWrapper<BPA>) {
    bpaEditWrapper.amountData.removeObserver(triggerSuggestionUpdateObserver)
    this.budgetPotAnnotations -= bpaEditWrapper
    transactionEditWrapper.updateSuggestions()
  }

  override fun addNewBudgetPotAnnotation() {
    this.budgetPotAnnotations +=
      createBudgetPotAnnotationWrapper(createNewBudgetPotAnnotation()).also {
        addObserverToChild(it)
      }
    transactionEditWrapper.updateSuggestions()
  }

  private fun addObserverToChild(child: BPAEditWrapper<BPA>) {
    child.amountData.observeForever(triggerSuggestionUpdateObserver)
  }

  override fun getSiblingAnnotations(target: BPAEditWrapper<BPA>): List<BPAEditWrapper<BPA>> {
    return budgetPotAnnotations - target
  }

  override fun destroy() {
    budgetPotAnnotations.forEach {
      it.amountData.removeObserver(triggerSuggestionUpdateObserver)
    }
    budgetPotAnnotations = emptyList()
  }
}
