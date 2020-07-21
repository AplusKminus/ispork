package app.pmsoft.ispork.transaction.scheduled

import app.pmsoft.ispork.data.FullBudgetPotAnnotationTemplate
import app.pmsoft.ispork.data.FullSubTransactionTemplate
import app.pmsoft.ispork.transaction.AbstractBudgetPotAnnotationEditWrapper
import app.pmsoft.ispork.transaction.AbstractSubTransactionEditWrapper

class SubTransactionTemplateEditWrapper(
  private val transactionEditWrapper: ScheduledTransactionEditWrapper,
  originalData: FullSubTransactionTemplate
) : AbstractSubTransactionEditWrapper<FullSubTransactionTemplate, FullBudgetPotAnnotationTemplate>(
  transactionEditWrapper,
  originalData
) {

  override val id: Long
    get() = originalData.id

  override fun createBudgetPotAnnotationWrapper(fullBudgetPotAnnotation: FullBudgetPotAnnotationTemplate): AbstractBudgetPotAnnotationEditWrapper<FullBudgetPotAnnotationTemplate> {
    return BudgetPotAnnotationTemplateEditWrapper(this, fullBudgetPotAnnotation)
  }

  override fun createNewBudgetPotAnnotation(): FullBudgetPotAnnotationTemplate {
    return FullBudgetPotAnnotationTemplate()
  }

  override fun extractSubTransaction(): FullSubTransactionTemplate {
    val actualBudgetPotAnnotations = budgetPotAnnotations.map { it.extractBudgetPotAnnotation() }
    if (actualBudgetPotAnnotations.size == 1) {
      actualBudgetPotAnnotations[0].amount = amount ?: suggestedAmount ?: 0L
    }
    return FullSubTransactionTemplate(
      originalData.id,
      amount ?: suggestedAmount ?: 0L,
      transactionEditWrapper.originalData.id,
      participant,
      actualBudgetPotAnnotations,
      notes
    )
  }
}
