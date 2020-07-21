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

  /**
   * Prints a given number in the current currency.
   *
   * The output will consist of:
   * - an optional minus sign
   * - a string of digits of at least length 1 ("0") including any grouping separators
   * - a decimal separator
   * - a string of n digits where n is the default number of fraction digits for this currency
   * - a space
   * - the currency symbol
   */
  fun format(
    amount: Long
  ): String {
    val negative = amount < 0
    val format = DecimalFormat.getNumberInstance(locale)
    var output = if (negative) "-" else ""
    // digits before separator
    output += format.format(abs(amount) / getScale(currency))
    // separator
    output += decimalFormatSymbols.decimalSeparator
    // digits after separator
    output += String.format(
      "%0${currency.defaultFractionDigits}d",
      abs(amount) % getScale(currency)
    )
    // append symbol
    output += " " + currency.symbol
    return output
  }

  /**
   * Parses a currency string as produced by [format].
   *
   * Strips the currency symbol and all separators before parsing. Assumes the input has the correct scale.
   */
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
