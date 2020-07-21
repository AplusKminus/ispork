package app.pmsoft.ispork.util

import android.icu.text.PluralFormat
import android.icu.text.PluralRules
import android.os.Build
import java.util.*

object StringHelpers {

  var locale: Locale = Locale.getDefault()

  fun List<String>.joinToTextList(
    and: String,
    separator: String = ", "
  ): String {
    if (this.size == 1)
      return this[0]
    val stringBuilder = StringBuilder(this.size * (this[0].length + 2) + and.length)
    this.forEachIndexed { index, item ->
      stringBuilder.append(item)
      when (index) {
        lastIndex - 1 -> {
          stringBuilder.append(' ')
            .append(and)
            .append(' ')
        }
        lastIndex -> {
          // do not append anything
        }
        else -> {
          stringBuilder.append(separator)
        }
      }
    }
    return stringBuilder.toString()
  }

  fun toOrdinal(int: Int): String {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
      val format = PluralFormat(locale, PluralRules.PluralType.ORDINAL)
      format.format(int)
    } else {
      "$int."
    }
  }
}
