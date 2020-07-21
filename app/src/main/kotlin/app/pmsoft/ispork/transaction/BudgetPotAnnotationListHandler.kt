package app.pmsoft.ispork.transaction

import app.pmsoft.ispork.data.FullBudgetPotAnnotation

interface BudgetPotAnnotationListHandler {

  fun selectBudgetPotFor(budgetPotAnnotationEditWrapper: BPAEditWrapper<FullBudgetPotAnnotation>)
}
