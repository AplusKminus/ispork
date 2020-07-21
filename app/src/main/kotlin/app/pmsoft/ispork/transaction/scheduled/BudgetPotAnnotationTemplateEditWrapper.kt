package app.pmsoft.ispork.transaction.scheduled

import app.pmsoft.ispork.data.FullBudgetPotAnnotationTemplate
import app.pmsoft.ispork.transaction.AbstractBudgetPotAnnotationEditWrapper

class BudgetPotAnnotationTemplateEditWrapper(
  subTransactionEditWrapper: SubTransactionTemplateEditWrapper,
  originalData: FullBudgetPotAnnotationTemplate
) : AbstractBudgetPotAnnotationEditWrapper<FullBudgetPotAnnotationTemplate>(subTransactionEditWrapper, originalData) {

  override fun extractBudgetPotAnnotation(): FullBudgetPotAnnotationTemplate {
    return FullBudgetPotAnnotationTemplate(
      originalData.id,
      subTransactionEditWrapper.originalData.id,
      amount ?: suggestedAmount ?: 0L,
      budgetPot,
      notes
    )
  }
}
