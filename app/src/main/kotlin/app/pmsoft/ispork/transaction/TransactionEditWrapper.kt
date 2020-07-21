package app.pmsoft.ispork.transaction

import androidx.lifecycle.LiveData
import app.pmsoft.ispork.data.FullBudgetPotAnnotation
import app.pmsoft.ispork.data.FullSubTransaction
import app.pmsoft.ispork.data.FullTransactionDefinition
import app.pmsoft.ispork.data.Participant
import app.pmsoft.ispork.util.NonNullLiveData
import app.pmsoft.ispork.util.NonNullMutableLiveData
import java.util.*

/**
 * This wrapper exists to update data across the "tree" that is a [app.pmsoft.ispork.data.TransactionDefinition].
 */
class TransactionEditWrapper(
  originalData: FullTransactionDefinition
) : AbstractTransactionEditWrapper<FullTransactionDefinition, FullSubTransaction, FullBudgetPotAnnotation>(originalData) {

  private val entryDateData = NonNullMutableLiveData(originalData.entryDate)
  var entryDate: Date by entryDateData

  override val subTransactionsData: LiveData<List<SubTransactionEditWrapper>>
    get() = super.subTransactionsData as NonNullLiveData<List<SubTransactionEditWrapper>>

  override fun createSubTransactionEditWrapper(fullSubTransaction: FullSubTransaction): AbstractSubTransactionEditWrapper<FullSubTransaction, FullBudgetPotAnnotation> {
    return SubTransactionEditWrapper(this, fullSubTransaction)
  }

  override fun createNewSubTransactionForParticipant(participant: Participant): FullSubTransaction {
    return FullSubTransaction(participant).also {
      if (!participant.type.internal) {
        it.bookingDate = entryDate
      }
    }
  }

  override fun extractTransaction(): FullTransactionDefinition {
    return FullTransactionDefinition(
      originalData.id,
      entryDate,
      subTransactions.map { it.extractSubTransaction() }
    )
  }
}
