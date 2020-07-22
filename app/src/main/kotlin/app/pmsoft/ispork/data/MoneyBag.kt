package app.pmsoft.ispork.data

import androidx.room.*
import kotlinx.android.parcel.Parcelize
import java.util.*

/**
 * A money bag is part of a [Participant].
 *
 * Splitting a participant into one or more money bags allows for different currencies to be held by the participant at
 * the same time.
 *
 * A money bag does not hold any money, but allows to calculate its current balance based on its [startingBalance] and
 * the sum of all [SubTransaction]s that agree in participant and currency.
 */
@Entity(
  tableName = "money_bags",
  foreignKeys = [
    ForeignKey(
      entity = Participant::class,
      parentColumns = ["id"],
      childColumns = ["participant_id"],
      onDelete = ForeignKey.CASCADE
    )
  ],
  indices = [Index("participant_id")]
)
@Parcelize
open class MoneyBag(
  @PrimaryKey(autoGenerate = true)
  override var id: Long,

  @ColumnInfo(name = "starting_balance")
  open var startingBalance: Amount,

  @ColumnInfo(name = "currency")
  open var currency: Currency,

  @ColumnInfo(name = "participant_id")
  open var participantId: Long
) : ISporkEntry {

  @Ignore
  constructor(currency: Currency) : this(
    0,
    0,
    currency,
    0
  )
}

/**
 * An owned money bag extends a [MoneyBag] by an actual participant. This is used by [FullSubTransaction].
 */
@Parcelize
class OwnedMoneyBag(
  @Ignore
  override var id: Long,
  @Ignore
  override var startingBalance: Amount,
  @Ignore
  override var currency: Currency,
  @Relation(
    entity = Participant::class,
    entityColumn = "id",
    parentColumn = "participant_id"
  )
  var participant: Participant
) : MoneyBag(currency) {

  @Ignore
  constructor(currency: Currency, participant: Participant) : this(
    0,
    0,
    currency,
    participant
  )

  override var participantId: Long
    get() = participant.id
    set(value) {}
}

/**
 * A loaded money bag extends a [MoneyBag] by the current balance. This is used by [FullParticipant].
 */
@Parcelize
class LoadedMoneyBag(
  @Ignore
  override var id: Long,
  @Ignore
  override var startingBalance: Amount,
  @Ignore
  override var currency: Currency,
  @Ignore
  override var participantId: Long,
  var bookedBalance: Amount
) : MoneyBag(currency) {

  @Ignore
  constructor(currency: Currency) : this(
    0,
    0,
    currency,
    0,
    0
  )
}
