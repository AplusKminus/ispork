package app.pmsoft.ispork.data

import android.os.Parcelable
import androidx.room.*
import app.pmsoft.ispork.data.Participant.Type
import kotlinx.android.parcel.Parcelize
import java.util.*

/**
 * A participant is either a source or a sink of money in a [SubTransaction].
 *
 * There are three [types][Type] of participants:
 * 1. Accounts ([Participant.Type.ACCOUNT]) represent accounts or other "money pots" of the user, such as:
 *    - credit cards
 *    - wallets with cash
 *    - piggy banks
 * 2. Persons ([Participant.Type.PERSON]) represent a type of account that is handled differently by the app. Money in a
 * "person" account symbolizes a person to person "social" loan to that person and is still considered to be funds of
 * the user. A negative balance of a person account is considered money that the user owes to that person.
 * 3. Payees ([Participant.Type.PAYEE]) represent external payees or payers (depending on the direction of flow).
 *
 * Transactions with a payee affect the user's budget. Therefore a sub transaction that has a payee set as participant
 * must have [BudgetPotAnnotation]s to assign the monetary flow to one or more [BudgetPot]s.
 *
 * A non-payee participant has one or more [money bags][MoneyBag], which allow different currencies to be managed for
 * the same participant.
 *
 * Participants additionally have the following properties:
 * - A [flag][multiCurrency] to state whether multiple currencies are supported by this participant.
 * - A [defaultCurrency] to use if only one is allowed or no specific currency is put in by the user when creating a sub
 * transaction.
 * - A [flag][bookingDateIsEntryDate] to state whether the booking date for this participant should always match the
 * entry date (useful for cash).
 */
@Entity(
  tableName = "participants"
)
@Parcelize
@TypeConverters(Type.Converter::class)
open class Participant(
  @ColumnInfo(name = "type")
  open val type: Type,
  @PrimaryKey(autoGenerate = true)
  override var id: Long,
  @ColumnInfo(name = "name")
  open var name: String,
  @ColumnInfo(name = "multi_currency")
  open var multiCurrency: Boolean,
  /**
   * The default currency is used for:
   * - to suggest a currency to the user when selecting a participant
   * - to use when only one currency is supported
   */
  @ColumnInfo(name = "default_currency")
  open var defaultCurrency: Currency?,
  @ColumnInfo(name = "booking_date_is_entry_date")
  open var bookingDateIsEntryDate: Boolean
) : ISporkEntry {

  /**
   * See [Participant].
   */
  @Parcelize
  enum class Type(val internal: Boolean) : Parcelable {
    ACCOUNT(true), PERSON(true), PAYEE(false);

    class Converter {

      @TypeConverter
      fun fromString(value: String?): Type? {
        if (value == null) {
          return null
        }
        try {
          return valueOf(value)
        } catch (e: IllegalArgumentException) {
          e.printStackTrace()
        }
        return null
      }

      @TypeConverter
      fun toString(type: Type?): String? = type?.name
    }
  }

  @Ignore
  constructor(type: Type) : this(
    type,
    0,
    "",
    true,
    null,
    false
  )
}

@Parcelize
@TypeConverters(Type.Converter::class)
class FullParticipant(
  @Ignore
  override val type: Type,
  @Ignore
  override var id: Long,
  @Ignore
  override var name: String,
  @Ignore
  override var multiCurrency: Boolean,
  @Ignore
  override var defaultCurrency: Currency?,
  @Ignore
  override var bookingDateIsEntryDate: Boolean,
  @Relation(
    entity = MoneyBag::class,
    parentColumn = "id",
    entityColumn = "participant_id"
  )
  var moneyBags: List<MoneyBag>
) : Participant(type) {

  @Ignore
  constructor(type: Type) : this(
    type,
    0,
    "",
    true,
    null,
    false,
    emptyList()
  )
}
