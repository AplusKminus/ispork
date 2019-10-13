package app.pmsoft.ispork.util

import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.*
import kotlin.math.abs
import kotlin.math.pow
import kotlin.math.roundToLong

object CurrencyHandler {

  var locale: Locale = Locale.getDefault()
    set(value) {
      field = value
      decimalFormatSymbols = DecimalFormatSymbols(value)
      currency = Currency.getInstance(value)
    }
  private lateinit var currency: Currency
  private lateinit var decimalFormatSymbols: DecimalFormatSymbols

  fun format(
    amount: Long
  ): String {
    val negative = amount < 0
    val format = DecimalFormat.getNumberInstance(locale)
    var output = if (negative) "-" else ""
    output += format.format(abs(amount) / getScale(currency))
    output += decimalFormatSymbols.decimalSeparator
    output += String.format(
      "%0${currency.defaultFractionDigits}d",
      abs(amount) % getScale(currency)
    )
    output += " " + currency.symbol
    return output
  }

  fun parse(input: String): Long? {
    var toParse = input
    if (input.endsWith(currency.symbol)) {
      toParse = toParse.substring(
        0,
        toParse.length - currency.symbol.length
      )
    }
    toParse = toParse.trim()
    toParse = toParse.replace(
      decimalFormatSymbols.groupingSeparator.toString(),
      ""
    )
    toParse = toParse.replace(
      decimalFormatSymbols.decimalSeparator.toString(),
      ""
    )
    return try {
      toParse.toLong()
    } catch (e: NumberFormatException) {
      null
    }
  }

  private fun getScale(currency: Currency): Long {
    return when (currency.defaultFractionDigits) {
      0 -> 1
      1 -> 10
      2 -> 100
      3 -> 1000
      else -> 10.0.pow(currency.defaultFractionDigits.toDouble()).roundToLong()
    }
  }
}
