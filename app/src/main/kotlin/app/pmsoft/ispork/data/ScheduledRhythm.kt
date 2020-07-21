package app.pmsoft.ispork.data

import androidx.room.*
import kotlinx.android.parcel.Parcelize
import java.util.*

/**
 * A scheduled rhythm defines the pattern of dates on which a transaction is booked from a [ScheduledTransaction].
 *
 * A rhythm has a base [interval]. Depending on which base [unit][Interval.Unit] was selected, different fields define
 * the specific rhythm relative to it.
 *
 * 1. A daily rhythm has no settings.
 *
 * 2. A weekly rhythm allows:
 *    - A 7-way bit-flag to specify for each weekday whether to include it in the rhythm or not
 *
 * 3. A monthly rhythm allows:
 *    - Specifying the X. of a month counting from the start or end
 *    - Specifying the X. (Monday|Tuesday|...) of a month counting from the start or end
 *
 * 4. A yearly rhythm allows:
 *    - the same specifications as a monthly rhythm
 *    - A 12-way bit-flag to specify each month the rhythm applies to
 *
 * A scheduled rhythm has both a [startDate] and an [endDate]. The latter is optional to signify indefinitely planned
 * transactions.
 */
@Entity(
  tableName = "scheduled_rhythms",
  foreignKeys = [
    ForeignKey(
      entity = ScheduledTransaction::class,
      parentColumns = ["id"],
      childColumns = ["scheduled_transaction_id"],
      onDelete = ForeignKey.CASCADE
    )
  ],
  indices = [Index("scheduled_transaction_id")]
)
@Parcelize
open class ScheduledRhythm(
  @PrimaryKey(autoGenerate = true)
  override var id: Long,

  @ColumnInfo(name = "scheduled_transaction_id")
  open var scheduledTransactionId: Long,

  @ColumnInfo(name = "start_date")
  open var startDate: Date,

  /**
   * Defines the last (inclusive) day this transaction could be scheduled on.
   *
   * If left as null, the transaction is scheduled indefinitely.
   */
  @ColumnInfo(name = "end_date")
  open var endDate: Date?,

  /**
   * Determines the base rhythm.
   */
  @Embedded(prefix = "interval_")
  open var interval: Interval,

  /**
   * Only available for weekly and yearly rhythms.
   *
   * For weekly rhythms it defines which weekdays are active, LSB first starting with Monday.
   *
   * For yearly rhythms it defines which months are active, LSB first starting with January.
   */
  @ColumnInfo(name = "pattern")
  open var pattern: Int,

  /**
   * Only available for monthly and yearly rhythms.
   *
   * For both it defines the X of "(X. day|X. Monday|X. Tuesday|...) of the month." Negative numbers count from the
   * Uses 0-based indexing for positive numbers, i.e. 0 = first, -1 = last.
   */
  @ColumnInfo(name = "day_offset")
  open var dayOffset: Int,

  /**
   * Only available for monthly and yearly rhythms.
   *
   * For both it defines the DDD of "X. DDD of the month."
   */
  @ColumnInfo(name = "offset_type")
  open var offsetType: OffsetType,

  /**
   * Only available for monthly and yearly rhythms.
   *
   * For both it instructs the next occurrence calculation to shift the date forward in time until the next workday is
   * hit.
   */
  @ColumnInfo(name = "wait_for_workday")
  open var waitForWorkday: Boolean
) : ISporkEntry {

  @Ignore
  constructor() : this(
    0,
    0,
    Date(),
    null,
    Interval(1),
    0,
    0,
    OffsetType.DAY,
    false
  )
}
