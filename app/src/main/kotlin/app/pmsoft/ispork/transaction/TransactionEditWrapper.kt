package app.pmsoft.ispork.transaction

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import app.pmsoft.ispork.data.FullSubTransaction
import app.pmsoft.ispork.data.FullTransaction
import app.pmsoft.ispork.data.Participant
import app.pmsoft.ispork.util.*
import java.util.*

class TransactionEditWrapper(
  val originalData: FullTransaction
) {

  val entryDateData = NonNullMutableLiveData(originalData.entryDate)
  var entryDate: Date by entryDateData

  val notesData = MutableLiveData(originalData.notes)
  var notes: String? by notesData

  private val _subTransactionsData: NonNullMutableLiveData<List<SubTransactionEditWrapper>> = NonNullMutableLiveData(emptyList())
  val subTransactionsData: NonNullLiveData<List<SubTransactionEditWrapper>> = _subTransactionsData.asImmutable()
  val subTransactions: List<SubTransactionEditWrapper> by _subTransactionsData

  val amountData = ZeroIsNullLongLiveData(originalData.getExternalSum())
  var amount: Long? by amountData

  private val _suggestedAmountData = ZeroIsNullLongLiveData()
  val suggestedAmountData: LiveData<Long?> = _suggestedAmountData
  val suggestedAmount: Long? by suggestedAmountData

  init {
    _subTransactionsData.value = originalData.subTransactions.map {
      SubTransactionEditWrapper(
        this,
        it
      ).also {
        addObserversToChild(it)
      }
    }
    amountData.observeForever {
      updateSuggestions()
    }
    updateSuggestedAmount()
  }

  fun updateSuggestions() {
    subTransactions.forEach {
      if (it.amount == null) {
        it.updateSuggestedAmountThroughCategories()
        if (it.suggestedAmount == null) {
          it.updateSuggestedAmountThroughTransaction()
        }
      }
      it.categoryAnnotations.forEach { annotationWrapper ->
        annotationWrapper.updateSuggestedAmount()
      }
    }
    updateSuggestedAmount()
  }

  private fun addObserversToChild(child: SubTransactionEditWrapper) {
    child.amountData.observeForever {
      updateSuggestions()
    }
  }

  private fun updateSuggestedAmount() {
    val newValue = if (amount == null) {
      getSuggestedAmountThroughExternalSubTransactions()
        ?: getSuggestedAmountThroughInternalSubTransactions()
    } else {
      null
    }
    if (newValue != suggestedAmount) {
      _suggestedAmountData.value = newValue
    }
  }

  fun getSuggestedAmountThroughInternalSubTransactions(): Long? =
    subTransactions
      .filter { it.participant.type.internal }
      .takeUnless { it.isEmpty() }
      ?.map { it.amount ?: return null }
      ?.sum()

  fun getSuggestedAmountThroughExternalSubTransactions(): Long? =
    subTransactions
      .firstOrNull { !it.participant.type.internal }
      ?.let { it.amount ?: it.getSuggestedAmountThroughCategories() }
      // negate because external sub transactions that receive (pos. amount) money mean the transaction is negative
      ?.let { -it }

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
        })
      }
    }
    subTransactions.filter { !newList.contains(it) }.forEach { it.destroy() }
    _subTransactionsData.value = newList
    updateSuggestions()
  }

  fun getSiblings(subTransactionEditWrapper: SubTransactionEditWrapper): List<SubTransactionEditWrapper> {
    return subTransactions - subTransactionEditWrapper
  }

  fun extractTransaction(): FullTransaction {
    return FullTransaction(
      originalData.id,
      entryDate,
      notes,
      subTransactions.map { it.extractSubTransaction() }
    )
  }
}
