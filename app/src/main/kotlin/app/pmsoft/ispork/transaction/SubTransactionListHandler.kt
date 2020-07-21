package app.pmsoft.ispork.transaction

import android.app.DatePickerDialog
import app.pmsoft.ispork.data.BaseBudgetPotAnnotation
import app.pmsoft.ispork.data.BaseSubTransaction
import java.util.*

interface SubTransactionListHandler<ST : BaseSubTransaction<BPA>, BPA : BaseBudgetPotAnnotation> {

  fun startCategoryPicking(
    subTransactionEditWrapper: STEditWrapper<ST, BPA>,
    categoryAnnotationIndex: Int?
  )

  fun showDateSelector(
    startDate: Date,
    callback: DatePickerDialog.OnDateSetListener
  )
}
