package app.pmsoft.ispork.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface ScheduledRhythmDao {

  @Query("SELECT * FROM scheduled_rhythms")
  fun getAll(): List<ScheduledRhythm>

  @Query("SELECT * FROM scheduled_rhythms WHERE id IS :id LIMIT 1")
  fun findById(id: Long): ScheduledRhythm?

  @Insert
  fun insert(scheduledOccurrence: ScheduledRhythm): Long

  @Delete
  fun delete(scheduledOccurrence: ScheduledRhythm)
}
