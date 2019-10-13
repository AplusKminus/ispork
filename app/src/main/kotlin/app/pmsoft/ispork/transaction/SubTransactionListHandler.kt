package app.pmsoft.ispork.transaction

import android.app.DatePickerDialog
import java.util.*

interface SubTransactionListHandler {

  fun startCategoryPicking(
    subTransactionEditWrapper: SubTransactionEditWrapper,
    categoryAnnotationIndex: Int?
  )

  fun showDateSelector(
    startDate: Date,
    callback: DatePickerDialog.OnDateSetListener
  )
}
