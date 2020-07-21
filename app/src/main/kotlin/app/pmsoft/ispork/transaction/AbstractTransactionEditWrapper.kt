package app.pmsoft.ispork.transaction

import app.pmsoft.ispork.data.BaseBudgetPotAnnotation
import app.pmsoft.ispork.data.BaseSubTransaction
import app.pmsoft.ispork.data.FullBaseTransaction
import app.pmsoft.ispork.data.Participant
import app.pmsoft.ispork.util.NonNullLiveData
import app.pmsoft.ispork.util.NonNullMutableLiveData

/**
 * This wrapper exists to update data across the "tree" that is a [app.pmsoft.ispork.data.TransactionDefinition].
 */
abstract class AbstractTransactionEditWrapper<T : FullBaseTransaction<ST, BPA>, ST : BaseSubTransaction<BPA>, BPA : BaseBudgetPotAnnotation>(
  val originalData: T
) : TEditWrapper<T, ST, BPA> {

  private val _subTransactionsData: NonNullMutableLiveData<List<STEditWrapper<ST, BPA>>> = NonNullMutableLiveData(
    emptyList()
  )
  open val subTransactionsData: NonNullLiveData<List<STEditWrapper<ST, BPA>>> = _subTransactionsData.asImmutable()
  override val subTransactions: List<STEditWrapper<ST, BPA>> by _subTransactionsData

  init {
    _subTransactionsData.value = originalData.subTransactions.map { fullSubTransaction ->
      createSubTransactionEditWrapper(fullSubTransaction).also {
        addObserversToChild(it)
      }
    }
  }

  abstract fun createSubTransactionEditWrapper(fullSubTransaction: ST): STEditWrapper<ST, BPA>

  abstract fun createNewSubTransactionForParticipant(participant: Participant): ST

  /**
   * Updates the suggestions across the entire tree.
   */
  fun updateSuggestions() {
    subTransactions.forEach(STEditWrapper<ST, BPA>::updateSuggestedAmount)
  }

  private fun addObserversToChild(child: STEditWrapper<ST, BPA>) {
    child.amountData.observeForever {
      updateSuggestions()
    }
  }

  fun createSubTransactionsFor(participants: List<Participant>) {
    val newList = mutableListOf<STEditWrapper<ST, BPA>>()
    for (participant in participants) {
      val existing = subTransactions.firstOrNull {
        it.participant.id == participant.id
      }
      if (existing != null) {
        newList.add(existing)
      } else {
        newList.add(createSubTransactionEditWrapper(
          createNewSubTransactionForParticipant(participant)
        ).also {
          addObserversToChild(it)
          if (!it.participant.type.internal) {
            it.addNewBudgetPotAnnotation()
          }
        })
      }
    }
    subTransactions.filter { !newList.contains(it) }
      .forEach { it.destroy() }
    _subTransactionsData.value = newList
    updateSuggestions()
  }

  override fun getSiblingSubTransactions(target: STEditWrapper<ST, BPA>): List<STEditWrapper<ST, BPA>> {
    return subTransactions - target
  }
}
