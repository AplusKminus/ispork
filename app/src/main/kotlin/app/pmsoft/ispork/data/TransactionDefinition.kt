package app.pmsoft.ispork.data

import androidx.room.*
import kotlinx.android.parcel.Parcelize
import java.util.*
import kotlin.math.absoluteValue

/**
 * A transaction definition is the handle for all data defining a transaction.
 *
 * It contains [sub transactions][SubTransaction] that each contain a [Participant] and an amount. This defines which
 * participant receives which amount of money.
 * One (or no) sub transaction contains one or more [budget flows][BudgetFlow] which in turn define
 * which [BudgetPot] (and thereby indirectly which [Category]) the money is taken from (or put into).
 *
 * Each [ScheduledTransaction] contains a transaction definition for defining how future payments should be booked.
 * At the moment of booking, the transaction definition of the scheduled transaction is copied and the scheduled
 * transaction is set as the [source][sourceScheduledTransactionId].
 *
 * A transaction definition is considered to be booked when its [entryDate] is not null.
 *
 * A transaction definition does not have its own notes field. In the UI the notes will be composed of the individual
 * sub transactions' notes.
 */
@Entity(
  tableName = "transaction_definitions",
  foreignKeys = [
    ForeignKey(
      entity = ScheduledTransaction::class,
      parentColumns = ["id"],
      childColumns = ["source_scheduled_transaction_id"]
    )
  ],
  indices = [Index("source_scheduled_transaction_id")]
)
@Parcelize
open class TransactionDefinition(
  @PrimaryKey(autoGenerate = true)
  override var id: Long,

  @ColumnInfo(name = "entry_date")
  open var entryDate: Date?,

  /**
   * This column identifies the [ScheduledTransaction] that created this.
   *
   * It is only set when this is a booked transaction (requires [entryDate] to be not null).
   *
   * It should not be confused with [ScheduledTransaction.transactionDefinitionId] which is used to define a non booked
   * transaction template that is copied when booked.
   */
  @ColumnInfo(name = "source_scheduled_transaction_id")
  open var sourceScheduledTransactionId: Long?
) : ISporkEntry {

  @Ignore
  constructor() : this(
    0,
    null,
    null
  )
}

@Parcelize
class FullTransactionDefinition(
  @Ignore
  override var id: Long,

  @Ignore
  override var entryDate: Date?,

  @Ignore
  override var sourceScheduledTransactionId: Long?,

  @Relation(
    parentColumn = "id",
    entityColumn = "transaction_definition_id",
    entity = SubTransaction::class
  )
  var subTransactions: List<FullSubTransaction>
) : TransactionDefinition() {

  constructor() : this(
    0,
    null,
    null,
    emptyList()
  )

  /**
   * Gets the amount that leaves (or enters) the user's budget as part of this transaction.
   *
   * @return The sum of the external sub-transactions' amounts.
   */
  fun getExternalSum(): Amount {
    return subTransactions.filter { it.moneyBag.participant.type.internal }
      .map { it.amountInBaseCurrency }
      .sum()
  }

  /**
   * Gets the amount that is transferred internally as part of this transaction.
   *
   * @return A non-negative number.
   */
  fun getInternalSum(): Amount {
    return getTotalFlowSum() - getExternalSum().absoluteValue
  }

  /**
   * Gets the amount that flowed from all origins to all destinations as part of this transaction.
   *
   * @return A positive number (if the transaction has meaningful values, but never negative).
   */
  fun getTotalFlowSum(): Amount {
    return subTransactions.filter { it.amountInBaseCurrency > 0 }
      .map { it.amountInBaseCurrency }
      .sum()
  }

  /**
   * Gets the participants for which the flow is negative, i.e. where the money comes from.
   */
  fun getOrigins(): List<Participant> {
    return subTransactions.filter { it.amountInBaseCurrency < 0 }
      .map { it.moneyBag.participant }
  }

  /**
   * Gets the participants for which the flow is positive, i.e. where the money goes to.
   */
  fun getDestinations(): List<Participant> {
    return subTransactions.filter { it.amountInBaseCurrency > 0 }
      .map { it.moneyBag.participant }
  }
}
