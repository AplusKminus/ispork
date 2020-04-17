package app.pmsoft.ispork.data

import androidx.room.*
import androidx.room.Transaction

@Dao
interface BudgetPotAnnotationDao {

  @Query("SELECT * FROM budgetPotAnnotation")
  @Transaction
  fun getAll(): List<FullBudgetPotAnnotation>

  @Query("SELECT * FROM budgetPotAnnotation WHERE id IS :id LIMIT 1")
  @Transaction
  fun findById(id: Long): FullBudgetPotAnnotation?

  @Insert
  fun insert(budgetPotAnnotations: BudgetPotAnnotation): Long

  @Query("UPDATE budgetPotAnnotation SET sub_transaction_id = :subTransactionId, budget_pot_id = :budgetPotId, notes = :notes, amount = :amount WHERE id = :id")
  fun update(
    id: Long,
    subTransactionId: Long,
    amount: Long,
    budgetPotId: Long?,
    notes: String?
  )

  @Query("DELETE FROM budgetPotAnnotation WHERE sub_transaction_id = :subTransactionId AND id NOT IN (:usedIds)")
  fun deleteUnused(
    subTransactionId: Long,
    usedIds: List<Long>
  )

  @Delete
  fun delete(budgetPotAnnotation: BudgetPotAnnotation)
}
