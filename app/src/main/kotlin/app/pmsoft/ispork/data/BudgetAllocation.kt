package app.pmsoft.ispork.data

import androidx.room.*
import kotlinx.android.parcel.Parcelize
import java.util.*

/**
 * A budget allocation defines an assignment of funds towards a certain [BudgetPot].
 *
 * The sum of all budget allocations for a budget pot determines its balance.
 *
 * The currency of the allocation is determined by the pot it is associated with.
 */
@Entity(
  tableName = "budget_allocations",
  foreignKeys = [
    ForeignKey(
      entity = BudgetPot::class,
      parentColumns = ["id"],
      childColumns = ["budget_pot_id"]
    )
  ],
  indices = [Index("budget_pot_id")]
)
@Parcelize
open class BudgetAllocation(

  @PrimaryKey(autoGenerate = true)
  override var id: Long,

  @ColumnInfo(name = "amount")
  open var amount: Amount,

  @ColumnInfo(name = "entry_date")
  open var entryDate: Date,

  @ColumnInfo(name = "budget_pot_id")
  open var budgetPotId: Long
) : ISporkEntry {

  @Ignore
  constructor() : this(
    0,
    0,
    Date(),
    0
  )
}
