package app.pmsoft.ispork.data

import androidx.room.*

@Dao
interface SavingGoalDao {

  @Query("SELECT * FROM savingGoal ORDER BY target_date")
  fun getAll(): List<SavingGoal>

  @Query("SELECT * FROM savingGoal WHERE id IS :id LIMIT 1")
  fun findById(id: Long): SavingGoal?

  @Query("SELECT * FROM savingGoal WHERE category_id is :categoryId")
  fun findByCategoryId(categoryId: Long): List<SavingGoal>

  @Update
  fun update(savingGoal: SavingGoal)

  @Insert
  fun insert(savingGoal: SavingGoal): Long

  @Delete
  fun delete(savingGoals: List<SavingGoal>)

  @Delete
  fun delete(savingGoal: SavingGoal)
}
