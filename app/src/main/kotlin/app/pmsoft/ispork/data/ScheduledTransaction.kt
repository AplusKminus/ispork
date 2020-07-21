package app.pmsoft.ispork.data

import androidx.room.*
import kotlinx.android.parcel.Parcelize

/**
 * A scheduled transaction stores all necessary data to determine when a transaction is to be booked, what data it
 * contains and which budgets and participants are involved.
 *
 * The contained [TransactionDefinition] is unique and only used to define future occurrences (its
 * [entry date][TransactionDefinition.entryDate] is null). It will be copied upon booking. This is done so that editing
 * the template for future occurrences does not modify the past.
 *
 * One or more [ScheduledRhythm]s may be associated with a scheduled transaction. These allow to define precisely when
 * a booking for the transaction should be created.
 *
 * Each scheduled transaction has a [name] to allow easy identification by the user.
 */
@Entity(
  tableName = "scheduled_transactions",
  foreignKeys = [
    ForeignKey(
      entity = TransactionDefinition::class,
      parentColumns = ["id"],
      childColumns = ["transaction_definition_id"]
    )
  ],
  indices = [Index("transaction_definition_id")]
)
@Parcelize
open class ScheduledTransaction(

  @PrimaryKey(autoGenerate = true)
  override var id: Long,

  @ColumnInfo(name = "name")
  open var name: String,

  /**
   * This column identifies the one [TransactionDefinition] used for defining the data of this scheduled transaction.
   *
   * It should not be confused with [TransactionDefinition.sourceScheduledTransactionId] which is used only on booked
   * transactions created from a scheduled transaction in order to link back to their source.
   */
  @ColumnInfo(name = "transaction_definition_id")
  open var transactionDefinitionId: Long
) : ISporkEntry {

  @Ignore
  constructor() : this(
    0,
    "",
    0
  )
}

@Parcelize
class FullScheduledTransaction(

  @Ignore
  override var id: Long,

  @Ignore
  override var name: String,

  @Relation(
    entity = ScheduledRhythm::class,
    entityColumn = "scheduled_transaction_id",
    parentColumn = "id"
  )
  var rhythms: List<ScheduledRhythm>,

  @Relation(
    entity = TransactionDefinition::class,
    entityColumn = "id",
    parentColumn = "transaction_definition_id"
  )
  var transactionDefinition: TransactionDefinition

) : ScheduledTransaction() {

  @Ignore
  constructor(transactionDefinition: TransactionDefinition) : this(
    0,
    "",
    emptyList(),
    transactionDefinition
  )

  override var transactionDefinitionId: Long
    get() = transactionDefinition.id
    set(_) {}
}
