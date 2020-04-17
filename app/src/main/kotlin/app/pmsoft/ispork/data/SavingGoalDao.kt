package app.pmsoft.ispork.data

import androidx.room.*
import androidx.room.Transaction

@Dao
interface SavingGoalDao {

  @Query("SELECT * FROM savingGoal ORDER BY target_date")
  @Transaction
  fun getAll(): List<SavingGoal>

  @Query("SELECT * FROM savingGoal WHERE id IS :id LIMIT 1")
  @Transaction
  fun findById(id: Long): SavingGoal?

  @Update
  fun update(savingGoal: SavingGoal)

  @Insert
  fun insert(savingGoal: SavingGoal): Long

  @Delete
  fun delete(savingGoals: List<SavingGoal>)

  @Delete
  fun delete(savingGoal: SavingGoal)
}
