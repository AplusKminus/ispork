package app.pmsoft.ispork.data

import androidx.room.*
import java.util.*

@Dao
interface SubTransactionDao {

  @Query("SELECT * FROM money_bags WHERE participant_id = :participantId AND currency = :currency LIMIT 1")
  @Transaction
  fun getMoneyBagFor(participantId: Long, currency: Currency): MoneyBagWithParticipant?

  @Update
  fun update(subTransaction: SubTransaction)

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
