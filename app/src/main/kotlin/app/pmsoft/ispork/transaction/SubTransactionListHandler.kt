package app.pmsoft.ispork.transaction

import android.app.DatePickerDialog
import java.util.*

interface SubTransactionListHandler {

  fun startBudgetFlowPicking(
    subTransactionEditWrapper: SubTransactionEditWrapper,
    budgetFlowIndex: Int?
  )

  fun showDateSelector(
    startDate: Date,
    callback: DatePickerDialog.OnDateSetListener
  )
}
