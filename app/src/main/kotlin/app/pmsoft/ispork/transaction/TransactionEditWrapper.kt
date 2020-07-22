package app.pmsoft.ispork.transaction

import androidx.lifecycle.MutableLiveData
import app.pmsoft.ispork.data.FullSubTransaction
import app.pmsoft.ispork.data.FullTransactionDefinition
import app.pmsoft.ispork.data.MoneyBagWithParticipant
import app.pmsoft.ispork.util.NonNullLiveData
import app.pmsoft.ispork.util.NonNullMutableLiveData
import app.pmsoft.ispork.util.getValue
import app.pmsoft.ispork.util.setValue
import java.util.*

/**
 * This wrapper exists to update data across the "tree" that is a [app.pmsoft.ispork.data.Transaction].
 */
class TransactionEditWrapper(
  val originalData: FullTransactionDefinition
) {

  private val entryDateData = MutableLiveData(originalData.entryDate)
  var entryDate: Date? by entryDateData

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

  fun createSubTransactionsFor(moneyBags: List<MoneyBagWithParticipant>) {
    val newList = mutableListOf<SubTransactionEditWrapper>()
    for (moneyBag in moneyBags) {
      val existing = subTransactions.firstOrNull {
        it.moneyBag.id == moneyBag.id
      }
      if (existing != null) {
        newList.add(existing)
      } else {
        newList.add(
          SubTransactionEditWrapper(
            this,
          FullSubTransaction(moneyBag).also {
            if (!moneyBag.participant.type.internal) {
              it.bookingDate = entryDate
            }
          }
        ).also {
          addObserversToChild(it)
          if (!it.moneyBag.participant.type.internal) {
            it.addNewBudgetFlow()
          }
        })
      }
    }
    subTransactions.filter { !newList.contains(it) }
      .forEach { it.destroy() }
    _subTransactionsData.value = newList
    updateSuggestions()
  }

  fun getSiblingSubTransactions(subTransactionEditWrapper: SubTransactionEditWrapper): List<SubTransactionEditWrapper> {
    return subTransactions - subTransactionEditWrapper
  }

  fun extractTransaction(): FullTransactionDefinition {
    return FullTransactionDefinition(
      originalData.id,
      entryDate,
      originalData.sourceScheduledTransactionId,
      subTransactions.map { it.extractSubTransaction() }
    )
  }
}
