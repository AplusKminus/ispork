package app.pmsoft.ispork.data

import android.os.Parcelable
import androidx.room.Ignore
import androidx.room.Relation
import kotlinx.android.parcel.Parcelize
import java.util.*
import kotlin.math.absoluteValue

@Parcelize
class FullTransaction(
  @Ignore
  override var id: Long,
  @Ignore
  override var entryDate: Date,
  @Ignore
  override var notes: String?,
  @Relation(
    parentColumn = "id",
    entityColumn = "transaction_id",
    entity = SubTransaction::class
  )
  var subTransactions: List<FullSubTransaction>
) : Transaction(),
  Parcelable {

  constructor() : this(
    0,
    Date(),
    null,
    emptyList()
  )

  /**
   * Gets the amount that leaves (or enters) the user's budget as part of this transaction.
   *
   * @return The sum of the external sub-transactions' amounts.
   */
  fun getExternalSum(): Long {
    return subTransactions.filter { it.participant.type.internal }.map { it.amount }.sum()
  }

  /**
   * Gets the amount that is transferred internally as part of this transaction.
   *
   * @return A non-negative number.
   */
  fun getInternalSum(): Long {
    return getTotalFlowSum() - getExternalSum().absoluteValue
  }

  fun getInternalDestinationSum(): Long {
    return subTransactions.filter { it.participant.type.internal && it.amount > 0 }.map { it.amount }.sum()
  }

  /**
   * Gets the amount that flowed from all origins to all destinations as part of this transaction.
   *
   * @return A positive number (if the transaction has meaningful values, but never negative).
   */
  fun getTotalFlowSum(): Long {
    return subTransactions.filter { it.amount > 0 }.map { it.amount }.sum()
  }

  fun getOrigins(): List<Participant> {
    return subTransactions.filter { it.amount < 0 }.map { it.participant }
  }

  fun getDestinations(): List<Participant> {
    return subTransactions.filter { it.amount > 0 }.map { it.participant }
  }
}
