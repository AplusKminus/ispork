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

  @Update
  fun update(budgetPotAnnotation: BudgetPotAnnotation)

  @Query("DELETE FROM budget_pot_annotations WHERE sub_transaction_id = :subTransactionId AND id NOT IN (:usedIds)")
  fun deleteUnused(
    subTransactionId: Long,
    usedIds: List<Long>
  )

  @Delete
  fun delete(budgetPotAnnotation: BudgetPotAnnotation)
}
