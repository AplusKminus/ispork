package app.pmsoft.ispork.data

import androidx.room.*
import kotlinx.android.parcel.Parcelize
import java.util.*

/**
 * A sub transaction defines all money flows that are part of the same [transaction][TransactionDefinition].
 *
 * Each sub transaction is associated with exactly one [Participant]. The sum of the amounts of all sub transactions
 * that have the same parent transaction definition must equal zero.
 *
 * One sub transaction per transaction definition may contain an external participant. If it does it must also contain
 * one or more [BudgetPotAnnotation]s. These define which [BudgetPot]s and thereby which [categories][Category] the
 * money flow of that sub transaction is assigned to (it may be split). The sum of all budget pot annotations' amounts
 * of one sub transaction must equal its own amount.
 *
 * The notes of a sub transaction allow to specify what the payment is/was for. If a sub transaction has budget pot
 * annotations, its notes will be ignored and composed of the individual budget pot annotations' notes in a comma
 * separated list.
 */
@Entity(
  tableName = "sub_transactions",
  foreignKeys = [
    ForeignKey(
      entity = TransactionDefinition::class,
      parentColumns = ["id"],
      childColumns = ["transaction_definition_id"],
      onDelete = ForeignKey.CASCADE
    ),
    ForeignKey(
      entity = MoneyBag::class,
      parentColumns = ["id"],
      childColumns = ["money_bag_id"]
    )
  ],
  indices = [Index("transaction_definition_id"), Index("money_bag_id")]
)
@Parcelize
open class SubTransaction(

  @PrimaryKey(autoGenerate = true)
  override var id: Long,

  @ColumnInfo(name = "transaction_definition_id")
  open var transactionDefinitionId: Long,

  @ColumnInfo(name = "amount_in_booked_currency")
  open var amountInBookedCurrency: Amount,

  @ColumnInfo(name = "amount_in_base_currency")
  open var amountInBaseCurrency: Amount,

  @ColumnInfo(name = "money_bag_id")
  open var moneyBagId: Long,

  @ColumnInfo(name = "booking_date")
  open var bookingDate: Date?,

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
    null,
    null
  )
}

@Parcelize
class FullSubTransaction(
  @Ignore
  override var id: Long,
  @Ignore
  override var amountInBookedCurrency: Amount,
  @Ignore
  override var amountInBaseCurrency: Amount,

  /**
   * This field is not replaced with a full object as the link is made through
   * [FullTransactionDefinition.subTransactions].
   */
  @Ignore
  override var transactionDefinitionId: Long,
  @Relation(
    parentColumn = "money_bag_id",
    entityColumn = "id",
    entity = MoneyBag::class
  )
  var moneyBag: OwnedMoneyBag,
  @Relation(
    parentColumn = "id",
    entityColumn = "sub_transaction_id",
    entity = BudgetPotAnnotation::class
  )
  var budgetPotAnnotations: List<FullBudgetPotAnnotation>,
  @Ignore
  override var bookingDate: Date?,
  @Ignore
  override var notes: String?
) : SubTransaction() {

  @Ignore
  constructor(moneyBag: OwnedMoneyBag) : this(
    0,
    0,
    0,
    0,
    moneyBag,
    emptyList(),
    null,
    null
  )

  override var moneyBagId: Long
    get() = moneyBag.id
    set(value) {
      super.moneyBagId = value
    }
}
