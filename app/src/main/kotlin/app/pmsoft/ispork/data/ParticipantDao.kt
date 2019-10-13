package app.pmsoft.ispork.data

import androidx.room.*
import androidx.room.Transaction

@Dao
interface ParticipantDao {

  @Query("SELECT id, type, starting_balance, name, balance FROM participant LEFT JOIN (SELECT SUM(amount) as balance, participant_id FROM subTransaction GROUP BY participant_id) ON participant_id = id ORDER BY name")
  @Transaction
  fun getAll(): List<FullParticipant>

  @Query("SELECT * FROM participant WHERE type IS 'ACCOUNT' ORDER BY name")
  fun getAccounts(): List<Participant>

  @Query("SELECT * FROM participant WHERE type IS 'PAYEE' ORDER BY name")
  fun getPayees(): List<Participant>

  @Query("SELECT * FROM participant WHERE type is 'PERSON' ORDER BY name")
  fun getPersons(): List<Participant>

  @Query("UPDATE participant SET name = :name, starting_balance = :startingBalance WHERE id IS :id")
  fun update(
    id: Long,
    name: String,
    startingBalance: Long?
  )

  @Query("SELECT id, type, starting_balance, name, balance FROM participant LEFT JOIN (SELECT SUM(amount) as balance, participant_id FROM subTransaction GROUP BY participant_id) ON participant_id = id WHERE id IS :id LIMIT 1")
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
