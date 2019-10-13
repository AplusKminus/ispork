package app.pmsoft.ispork.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import java.util.*

@Dao
interface SubTransactionDao {

  @Query("UPDATE subTransaction SET amount = :amount, transaction_id = :transactionId, participant_id = :participantId, booking_date = :bookingDate, notes = :notes WHERE id IS :id")
  fun update(
    id: Long,
    amount: Long,
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

  @Query("DELETE FROM subTransaction WHERE transaction_id = :transactionId AND id NOT IN (:usedIds)")
  fun deleteUnused(
    transactionId: Long,
    usedIds: List<Long>
  )

  @Query("DELETE FROM subTransaction WHERE id IS :id")
  fun deleteById(id: Long)
}
