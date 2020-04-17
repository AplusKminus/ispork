package app.pmsoft.ispork.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface ScheduledOccurrenceDao {

  @Query("SELECT * FROM scheduledOccurrence")
  fun getAll(): List<ScheduledOccurrence>

  @Query("SELECT * FROM scheduledOccurrence WHERE id IS :id LIMIT 1")
  fun findById(id: Long): ScheduledOccurrence?

  @Insert
  fun insert(scheduledOccurrence: ScheduledOccurrence): Long

  @Delete
  fun delete(scheduledOccurrence: ScheduledOccurrence)
}
