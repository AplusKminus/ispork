package app.pmsoft.ispork.transaction

import app.pmsoft.ispork.data.FullBudgetPotAnnotation

class BudgetPotAnnotationEditWrapper(
  subTransactionEditWrapper: STEditWrapper<*, FullBudgetPotAnnotation>,
  originalData: FullBudgetPotAnnotation
) : AbstractBudgetPotAnnotationEditWrapper<FullBudgetPotAnnotation>(subTransactionEditWrapper, originalData) {

  override fun extractBudgetPotAnnotation(): FullBudgetPotAnnotation {
    return FullBudgetPotAnnotation(
      originalData.id,
      subTransactionEditWrapper.id,
      amount ?: suggestedAmount ?: 0L,
      budgetPot,
      notes
    )
  }
}
