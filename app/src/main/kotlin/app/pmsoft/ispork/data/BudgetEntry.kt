package app.pmsoft.ispork.data

import androidx.room.*
import kotlinx.android.parcel.Parcelize
import java.util.*

/**
 * A budget entry defines an allocation of funds towards a certain [BudgetPot].
 *
 * The sum of all budget entries for a budget pot determines its balance.
 *
 * The currency of the entry is determined by the pot it is associated with.
 */
@Entity(
  tableName = "budget_entries",
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
open class BudgetEntry(

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
