package app.pmsoft.ispork.data

import androidx.room.*

@Dao
interface SubTransactionDao {

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
