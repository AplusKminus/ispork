package app.pmsoft.ispork.data

import androidx.room.*

@Dao
interface BudgetPotAnnotationDao {

  @Query("SELECT * FROM budget_pot_annotations")
  @Transaction
  fun getAll(): List<FullBudgetPotAnnotation>

  @Query("SELECT * FROM budget_pot_annotations WHERE id IS :id LIMIT 1")
  @Transaction
  fun findById(id: Long): FullBudgetPotAnnotation?

  @Insert
  fun insert(budgetPotAnnotations: BudgetPotAnnotation): Long

  @Query("UPDATE budget_pot_annotations SET sub_transaction_id = :subTransactionId, budget_pot_id = :budgetPotId, notes = :notes, amount_in_transaction = :amountInTransaction, amount_in_budget = :amountInBudget WHERE id = :id")
  fun update(
    id: Long,
    subTransactionId: Long,
    amountInTransaction: Amount,
    amountInBudget: Amount,
    budgetPotId: Long?,
    notes: String?
  )

  @Query("DELETE FROM budget_pot_annotations WHERE sub_transaction_id = :subTransactionId AND id NOT IN (:usedIds)")
  fun deleteUnused(
    subTransactionId: Long,
    usedIds: List<Long>
  )

  @Delete
  fun delete(budgetPotAnnotation: BudgetPotAnnotation)
}
