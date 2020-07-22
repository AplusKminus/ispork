package app.pmsoft.ispork.util

import app.pmsoft.ispork.data.Amount
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.*
import kotlin.math.abs
import kotlin.math.pow
import kotlin.math.roundToLong

class CurrencyHandler private constructor(private val currency: Currency) {

  companion object {

    private val instances = mutableMapOf<Currency, CurrencyHandler>()

    fun getInstanceFor(currency: Currency): CurrencyHandler {
      return instances.getOrPut(currency, { CurrencyHandler(currency) })
    }
  }

  private fun locale(): Locale = LocaleHandler.locale
  private fun decimalFormatSymbols(): DecimalFormatSymbols = DecimalFormatSymbols(locale())

  fun format(
    amount: Amount
  ): String {
    val negative = amount < 0
    val format = DecimalFormat.getNumberInstance(locale())
    var output = if (negative) "-" else ""
    output += format.format(abs(amount) / getScale(currency))
    output += decimalFormatSymbols().decimalSeparator
    output += String.format(
      "%0${currency.defaultFractionDigits}d",
      abs(amount) % getScale(currency)
    )
    output += " " + currency.symbol
    return output
  }

  fun parse(input: String): Amount? {
    var toParse = ""
    for (char in input) {
      if (char in arrayOf('0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '-')) {
        toParse += char
      }
    }
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
      else -> 10.0.pow(currency.defaultFractionDigits.toDouble())
        .roundToLong()
    }
  }
}
