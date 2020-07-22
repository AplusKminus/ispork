package app.pmsoft.ispork.data

import androidx.room.*

@Dao
interface BudgetFlowDao {

  @Query("SELECT * FROM budget_flows")
  @Transaction
  fun getAll(): List<FullBudgetFlow>

  @Query("SELECT * FROM budget_flows WHERE id IS :id LIMIT 1")
  @Transaction
  fun findById(id: Long): FullBudgetFlow?

  @Insert
  fun insert(budgetFlows: BudgetFlow): Long

  @Update
  fun update(budgetFlow: BudgetFlow)

  @Query("DELETE FROM budget_flows WHERE sub_transaction_id = :subTransactionId AND id NOT IN (:usedIds)")
  fun deleteUnused(
    subTransactionId: Long,
    usedIds: List<Long>
  )

  @Delete
  fun delete(budgetFlow: BudgetFlow)
}
