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

  fun Date.plusDays(days: Int): Date {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
      val calendar = Calendar.getInstance(locale)
      calendar.time = this
      calendar.add(Calendar.DATE, days)
      calendar.time
    } else {
      val calendar = java.util.Calendar.getInstance(locale)
      calendar.time = this
      calendar.add(java.util.Calendar.DATE, days)
      calendar.time
    }
  }

  fun Date.plusMonths(months: Int): Date {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
      val calendar = Calendar.getInstance(locale)
      calendar.time = this
      calendar.add(Calendar.MONTH, months)
      calendar.time
    } else {
      val calendar = java.util.Calendar.getInstance(locale)
      calendar.time = this
      calendar.add(java.util.Calendar.MONTH, months)
      calendar.time
    }
  }

  fun Date.plusYears(years: Int): Date {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
      val calendar = Calendar.getInstance(locale)
      calendar.time = this
      calendar.add(Calendar.YEAR, years)
      calendar.time
    } else {
      val calendar = java.util.Calendar.getInstance(locale)
      calendar.time = this
      calendar.add(java.util.Calendar.YEAR, years)
      calendar.time
    }
  }
}
