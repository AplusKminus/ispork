package app.pmsoft.ispork.data

import androidx.room.*

@Dao
interface TransactionDefinitionDao {

  @Query("SELECT * FROM transaction_definitions")
  @Transaction
  fun getAll(): List<FullTransactionDefinition>

  @Query("SELECT * FROM transaction_definitions WHERE booked IS 'true'")
  @Transaction
  fun getBooked(): List<FullTransactionDefinition>

  @Query("SELECT * FROM transaction_definitions WHERE id IS :id LIMIT 1")
  @Transaction
  fun findById(id: Long): FullTransactionDefinition?

  @Query("UPDATE transaction_definitions SET booked = :booked WHERE id = :id")
  fun update(id: Long, booked: Boolean)

  @Insert
  fun insert(transactions: TransactionDefinition): Long

  @Delete
  fun delete(transactionDefinitions: List<TransactionDefinition>)

  @Delete
  fun delete(transactionDefinition: TransactionDefinition)
}
