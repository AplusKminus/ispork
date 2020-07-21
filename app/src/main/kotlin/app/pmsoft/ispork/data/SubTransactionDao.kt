package app.pmsoft.ispork.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import java.util.*

@Dao
interface SubTransactionDao {

  @Query("UPDATE sub_transactions SET amount_in_booked_currency = :amountInBookedCurrency, amount_in_base_currency = :amountInBaseCurrency, transaction_definition_id = :transactionId, participant_id = :participantId, booking_date = :bookingDate, notes = :notes WHERE id IS :id")
  fun update(
    id: Long,
    amountInBookedCurrency: Amount,
    amountInBaseCurrency: Amount,
    transactionId: Long,
    participantId: Long,
    bookingDate: Date?,
    notes: String?
  )

  @Insert
  fun insert(subTransaction: SubTransaction): Long

  @Insert
  fun insertAll(vararg subTransactions: SubTransaction)

  @Delete
  fun delete(subTransaction: SubTransaction)

  @Query("DELETE FROM sub_transactions WHERE transaction_definition_id = :transactionId AND id NOT IN (:usedIds)")
  fun deleteUnused(
    transactionId: Long,
    usedIds: List<Long>
  )

  @Query("DELETE FROM sub_transactions WHERE id IS :id")
  fun deleteById(id: Long)
}
