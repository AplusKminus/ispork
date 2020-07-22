package app.pmsoft.ispork.data

import androidx.room.*
import kotlinx.android.parcel.Parcelize

/**
 * A spending buffer is a general budgeting tool.
 *
 * [Budget allocations][BudgetAllocation] towards its [BudgetPot] are made based on several parameters:
 * - The [rate] amount determines the default amount it would be filled with at each [interval] start
 * - The [min] amount determines the minimum it should contain at each [interval] start thereby possibly increasing the
 * actual rate over the specified [rate]
 * - The [max] amount determines the maximum it would be filled to at each [interval] start thereby possibly lowering
 * the actual rate under the specified [rate]
 * - The [interval] determines when this budget pot should receive funds
 */
@Entity(
  tableName = "spending_buffers",
  foreignKeys = [
    ForeignKey(
      entity = BudgetPot::class,
      parentColumns = ["id"],
      childColumns = ["budget_pot_id"],
      onDelete = ForeignKey.CASCADE
    )
  ],
  indices = [Index("budget_pot_id")]
)
@Parcelize
open class SpendingBuffer(

  @PrimaryKey(autoGenerate = true)
  override var id: Long,

  @ColumnInfo(name = "name")
  open var name: String?,

  @ColumnInfo(name = "min")
  open var min: Amount,

  @ColumnInfo(name = "max")
  open var max: Amount,

  @ColumnInfo(name = "rate")
  open var rate: Amount,

  @Embedded(prefix = "interval_")
  open var interval: Interval,

  @ColumnInfo(name = "notes")
  open var notes: String?,

  @ColumnInfo(name = "budget_pot_id")
  open var budgetPotId: Long?
) : ISporkEntry {

  @Ignore
  constructor() : this(
    0,
    null,
    0,
    0,
    0,
    Interval(1),
    null,
    null
  )
}

@Parcelize
class FullSpendingBuffer(

  @Ignore
  override var id: Long,

  @Ignore
  override var name: String?,

  @Ignore
  override var min: Amount,

  @Ignore
  override var max: Amount,

  @Ignore
  override var rate: Amount,

  @Ignore
  override var interval: Interval,

  @Ignore
  override var notes: String?,

  @Relation(
    entity = BudgetPot::class,
    entityColumn = "id",
    parentColumn = "budget_pot_id"
  )
  var budgetPot: FullBudgetPot?
) : SpendingBuffer() {

  @Ignore
  constructor() : this(
    0,
    null,
    0,
    0,
    0,
    Interval(1),
    null,
    null
  )

  override var budgetPotId: Long?
    get() = budgetPot?.id
    set(value) {
      super.budgetPotId = value
    }
}
