package app.pmsoft.ispork.transaction

import androidx.lifecycle.MutableLiveData
import app.pmsoft.ispork.data.FullBudgetPotAnnotation
import app.pmsoft.ispork.data.FullSubTransaction
import java.util.*

class SubTransactionEditWrapper(
  private val transactionEditWrapper: TransactionEditWrapper,
  originalData: FullSubTransaction
) : AbstractSubTransactionEditWrapper<FullSubTransaction, FullBudgetPotAnnotation>(
  transactionEditWrapper,
  originalData
) {

  override val id: Long
    get() = originalData.id

  private val bookingDateData = MutableLiveData(originalData.bookingDate)
  var bookingDate: Date? by bookingDateData

  override fun createBudgetPotAnnotationWrapper(fullBudgetPotAnnotation: FullBudgetPotAnnotation): AbstractBudgetPotAnnotationEditWrapper<FullBudgetPotAnnotation> {
    return BudgetPotAnnotationEditWrapper(this, fullBudgetPotAnnotation)
  }

  override fun createNewBudgetPotAnnotation(): FullBudgetPotAnnotation {
    return FullBudgetPotAnnotation()
  }

  override fun extractSubTransaction(): FullSubTransaction {
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
      actualBudgetPotAnnotations,
      notes
    )
  }
}
