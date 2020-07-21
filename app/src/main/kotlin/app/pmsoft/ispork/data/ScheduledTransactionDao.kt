package app.pmsoft.ispork.data

import androidx.room.*

@Dao
interface ScheduledTransactionDao {

  @Query("SELECT * FROM scheduled_transactions ORDER BY name ASC")
  @Transaction
  fun getAll(): List<FullScheduledTransaction>

  @Query("SELECT * FROM scheduled_transactions WHERE id IS :id LIMIT 1")
  @Transaction
  fun findById(id: Long): FullScheduledTransaction?

  @Query("UPDATE scheduled_transactions SET name = :name WHERE id IS :id")
  fun update(
    id: Long,
    name: String
  )

  fun enhance(scheduledTransaction: ScheduledTransaction): FullScheduledTransaction? {
    return findById(scheduledTransaction.id)
  }

  @Insert
  fun insert(scheduledTransactions: ScheduledTransaction): Long

  @Delete
  fun delete(scheduledTransactions: List<ScheduledTransaction>)

  @Delete
  fun delete(scheduledTransaction: ScheduledTransaction)
}
