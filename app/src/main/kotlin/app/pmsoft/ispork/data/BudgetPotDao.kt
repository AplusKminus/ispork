package app.pmsoft.ispork.data

import androidx.room.*
import androidx.room.Transaction

@Dao
interface BudgetPotDao {

  @Query("SELECT * FROM budgetPot ORDER BY priority")
  @Transaction
  fun getAll(): List<FullBudgetPot>

  @Query("SELECT * FROM budgetPot WHERE id IS :id LIMIT 1")
  @Transaction
  fun findById(id: Long): FullBudgetPot?

  @Query("SELECT * FROM budgetPot WHERE category_id IS :categoryId")
  @Transaction
  fun findByCategoryId(categoryId: Long): List<FullBudgetPot>

  @Update
  fun update(budgetPot: BudgetPot)

  @Insert
  fun insert(budgetPot: BudgetPot): Long

  @Delete
  fun delete(budgetPots: List<BudgetPot>)

  @Delete
  fun delete(budgetPot: BudgetPot)
}
