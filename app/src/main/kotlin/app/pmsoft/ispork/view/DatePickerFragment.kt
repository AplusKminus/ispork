package app.pmsoft.ispork.view

import android.app.DatePickerDialog
import android.app.Dialog
import android.icu.util.Calendar
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import java.util.*

class DatePickerFragment : DialogFragment() {

  private var date: Date = Date()
  private lateinit var listener: DatePickerDialog.OnDateSetListener

  override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
    val year: Int
    val month: Int
    val day: Int

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
      val c = Calendar.getInstance()
      c.time = date
      year = c.get(Calendar.YEAR)
      month = c.get(Calendar.MONTH)
      day = c.get(Calendar.DAY_OF_MONTH)
    } else {
      val c = java.util.Calendar.getInstance()
      c.time = date
      year = c.get(java.util.Calendar.YEAR)
      month = c.get(java.util.Calendar.MONTH)
      day = c.get(java.util.Calendar.DAY_OF_MONTH)
    }

    return DatePickerDialog(
      activity!!,
      listener,
      year,
      month,
      day
    )
  }

  fun setDate(date: Date) {
    this.date = date
  }

  fun setResultHandler(listener: DatePickerDialog.OnDateSetListener) {
    this.listener = listener
  }
}
