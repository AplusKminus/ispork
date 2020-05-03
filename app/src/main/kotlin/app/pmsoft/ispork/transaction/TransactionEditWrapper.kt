package app.pmsoft.ispork.transaction

import app.pmsoft.ispork.data.FullSubTransaction
import app.pmsoft.ispork.data.FullTransaction
import app.pmsoft.ispork.data.Participant
import app.pmsoft.ispork.util.NonNullLiveData
import app.pmsoft.ispork.util.NonNullMutableLiveData
import app.pmsoft.ispork.util.setValue
import java.util.*

/**
 * This wrapper exists to update data across the "tree" that is a [app.pmsoft.ispork.data.Transaction].
 */
class TransactionEditWrapper(
  val originalData: FullTransaction
) {

  private val entryDateData = NonNullMutableLiveData(originalData.entryDate)
  var entryDate: Date by entryDateData

  private val _subTransactionsData: NonNullMutableLiveData<List<SubTransactionEditWrapper>> = NonNullMutableLiveData(
    emptyList()
  )
  val subTransactionsData: NonNullLiveData<List<SubTransactionEditWrapper>> = _subTransactionsData.asImmutable()
  val subTransactions: List<SubTransactionEditWrapper> by _subTransactionsData

  init {
    _subTransactionsData.value = originalData.subTransactions.map { fullSubTransaction ->
      SubTransactionEditWrapper(
        this,
        fullSubTransaction
      ).also {
        addObserversToChild(it)
      }
    }
  }

  /**
   * Updates the suggestions across the entire tree.
   */
  fun updateSuggestions() {
    subTransactions.forEach(SubTransactionEditWrapper::updateSuggestedAmount)
  }

  private fun addObserversToChild(child: SubTransactionEditWrapper) {
    child.amountData.observeForever {
      updateSuggestions()
    }
  }

  fun createSubTransactionsFor(participants: List<Participant>) {
    val newList = mutableListOf<SubTransactionEditWrapper>()
    for (participant in participants) {
      val existing = subTransactions.firstOrNull {
        it.participant.id == participant.id
      }
      if (existing != null) {
        newList.add(existing)
      } else {
        newList.add(SubTransactionEditWrapper(
          this,
          FullSubTransaction(participant).also {
            if (!participant.type.internal) {
              it.bookingDate = entryDate
            }
          }
        ).also {
          addObserversToChild(it)
          if (!it.participant.type.internal) {
            it.addNewBudgetPotAnnotation()
          }
        })
      }
    }
    subTransactions.filter { !newList.contains(it) }.forEach { it.destroy() }
    _subTransactionsData.value = newList
    updateSuggestions()
  }

  fun getSiblingSubTransactions(subTransactionEditWrapper: SubTransactionEditWrapper): List<SubTransactionEditWrapper> {
    return subTransactions - subTransactionEditWrapper
  }

  fun extractTransaction(): FullTransaction {
    return FullTransaction(
      originalData.id,
      entryDate,
      subTransactions.map { it.extractSubTransaction() }
    )
  }
}
