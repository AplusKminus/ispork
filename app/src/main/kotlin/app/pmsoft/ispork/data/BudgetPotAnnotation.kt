package app.pmsoft.ispork.data

import androidx.room.*
import kotlinx.android.parcel.Parcelize

/**
 * A budget pot annotation allows monetary flows to be assigned to [BudgetPot]s and thereby to [categories][Category].
 *
 * Using several budget pot annotations on the same [SubTransaction] allows it to be split across multiple budget pots.
 * The sum of all sibling budget pot annotations' [amounts][amountInTransaction] must match the
 * [amount][SubTransaction.amountInBookedCurrency] of the parent sub transaction.
 *
 * If the budget pot and the sub transaction of this annotation have different currencies, the ratio between
 * [amountInTransaction] and [amountInBudget] is assumed to be the exchange rate.
 *
 * A notes field allows the user to specify which items of a transaction are contained in which "split".
 */
@Entity(
  foreignKeys = [
    ForeignKey(
      entity = SubTransaction::class,
      parentColumns = ["id"],
      childColumns = ["sub_transaction_id"],
      onDelete = ForeignKey.CASCADE
    ),
    ForeignKey(
      entity = BudgetPot::class,
      parentColumns = ["id"],
      childColumns = ["budget_pot_id"]
    )
  ],
  indices = [Index("sub_transaction_id"), Index("budget_pot_id")],
  tableName = "budget_pot_annotations"
)
@Parcelize
open class BudgetPotAnnotation(

  @PrimaryKey(autoGenerate = true)
  override var id: Long,

  @ColumnInfo(name = "sub_transaction_id")
  open var subTransactionId: Long,

  @ColumnInfo(name = "amount_in_transaction")
  open var amountInTransaction: Amount,

  @ColumnInfo(name = "amount_in_budget")
  open var amountInBudget: Amount,

  @ColumnInfo(name = "budget_pot_id")
  open var budgetPotId: Long?,

  @ColumnInfo(name = "notes")
  open var notes: String?
) : ISporkEntry {

  @Ignore
  constructor() : this(
    0,
    0,
    0,
    0,
    0,
    null
  )
}

@Parcelize
class FullBudgetPotAnnotation(

  @Ignore
  override var id: Long,
  @Ignore
  override var subTransactionId: Long,
  @Ignore
  override var amountInTransaction: Amount,
  @Ignore
  override var amountInBudget: Amount,
  @Relation(
    entityColumn = "id",
    parentColumn = "budget_pot_id",
    entity = BudgetPot::class
  )
  var budgetPot: FullBudgetPot?,
  @Ignore
  override var notes: String?
) : BudgetPotAnnotation() {

  @Ignore
  constructor() : this(
    0,
    0,
    0,
    0,
    null,
    null
  )

  override var budgetPotId: Long?
    get() = budgetPot?.id
    set(value) {
      super.budgetPotId = value
    }
}
