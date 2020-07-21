package app.pmsoft.ispork.data

import androidx.room.TypeConverter
import java.util.*

/**
 * To allow 'documentation by implementation' by distinguishing clearly between numbers that are monetary amounts,
 * and those that are not, like IDs.
 */
typealias Amount = Long

data class AmountWithCurrency(val amount: Amount, val currency: Currency)

class CurrencyTypeConverter {

  @TypeConverter
  fun toString(currency: Currency): String {
    return currency.currencyCode
  }

  @TypeConverter
  fun fromString(string: String): Currency {
    return Currency.getInstance(string)
  }
}
