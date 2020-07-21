package app.pmsoft.ispork.transaction.scheduled

import app.pmsoft.ispork.data.FullScheduledTransaction
import app.pmsoft.ispork.data.Participant
import app.pmsoft.ispork.data.ScheduledRhythm
import app.pmsoft.ispork.transaction.AbstractSubTransactionEditWrapper
import app.pmsoft.ispork.transaction.AbstractTransactionEditWrapper
import app.pmsoft.ispork.util.NonNullMutableLiveData

/**
 * This wrapper exists to update data across the "tree" that is a [app.pmsoft.ispork.data.TransactionDefinition].
 */
class ScheduledTransactionEditWrapper(
  originalData: FullScheduledTransaction
) : AbstractTransactionEditWrapper<FullScheduledTransaction, FullSubTransactionTemplate, FullBudgetPotAnnotationTemplate>(
  originalData
) {

  val nameData = NonNullMutableLiveData(originalData.name)
  val name: String by nameData

  val rhythmData = NonNullMutableLiveData(originalData.rhythms)
  val rhythms: List<ScheduledRhythm> by rhythmData

  override fun createSubTransactionEditWrapper(fullSubTransaction: FullSubTransactionTemplate)
    : AbstractSubTransactionEditWrapper<FullSubTransactionTemplate, FullBudgetPotAnnotationTemplate> {
    return SubTransactionTemplateEditWrapper(this, fullSubTransaction)
  }

  override fun createNewSubTransactionForParticipant(participant: Participant): FullSubTransactionTemplate {
    return FullSubTransactionTemplate(participant)
  }

  override fun extractTransaction(): FullScheduledTransaction {
    return FullScheduledTransaction(
      originalData.id,
      name,
      rhythms,
      subTransactions.map { it.extractSubTransaction() }
    )
  }
}
