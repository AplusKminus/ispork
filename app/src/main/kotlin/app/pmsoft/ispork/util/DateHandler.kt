package app.pmsoft.ispork.util

import android.icu.text.DateFormat
import android.icu.util.Calendar
import android.os.Build
import java.util.*

object DateHandler {

  var locale: Locale = Locale.getDefault()

  fun format(date: Date?): String {
    if (date == null) {
      return ""
    }
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
      DateFormat.getDateInstance(
        Calendar.getInstance(locale),
        DateFormat.DEFAULT
      ).format(date)
    } else {
      java.text.DateFormat.getDateInstance(
        java.text.DateFormat.DEFAULT,
        locale
      ).format(date)
    }
  }

  fun create(
    year: Int,
    month: Int,
    dayOfMonth: Int
  ): Date {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
      val calendar = Calendar.getInstance(locale)
      calendar.set(
        year,
        month,
        dayOfMonth
      )
      calendar.time
    } else {
      val calendar = java.util.Calendar.getInstance(locale)
      calendar.set(
        year,
        month,
        dayOfMonth
      )
      calendar.time
    }
  }
}
