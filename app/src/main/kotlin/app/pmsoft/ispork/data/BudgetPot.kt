package app.pmsoft.ispork.data

import androidx.room.*
import kotlinx.android.parcel.Parcelize
import java.util.*

/**
 * A budget pot is the central tool of budget allocation.
 *
 * All funds that are spent through a [transaction][TransactionDefinition] have to be allocated through a budget pot.
 * Funds are allocated by creating [budget entries][BudgetEntry].
 *
 * A budget pot has an associated currency that can never be changed. If a different currency is required for budgeting
 * in a certain [Category], it is no problem to create a new budget pot and transfer the funds using an assumed exchange
 * rate.
 */
@Entity(
  tableName = "budget_pots",
  foreignKeys = [
    ForeignKey(
      entity = Category::class,
      parentColumns = ["id"],
      childColumns = ["category_id"]
    )
  ],
  indices = [Index("category_id")]
)
@Parcelize
open class BudgetPot(

  @PrimaryKey(autoGenerate = true)
  override var id: Long,

  @ColumnInfo(name = "priority")
  open var priority: Int,

  @ColumnInfo(name = "currency")
  open var currency: Currency,

  @ColumnInfo(name = "category_id")
  open var categoryId: Long?
) : ISporkEntry {

  @Ignore
  constructor(currency: Currency) : this(
    0,
    0,
    currency,
    null
  )
}

@Parcelize
class FullBudgetPot(

  @Ignore
  override var id: Long,

  @Ignore
  override var priority: Int,

  @Ignore
  override var currency: Currency,

  @Relation(
    parentColumn = "category_id",
    entityColumn = "id",
    entity = Category::class
  )
  var category: Category?

) : BudgetPot(currency) {

  @Ignore
  constructor(currency: Currency) : this(
    0,
    0,
    currency,
    null
  )
}
