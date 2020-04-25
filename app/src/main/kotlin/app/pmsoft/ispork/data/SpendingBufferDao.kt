package app.pmsoft.ispork.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface SpendingBufferDao {

  @Query("SELECT * FROM bufferDefinition")
  fun getAll(): List<SpendingBuffer>

  @Insert
  fun insert(spendingBuffer: SpendingBuffer): Long

  @Delete
  fun delete(spendingBuffer: SpendingBuffer)
}
