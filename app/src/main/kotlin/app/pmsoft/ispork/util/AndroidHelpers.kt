package app.pmsoft.ispork.util

import android.content.res.Resources
import android.util.TypedValue

fun Int.dpToPx(r: Resources): Int = TypedValue.applyDimension(
  TypedValue.COMPLEX_UNIT_DIP,
  this.toFloat(),
  r.displayMetrics
).toInt()
