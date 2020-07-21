package app.pmsoft.ispork.data

import androidx.room.*

@Dao
interface SavingGoalDao {

  @Query("SELECT * FROM saving_goals ORDER BY target_date")
  @Transaction
  fun getAll(): List<FullSavingGoal>

  @Query("SELECT * FROM saving_goals WHERE id IS :id LIMIT 1")
  @Transaction
  fun findById(id: Long): FullSavingGoal?

  @Update
  fun update(savingGoal: SavingGoal)

  @Insert
  fun insert(savingGoal: SavingGoal): Long

  @Delete
  fun delete(savingGoals: List<SavingGoal>)

  @Delete
  fun delete(savingGoal: SavingGoal)
}
