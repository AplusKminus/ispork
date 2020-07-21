package app.pmsoft.ispork.data

import androidx.room.*

@Dao
interface ParticipantDao {

  @Query("SELECT * FROM participants ORDER BY name")
  @Transaction
  fun getAll(): List<FullParticipant>

  @Query("SELECT * FROM participants WHERE type IS 'ACCOUNT' ORDER BY name")
  fun getAccounts(): List<Participant>

  @Query("SELECT * FROM participants WHERE type IS 'PAYEE' ORDER BY name")
  fun getPayees(): List<Participant>

  @Query("SELECT * FROM participants WHERE type is 'PERSON' ORDER BY name")
  fun getPersons(): List<Participant>

  @Query("SELECT * FROM participants WHERE id IS :id LIMIT 1")
  @Transaction
  fun findById(id: Long): FullParticipant?

  @Insert
  fun insert(participant: Participant): Long

  @Insert
  fun insertAll(vararg participants: Participant)

  @Delete
  fun delete(participant: List<Participant>)

  @Delete
  fun delete(participant: Participant)
}
