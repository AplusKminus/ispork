package app.pmsoft.ispork.transaction

import app.pmsoft.ispork.data.BaseBudgetPotAnnotation
import app.pmsoft.ispork.data.BaseSubTransaction
import app.pmsoft.ispork.data.FullBaseTransaction

interface TEditWrapper<T : FullBaseTransaction<ST, BPA>, ST : BaseSubTransaction<BPA>, BPA : BaseBudgetPotAnnotation> {

  val subTransactions: List<STEditWrapper<ST, BPA>>

  fun getSiblingSubTransactions(target: STEditWrapper<ST, BPA>): List<STEditWrapper<ST, BPA>>

  fun extractTransaction(): T
}
