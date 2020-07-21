package app.pmsoft.ispork.data

import androidx.room.*

@Dao
interface SpendingBufferDao {

  @Query("SELECT * FROM spending_buffers")
  @Transaction
  fun getAll(): List<FullSpendingBuffer>

  @Insert
  fun insert(spendingBuffer: SpendingBuffer): Long

  @Delete
  fun delete(spendingBuffer: SpendingBuffer)
}
