package app.pmsoft.ispork.data

import androidx.room.*
import androidx.room.Transaction

@Dao
interface SavingGoalDao {

  @Query("SELECT * FROM savingGoal ORDER BY target_date")
  @Transaction
  fun getAll(): List<FullSavingGoal>

  @Query("SELECT * FROM savingGoal WHERE id IS :id LIMIT 1")
  @Transaction
  fun findById(id: Long): FullSavingGoal?

  @Query("SELECT * FROM savingGoal WHERE category_id is :categoryId")
  @Transaction
  fun findByCategoryId(categoryId: Long): List<FullSavingGoal>

  @Update
  fun update(savingGoal: SavingGoal)

  @Insert
  fun insert(savingGoal: SavingGoal): Long

  @Delete
  fun delete(savingGoals: List<SavingGoal>)

  @Delete
  fun delete(savingGoal: SavingGoal)
}
