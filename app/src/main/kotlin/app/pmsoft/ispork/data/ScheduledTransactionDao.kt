package app.pmsoft.ispork.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface ScheduledTransactionDao {

  @Query("SELECT * FROM scheduledTransaction ORDER BY name ASC")
  @androidx.room.Transaction
  fun getAll(): List<FullScheduledTransaction>

  @Query("SELECT * FROM scheduledTransaction WHERE id IS :id LIMIT 1")
  @androidx.room.Transaction
  fun findById(id: Long): FullScheduledTransaction?

  @Query("UPDATE scheduledTransaction SET name = :name WHERE id IS :id")
  fun update(
    id: Long,
    name: String
  )

  @Insert
  fun insert(scheduledTransactions: ScheduledTransaction): Long

  @Insert
  fun insertAll(vararg scheduledTransactions: ScheduledTransaction)

  @Delete
  fun delete(scheduledTransactions: List<ScheduledTransaction>)

  @Delete
  fun delete(scheduledTransaction: ScheduledTransaction)
}
