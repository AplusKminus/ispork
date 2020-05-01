package app.pmsoft.ispork.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import java.util.*

@Dao
interface TransactionDao {

  @Query("SELECT * FROM `transaction` ORDER BY entry_date DESC")
  @androidx.room.Transaction
  fun getAll(): List<FullTransaction>

  @Query("SELECT * FROM `transaction` WHERE id IS :id LIMIT 1")
  @androidx.room.Transaction
  fun findById(id: Long): FullTransaction?

  @Query("UPDATE `transaction` SET entry_date = :entryDate WHERE id IS :id")
  fun update(
    id: Long,
    entryDate: Date
  )

  @Insert
  fun insert(transactions: Transaction): Long

  @Insert
  fun insertAll(vararg transactions: Transaction)

  @Delete
  fun delete(transactions: List<Transaction>)

  @Delete
  fun delete(transaction: Transaction)
}
